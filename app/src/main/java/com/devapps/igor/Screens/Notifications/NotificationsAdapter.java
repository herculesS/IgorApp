package com.devapps.igor.Screens.Notifications;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Notification;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.CreateCharacter.CreateCharacterFragment;
import com.devapps.igor.Screens.HomeJogosFrontend.FragmentAdventure;
import com.devapps.igor.Screens.MainActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by hercules on 25/11/17.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {
    ArrayList<Notification> mNotifications;
    FragmentActivity mActivity;
    String mPlayerId;

    NotificationsAdapter(ArrayList<Notification> notifications, FragmentActivity activity, String playerId) {
        mNotifications = notifications;
        mActivity = activity;
        mPlayerId = playerId;
    }

    @Override
    public NotificationsAdapter.NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_list_item, parent, false);
        return new NotificationsAdapter.NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationsAdapter.NotificationsViewHolder holder, final int position) {
        switch (mNotifications.get(position).getNotificationType()) {
            case AddedAsPlayer:
                setCommonBehavior(holder, position, "Você foi adicionado à aventura");
                holder.mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = CreateCharacterFragment.
                                newInstance(mNotifications.get(position).getAdventureId(), mPlayerId, position);
                        mActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment).commit();
                    }
                });

                break;
            case AddedAsMaster:
                setCommonBehavior(holder, position, "Você foi selecionado como novo mestre da aventura");
                holder.mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
        }


    }

    private void setCommonBehavior(NotificationsViewHolder holder, final int position, String text) {
        holder.mYouWereAddedTextView.setText(text + " " +
                mNotifications.get(position).getAdventureName());
        holder.mBtnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                denyBtnOnClickAction(position);
            }
        });
    }

    private void denyBtnOnClickAction(int position) {
        MainActivity activity = (MainActivity) mActivity;
        Profile user = activity.getUserProfile();
        user.removeNotification(position);
        DatabaseReference ref = Database.getUsersReference();
        ref.child(user.getId()).setValue(user);
        mNotifications = user.getNotifications();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mNotifications.size());

        if (mNotifications == null || mNotifications.size() == 0) {
            activity.updateNotifications();
            Fragment fragment = new FragmentAdventure();
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }

    }

    @Override
    public int getItemCount() {
        return mNotifications == null ? 0 : mNotifications.size();
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {
        TextView mYouWereAddedTextView;
        ImageView mBtnAccept, mBtnDeny;

        public NotificationsViewHolder(View itemView) {
            super(itemView);

            mYouWereAddedTextView = (TextView) itemView.findViewById(R.id.you_were_added_text);
            mBtnAccept = (ImageView) itemView.findViewById(R.id.accept_btn);
            mBtnDeny = (ImageView) itemView.findViewById(R.id.deny_btn);

        }


    }
}
