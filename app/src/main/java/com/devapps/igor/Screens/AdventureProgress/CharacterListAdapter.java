package com.devapps.igor.Screens.AdventureProgress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;

import java.util.ArrayList;

/**
 * Created by hercules on 25/10/17.
 */

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder> {

    private ArrayList<Character> mCharacters;
    private ArrayList<Profile> mProfiles;
    private Character mDmCharacter;

    public CharacterListAdapter(ArrayList<Character> characters,
                                ArrayList<Profile> profiles, Character dmCharacter) {
        mCharacters = characters;
        mProfiles = profiles;
        mDmCharacter = dmCharacter;

    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.master_item_list_item, parent, false);
            return new CharacterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.characters_list_view_elem, parent, false);
            return new CharacterViewHolder(view);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        if (position == 0) {
            if (mDmCharacter != null) {
                holder.mTextViewDMName.setText(mDmCharacter.getName());
                holder.mTextViewDMSummary.setText(mDmCharacter.getSummary());
            } else {
                holder.mTextViewDMName.setText(R.string.no_master_assigned);
                holder.mTextViewDMSummary.setVisibility(View.INVISIBLE);
            }
        } else {
            position--;
            holder.mTextViewCharacterName.setText(mCharacters.get(position).getName());
            holder.mTextViewPlayerName.setText(mProfiles.get(position).getName());
        }


    }

    @Override
    public int getItemCount() {
        return mCharacters.size() < mProfiles.size() ? mCharacters.size() + 1
                : mProfiles.size() + 1;
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewCharacterName;
        private TextView mTextViewPlayerName;
        private TextView mTextViewDMName;
        private TextView mTextViewDMSummary;

        public CharacterViewHolder(View itemView) {
            super(itemView);
            mTextViewDMName = (TextView) itemView.findViewById(R.id.text_view_dm_name);
            mTextViewDMSummary = (TextView) itemView.findViewById(R.id.text_view_dm_summary);
            mTextViewCharacterName = (TextView) itemView.findViewById(R.id.text_view_character_name);
            mTextViewPlayerName = (TextView) itemView.findViewById(R.id.text_view_player_name);


        }
    }
}

