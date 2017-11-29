package com.devapps.igor.Screens.Edit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.BackableFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class EditSummaryFragment extends Fragment implements BackableFragment {
    private static final String ARG_ADVENTURE_OR_SESSION = "ADVENTURE";
    private static final String ARG_ADVENTURE_ID = "ADVENTURE_ID";
    private static final String ARG_SESSION_ID = "SESSION_ID";

    // TODO: Rename and change types of parameters
    private String mAdventureOrSession;
    private String mAdventureId;
    private int mSessionId;

    private TextView mEditSummaryLabel;
    private EditText mSummaryEditTextView;
    private ImageView mBtnClose;
    private ImageView mBtnCloseEdit;

    private Adventure mAdventure;


    public EditSummaryFragment() {
        // Required empty public constructor
    }

    public static EditSummaryFragment newInstance(String adventureOrSession, String adventureId, int sessionId) {
        EditSummaryFragment fragment = new EditSummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADVENTURE_OR_SESSION, adventureOrSession);
        args.putString(ARG_ADVENTURE_ID, adventureId);
        args.putInt(ARG_SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdventureOrSession = getArguments().getString(ARG_ADVENTURE_OR_SESSION);
            mAdventureId = getArguments().getString(ARG_ADVENTURE_ID);
            mSessionId = getArguments().getInt(ARG_SESSION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_summary, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeMembers(view);

        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAdventure = dataSnapshot.getValue(Adventure.class);
                setListeners();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initializeMembers(View view) {
        mEditSummaryLabel = (TextView) view.findViewById(R.id.edit_summary_label);
        mSummaryEditTextView = (EditText) view.findViewById(R.id.summary_edit_text_view);
        mBtnClose = (ImageView) view.findViewById(R.id.btn_close);
        mBtnCloseEdit = (ImageView) view.findViewById(R.id.btn_close_edit_mode);
    }


    private void setListeners() {
        if (mAdventureOrSession.equals(Adventure.ADVENTURE_TAG)) {
            mEditSummaryLabel.setText(R.string.edit_adventure_label);
            mSummaryEditTextView.setHint(R.string.edit_adventure_summary_hint);
            mSummaryEditTextView.setText(mAdventure.getSummary());

        } else {
            mEditSummaryLabel.setText(R.string.edit_session_label);
            mSummaryEditTextView.setHint(R.string.edit_session_summary_hint);
            mSummaryEditTextView.setText(mAdventure.getSessions().get(mSessionId).getSummary());
        }

        mSummaryEditTextView.requestFocus();
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mSummaryEditTextView, InputMethodManager.SHOW_IMPLICIT);
        }


        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();

            }
        });

        mBtnCloseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeEditBehavior();
            }
        });
    }

    private void closeEditBehavior() {
        if (mAdventureOrSession.equals(Adventure.ADVENTURE_TAG)) {
            mAdventure.setSummary(mSummaryEditTextView.getText().toString());
        } else {
            mAdventure.getSessions().get(mSessionId).
                    setSummary(mSummaryEditTextView.getText().toString());
        }

        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).setValue(mAdventure);


        Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void back() {
        Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }
}