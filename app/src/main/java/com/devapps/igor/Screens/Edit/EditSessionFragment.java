package com.devapps.igor.Screens.Edit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.AdventureRequestManager;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.BackableFragment;

import java.util.Calendar;

public class EditSessionFragment extends Fragment implements AdventureRequestManager.AdventureLoaderListener, BackableFragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";
    private static final String SESSION_ID = "SESSION_ID";

    private String mAdventureId;
    private int mSessionId;

    private EditText mSessionTitleEditText;
    private EditText mSessionSummaryEditText;
    private Button mBtnSessionDate;
    private ImageView mBtnReady;
    private Calendar mDate = Calendar.getInstance();
    private DatePickerDialog mDatePickerDialog;


    public EditSessionFragment() {
        // Required empty public constructor
    }

    public static EditSessionFragment newInstance(String adventureId, int sessionId) {
        EditSessionFragment fragment = new EditSessionFragment();
        Bundle args = new Bundle();
        args.putString(ADVENTURE_ID, adventureId);
        args.putInt(SESSION_ID, sessionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdventureId = getArguments().getString(ADVENTURE_ID);
            mSessionId = getArguments().getInt(SESSION_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_session, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeViews(view);
        AdventureRequestManager loader = new AdventureRequestManager();
        loader.setAdventureLoaderListener(this);
        loader.load(mAdventureId);
    }

    private void initializeViews(View view) {
        mSessionTitleEditText = (EditText) view.findViewById(R.id.edit_text_name);
        mSessionSummaryEditText = (EditText) view.findViewById(R.id.edit_text_summary);
        mBtnReady = (ImageView) view.findViewById(R.id.button_ready);
        mBtnSessionDate = (Button) view.findViewById(R.id.session_date_picker);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAdventureLoaded(final Adventure a) {
        mSessionTitleEditText.setText(a.getSessions().get(mSessionId).getTitle());
        mSessionSummaryEditText.setText(a.getSessions().get(mSessionId).getSummary());
        mBtnSessionDate.setText(Session.formatSessionDateToDayMonth(a.getSessions().get(mSessionId).getDate()));
        mBtnSessionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });
        mBtnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.getSessions().get(mSessionId).setTitle(mSessionTitleEditText.getText().toString().trim());
                a.getSessions().get(mSessionId).setSummary(mSessionSummaryEditText.getText().toString().trim());
                String date = mDate.get(Calendar.DAY_OF_MONTH) + "/" +
                        mDate.get(Calendar.MONTH) + "/" + mDate.get(Calendar.YEAR);
                a.getSessions().get(mSessionId).setDate(date);
                AdventureRequestManager.saveAdventure(a);

                Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    private void setDate() {
        Calendar newCalendar = mDate;
        if (mDatePickerDialog == null) {
            mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    mDate.set(year, month, day);
                    mBtnSessionDate.setText(Session.
                            formatSessionDateToDayMonth(mDate.get(Calendar.DAY_OF_MONTH) + "/" + mDate.get(Calendar.MONTH)));
                }
            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        }
        mDatePickerDialog.show();

    }

    @Override
    public void back() {
        Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }
}
