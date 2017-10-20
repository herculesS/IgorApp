package com.devapps.igor.Screens.AdventureProgress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.CreateNewSession.CreateNewSessionFragment;
import com.devapps.igor.Screens.EditSummary.EditSummaryFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AdventureProgressFragment extends Fragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;

    private TextView mAdventureTitleTextView;
    private TextView mAdventureSummaryTextView;
    private ImageView mBtnSeeMore;
    private RecyclerView mSessionsRecyclerView;
    private RecyclerView.LayoutManager mSessionLayoutManager;
    private ImageView mBtnAddSession;
    private Context mContext;

    public AdventureProgressFragment() {
        // Required empty public constructor
    }


    public static AdventureProgressFragment newInstance(String adventureId) {
        AdventureProgressFragment fragment = new AdventureProgressFragment();
        Bundle args = new Bundle();
        args.putString(ADVENTURE_ID, adventureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mAdventureId = getArguments().getString(ADVENTURE_ID);
            Log.d("AdventureId", mAdventureId);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_progress, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        InitializeMembers(view);
        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAdventure = dataSnapshot.getValue(Adventure.class);
                mAdventureTitleTextView.setText(mAdventure.getName());
                mAdventureSummaryTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = EditSummaryFragment.newInstance(Adventure.ADVENTURE_TAG, mAdventureId, 0);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment).commit();
                    }
                });
                mAdventureSummaryTextView.setText(mAdventure.getSummary());
                mAdventureSummaryTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdventureSummaryTextView.getLineCount() > 6) {
                            mAdventureSummaryTextView.setMaxLines(6);
                            mBtnSeeMore.setVisibility(View.VISIBLE);
                        }

                    }
                });

                mSessionsRecyclerView.setAdapter(new SessionsListAdapter(mAdventure.getSessions(), getActivity(), mAdventureId));
                mBtnSeeMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mAdventureSummaryTextView.getMaxLines() > 6) {
                            mAdventureSummaryTextView.setMaxLines(6);
                            mAdventureSummaryTextView.getMaxLines();
                        } else {
                            mAdventureSummaryTextView.setMaxLines(mAdventureSummaryTextView.getLineCount());
                            mAdventureSummaryTextView.getMaxLines();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mBtnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = CreateNewSessionFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();


            }
        });

    }

    private void InitializeMembers(View view) {
        mContext = view.getContext();
        mAdventureTitleTextView = (TextView) view.findViewById(R.id.adventure_progress_adventure_title);
        mAdventureSummaryTextView = (TextView) view.findViewById(R.id.adventure_progress_adventure_summary);
        mBtnSeeMore = (ImageView) view.findViewById(R.id.adventure_progress_btn_see_more);
        mSessionsRecyclerView = (RecyclerView) view.findViewById(R.id.adventure_progress_sessions_list);
        mSessionLayoutManager = new LinearLayoutManager(mContext);
        mSessionsRecyclerView.setLayoutManager(mSessionLayoutManager);
        mBtnAddSession = (ImageView) view.findViewById(R.id.adventure_progress_btn_add_session);
        mBtnSeeMore.setVisibility(View.GONE);
    }


}
