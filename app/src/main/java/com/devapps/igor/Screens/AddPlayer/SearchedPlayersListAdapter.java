package com.devapps.igor.Screens.AddPlayer;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Notification;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Util.DataObjectUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by hercules on 24/11/17.
 */

public class SearchedPlayersListAdapter extends
        RecyclerView.Adapter<SearchedPlayersListAdapter.SearchedPlayersViewHolder> {
    private Adventure mAdventure;
    private ArrayList<Profile> mSearchedPlayersList;
    private String mAdventureId;
    private FragmentActivity mActivity;

    SearchedPlayersListAdapter(ArrayList<Profile> list, String adventureId,
                               FragmentActivity activity, Adventure adventure) {
        mSearchedPlayersList = list;
        mAdventureId = adventureId;
        mActivity = activity;
    }


    @Override
    public SearchedPlayersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_players_list_item, parent, false);
        return new SearchedPlayersListAdapter.SearchedPlayersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchedPlayersViewHolder holder, final int position) {
        holder.mNameTextView.setText(mSearchedPlayersList.get(position).getName());
        holder.mBtnAdd.setTag(mSearchedPlayersList.get(position));
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnAddOnClick(view, holder, position);
            }
        });

        updateVisibility(holder, position);

    }

    private void updateVisibility(SearchedPlayersViewHolder holder, int position) {
        if (DataObjectUtils.isPlayerInTheAdventure(mAdventure, mSearchedPlayersList.get(position).getId())) {
            holder.mBtnAdd.setVisibility(View.GONE);
            holder.mAlreadyAdded.setVisibility(View.VISIBLE);
        }
        if (DataObjectUtils.isAddPlayerNotificationAlreadyAdded(mAdventure, mSearchedPlayersList.get(position))) {
            holder.mBtnAdd.setVisibility(View.GONE);
            holder.mAlreadyAdded.setVisibility(View.VISIBLE);
            holder.mAlreadyAdded.setImageResource(R.drawable.waiting_invite_icon);
        }
    }

    private void BtnAddOnClick(View view, SearchedPlayersViewHolder holder, int position) {

        if (mAdventure != null) {
            Profile p = (Profile) view.getTag();
            if (!DataObjectUtils.isPlayerInTheAdventure(mAdventure, p.getId())) {
                if (!DataObjectUtils.isAddPlayerNotificationAlreadyAdded(mAdventure, p)) {
                    Notification notification = new Notification();
                    notification.setAdventureId(mAdventureId);
                    notification.setNotificationType(Notification.NotificationType.AddedAsPlayer);
                    notification.setAdventureName(mAdventure.getName());
                    p.addNotification(notification);

                    DatabaseReference ref = Database.getUsersReference();
                    ref.child(p.getId()).setValue(p);
                }
            }
        }
        updateVisibility(holder, position);
    }


    @Override
    public int getItemCount() {
        return mSearchedPlayersList.size();
    }

    public void changeData(ArrayList<Profile> p) {
        mSearchedPlayersList = p;
        notifyDataSetChanged();
    }

    public void setAdventure(Adventure adventure) {
        mAdventure = adventure;
    }

    public class SearchedPlayersViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTextView;
        public Button mBtnAdd;
        public ImageView mAlreadyAdded;

        public SearchedPlayersViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.player_name);
            mBtnAdd = (Button) itemView.findViewById(R.id.button_search);
            mAlreadyAdded = (ImageView) itemView.findViewById(R.id.added_icon);

        }
    }
}
