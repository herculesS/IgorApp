package com.devapps.igor.Screens.AdventureProgress;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

    public CharacterListAdapter(ArrayList<Character> characters, ArrayList<Profile> profiles) {
        mCharacters = characters;
        mProfiles = profiles;
    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.characters_list_view_elem, parent, false);
        return new CharacterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {

        holder.mTextViewCharacterName.setText(mCharacters.get(position).getName());
        holder.mTextViewPlayerName.setText(mProfiles.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return mCharacters.size();
    }

    public class CharacterViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewCharacterName;
        private TextView mTextViewPlayerName;

        public CharacterViewHolder(View itemView) {
            super(itemView);

            mTextViewCharacterName = (TextView) itemView.findViewById(R.id.text_view_character_name);
            mTextViewPlayerName = (TextView) itemView.findViewById(R.id.text_view_player_name);


        }
    }
}

