package com.devapps.igor.Screens.AddPlayer;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.AdventureProgress.SessionsListAdapter;
import com.devapps.igor.Screens.CreateCharacter.CreateCharacterFragment;

import java.util.ArrayList;

/**
 * Created by hercules on 24/11/17.
 */

public class SearchedPlayersListAdapter extends
        RecyclerView.Adapter<SearchedPlayersListAdapter.SearchedPlayersViewHolder> {
    ArrayList<Profile> mSearchedPlayersList;
    String mAdventureId;
    FragmentActivity mActivity;

    SearchedPlayersListAdapter(ArrayList<Profile> list, String adventureId, FragmentActivity activity) {
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
        holder.mBtnAdd.setTag(mSearchedPlayersList.get(position).getId());
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerId = (String) view.getTag();
                Fragment fragment = CreateCharacterFragment.newInstance(mAdventureId, playerId);
                mActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mSearchedPlayersList.size();
    }

    public void changeData(ArrayList<Profile> p) {
        mSearchedPlayersList = p;
        notifyDataSetChanged();
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
