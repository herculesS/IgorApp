package com.devapps.igor.Screens.AdventureProgress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.CreateNewSession.CreateNewSessionFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdventureProgressFragment extends Fragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;

    private TextView mAdventureTitleTextView;
    private TextView mAdventureSummaryTextView;
    private ImageView mBtnSeeMore;
    private ListView mSessionsListView;
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
        mAdventureTitleTextView = (TextView) view.findViewById(R.id.adventure_progress_adventure_title);
        mAdventureSummaryTextView = (TextView) view.findViewById(R.id.adventure_progress_adventure_summary);
        mBtnSeeMore = (ImageView) view.findViewById(R.id.adventure_progress_btn_see_more);
        mSessionsListView = (ListView) view.findViewById(R.id.adventure_progress_sessions_list);
        mBtnAddSession = (ImageView) view.findViewById(R.id.adventure_progress_btn_add_session);
        mContext = view.getContext();
        mSessionsListView.setItemsCanFocus(true);
        mBtnSeeMore.setVisibility(View.GONE);

        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAdventure = dataSnapshot.getValue(Adventure.class);
                mAdventureTitleTextView.setText(mAdventure.getName());
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

                mSessionsListView.setAdapter(new SessionsAdapter(mContext, mAdventure.getSessions()));
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

    private class SessionsAdapter extends ArrayAdapter<Session> {
        public SessionsAdapter(Context context, ArrayList<Session> sessions) {
            super(context, 0, sessions);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Get the data item for this position
            Session s = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.sessions_list_view_elem, parent, false);
            }


            TextView sessionDateTextView = (TextView) convertView.findViewById(R.id.session_list_view_item_date);
            TextView sessionTitleTextView = (TextView) convertView.findViewById(R.id.session_list_view_item_title);
            TextView sessionSummaryTextView = (TextView) convertView.findViewById(R.id.session_list_view_item_summary);

            String[] date = s.getDate().split("/");
            sessionDateTextView.setText(date[0] + "/" + date[1]);
            sessionTitleTextView.setText(s.getTitle());
            sessionSummaryTextView.setText(s.getSummary());


            sessionTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View parent = (View) view.getParent();
                    if (parent != null) {
                        View root = (View) view.getParent().getParent();
                        if (root != null) {
                            TextView sessionSummaryTextView = (TextView) root.findViewById(R.id.session_list_view_item_summary);
                            if (sessionSummaryTextView != null) {

                                if (!sessionSummaryTextView.isShown()) {

                                    sessionSummaryTextView.setVisibility(View.VISIBLE);
                                } else {
                                    sessionSummaryTextView.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                }
            });


            // Return the completed view to render on screen
            return convertView;
        }

    }
}
