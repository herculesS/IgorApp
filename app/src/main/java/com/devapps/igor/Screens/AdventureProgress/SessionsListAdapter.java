package com.devapps.igor.Screens.AdventureProgress;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.Edit.EditSessionFragment;
import com.devapps.igor.Screens.Edit.EditSummaryFragment;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by hercules on 06/10/17.
 */

public class SessionsListAdapter extends RecyclerView.Adapter<SessionsListAdapter.SessionViewHolder> {
    private boolean mEditMode;
    Adventure mAdventure;
    String mAdventureId;
    FragmentActivity mActivity;

    SessionsListAdapter(Adventure a, FragmentActivity activity, String adventureId, boolean editMode) {
        mAdventure = a;
        mActivity = activity;
        mAdventureId = adventureId;
        mEditMode = editMode;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sessions_list_view_elem, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, final int position) {

        updateEditMode(holder);
        holder.mDateTextView.setText(Session.formatSessionDateToDayMonth(mAdventure.getSessions().get(position).getDate()));
        holder.mTitleView.setText(mAdventure.getSessions().get(position).getTitle());
        holder.mSummaryTextView.setText(mAdventure.getSessions().get(position).getSummary());
        holder.mSummaryTextView.setTag(position);

        holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteSession(position);

            }
        });
        holder.mBtnEdit.setTag(position);
        holder.mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditSession(view);
            }
        });

        holder.itemView.setTag(holder.mSummaryTextView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView summary = (TextView) view.getTag();
                if (summary != null) {
                    if (!summary.isShown()) {
                        summary.setVisibility(View.VISIBLE);
                    } else {
                        summary.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void onEditSession(View view) {
        int position = (int) view.getTag();
        Fragment fragment = EditSessionFragment.newInstance(mAdventureId, position);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    private void onDeleteSession(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Deseja deletar a sessão "
                + mAdventure.getSessions().get(position).getTitle() + "?");
        builder.setPositiveButton("Sim", new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdventure.getSessions().remove(position);
                DatabaseReference ref = Database.getAdventuresReference();
                ref.child(mAdventureId).setValue(mAdventure);
                notifyDataSetChanged();

                dialog.cancel();
            }

        });

        builder.setNegativeButton("Não", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });

        builder.show();
    }

    private void updateEditMode(SessionViewHolder holder) {
        if (mEditMode) {
            holder.mEditLayout.setVisibility(View.VISIBLE);

        } else {
            holder.mEditLayout.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return mAdventure.getSessions().size();
    }

    public void changeEditMode(Boolean editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {
        public ImageView mBtnEdit;
        public TextView mDateTextView;
        public TextView mTitleView;
        public TextView mSummaryTextView;
        public ImageView mBtnDelete;
        public LinearLayout mEditLayout;

        public SessionViewHolder(View itemView) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.session_list_view_item_date);
            mTitleView = (TextView) itemView.findViewById(R.id.session_list_view_item_title);
            mSummaryTextView = (TextView) itemView.findViewById(R.id.session_list_view_item_summary);
            mBtnDelete = (ImageView) itemView.findViewById(R.id.sessions_item_trash_can);
            mBtnEdit = (ImageView) itemView.findViewById(R.id.sessions_item_edit);
            mEditLayout = (LinearLayout) itemView.findViewById(R.id.sessions_edit_layout);
        }
    }
}
