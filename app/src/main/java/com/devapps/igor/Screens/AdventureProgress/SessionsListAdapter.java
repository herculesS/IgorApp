package com.devapps.igor.Screens.AdventureProgress;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.Screens.EditSummary.EditSummaryFragment;

import java.util.ArrayList;

/**
 * Created by hercules on 06/10/17.
 */

public class SessionsListAdapter extends RecyclerView.Adapter<SessionsListAdapter.SessionViewHolder> {
    ArrayList<Session> mSessions;
    String mAdventureId;
    FragmentActivity mActivity;

    SessionsListAdapter(ArrayList<Session> sessions, FragmentActivity activity, String adventureId) {
        mSessions = sessions;
        mActivity = activity;
        mAdventureId = adventureId;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sessions_list_view_elem, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {


        holder.mDateTextView.setText(Session.formatSessionDateToDayMonth(mSessions.get(position).getDate()));
        holder.mTitleView.setText(mSessions.get(position).getTitle());
        holder.mSummaryTextView.setText(mSessions.get(position).getSummary());
        holder.mSummaryTextView.setTag(position);
        holder.mSummaryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sessionId = (Integer) view.getTag();
                Fragment fragment = EditSummaryFragment.newInstance(Session.SESSION_TAG, mAdventureId, sessionId);
                mActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });


        holder.mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View) view.getParent();
                if (parent != null) {
                    View root = (View) view.getParent().getParent();
                    if (root != null) {
                        setSummarySeeMoreBehavior(root);
                    }
                }
            }
        });


    }

    private void setSummarySeeMoreBehavior(View root) {
        TextView sessionSummaryTextView = (TextView) root.findViewById(R.id.session_list_view_item_summary);
        if (sessionSummaryTextView != null) {

            if (!sessionSummaryTextView.isShown()) {

                sessionSummaryTextView.setVisibility(View.VISIBLE);
            } else {
                sessionSummaryTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mSessions.size();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {
        public TextView mDateTextView;
        public TextView mTitleView;
        public TextView mSummaryTextView;

        public SessionViewHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.session_list_view_item_date);
            mTitleView = (TextView) itemView.findViewById(R.id.session_list_view_item_title);
            mSummaryTextView = (TextView) itemView.findViewById(R.id.session_list_view_item_summary);

        }
    }
}
