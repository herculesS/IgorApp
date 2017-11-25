package com.devapps.igor.Screens.AddPlayer;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Notification;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.AdventureProgress.SessionsListAdapter;
import com.devapps.igor.Screens.CreateCharacter.CreateCharacterFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(SearchedPlayersViewHolder holder, int position) {
        holder.mNameTextView.setText(mSearchedPlayersList.get(position).getName());
        holder.mBtnAdd.setTag(mSearchedPlayersList.get(position));
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BtnAddOnClick(view);
            }
        });

    }

    private void BtnAddOnClick(View view) {
        if (mAdventure != null) {
            Profile p = (Profile) view.getTag();
            if (!isPlayerInTheAdventure(p.getId())) {
                if (!isNotificationAlreadyAdded(p)) {
                    Notification notification = new Notification();
                    notification.setAdventureId(mAdventureId);
                    notification.setNotificationType(Notification.NotificationType.AddedAsPlayer);
                    notification.setAdventureName(mAdventure.getName());
                    p.addNotification(notification);

                    DatabaseReference ref = Database.getUsersReference();
                    ref.child(p.getId()).setValue(p);

                    Toast.makeText(mActivity, "Notificação enviada ao jogador",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mActivity, "Notificação já foi enviada ao jogador",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mActivity, "Jogador já foi adicionado à aventura",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNotificationAlreadyAdded(Profile p) {
        if (p.getNotifications() != null) {
            for (Notification n : p.getNotifications()) {
                if (n.getAdventureId().equals(mAdventureId) && n.getNotificationType()
                        == Notification.NotificationType.AddedAsPlayer) {
                    return true;
                }
            }
            return false;

        } else {
            return false;
        }
    }

    private boolean isPlayerInTheAdventure(String playerId) {
        boolean isPlayerInTheAdventure = false;
        for (Character c : mAdventure.getCharacters()) {
            if (c.getPlayerId().equals(playerId)) {
                isPlayerInTheAdventure = true;
                break;
            }
        }
        return isPlayerInTheAdventure;
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

        public SearchedPlayersViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.player_name);
            mBtnAdd = (Button) itemView.findViewById(R.id.button_search);

        }
    }
}
