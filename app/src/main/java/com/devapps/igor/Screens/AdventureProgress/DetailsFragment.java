package com.devapps.igor.Screens.AdventureProgress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.AdventureRequestManager;


public class DetailsFragment extends Fragment implements AdventureRequestManager.AdventureLoaderListener, Editable {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;


    private TextView mAdventureSummaryTextView;
    private ImageView mBtnSeeMore;
    private RecyclerView mSessionsRecyclerView;
    private RecyclerView.LayoutManager mSessionLayoutManager;
    private AdventureRequestManager mAdventureRequestManager;
    private Context mContext;
    boolean mEditMode;
    private SessionsListAdapter mSessionAdapter;


    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String adventureId) {
        DetailsFragment fragment = new DetailsFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        InitializeMembers(view);

        mAdventureRequestManager = new AdventureRequestManager();
        mAdventureRequestManager.setAdventureLoaderListener(this);
        mAdventureRequestManager.load(mAdventureId);
    }

    private void InitializeMembers(View view) {
        mContext = view.getContext();
        mAdventureSummaryTextView = (TextView) view.findViewById(R.id.adventure_progress_adventure_summary);
        mBtnSeeMore = (ImageView) view.findViewById(R.id.adventure_progress_btn_see_more);
        mSessionsRecyclerView = (RecyclerView) view.findViewById(R.id.adventure_progress_sessions_list);
        mSessionLayoutManager = new LinearLayoutManager(mContext);
        mSessionsRecyclerView.setLayoutManager(mSessionLayoutManager);
        mBtnSeeMore.setVisibility(View.GONE);
    }

    @Override
    public void onAdventureLoaded(Adventure a) {
        mAdventure = a;
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
        mSessionAdapter = new SessionsListAdapter(mAdventure,
                getActivity(), mAdventureId, mEditMode);
        mSessionsRecyclerView.setAdapter(mSessionAdapter);

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
    public void editMode(boolean mode) {
        mEditMode = mode;
        if (mSessionAdapter != null) {
            mSessionAdapter.changeEditMode(mode);
        }

    }
}


