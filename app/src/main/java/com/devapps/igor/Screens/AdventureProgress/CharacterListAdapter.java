package com.devapps.igor.Screens.AdventureProgress;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by hercules on 25/10/17.
 */

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder> {

    private ArrayList<Character> mCharacters;
    private ArrayList<Profile> mProfiles;
    private String mUserId;
    private Character mDmCharacter;
    private boolean mEditMode = false;

    public CharacterListAdapter(ArrayList<Character> characters,
                                ArrayList<Profile> profiles, Character dmCharacter, boolean editMode) {
        mCharacters = characters;
        mProfiles = profiles;
        mDmCharacter = dmCharacter;
        mEditMode = editMode;
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

    public void changeEditMode(boolean mode) {
        mEditMode = mode;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CharacterViewHolder holder, int position) {
        if (position == 0) {
            if (mDmCharacter != null) {
                if (mEditMode && mDmCharacter.getPlayerId().equals(mUserId)) {
                    holder.mBtnEditDm.setVisibility(View.VISIBLE);
                } else {
                    holder.mBtnEditDm.setVisibility(View.GONE);
                }
                holder.mTextViewDMName.setText(mDmCharacter.getName());
                holder.mTextViewDMSummary.setText(mDmCharacter.getSummary());
            } else {
                holder.mTextViewDMName.setText(R.string.no_master_assigned);
                holder.mTextViewDMSummary.setVisibility(View.INVISIBLE);
            }
        } else {
            position--;
            if (mEditMode) {
                if (mUserId.equals(mProfiles.get(position).getId()))
                    holder.mEditLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mEditLayout.setVisibility(View.GONE);
            }
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
        private LinearLayout mEditLayout;
        private ImageView mBtnDelete;
        private ImageView mBtnEditCharacter;
        private ImageView mBtnEditDm;


        public CharacterViewHolder(View itemView) {
            super(itemView);
            mTextViewDMName = (TextView) itemView.findViewById(R.id.text_view_dm_name);
            mTextViewDMSummary = (TextView) itemView.findViewById(R.id.text_view_dm_summary);
            mTextViewCharacterName = (TextView) itemView.findViewById(R.id.text_view_character_name);
            mTextViewPlayerName = (TextView) itemView.findViewById(R.id.text_view_player_name);
            mBtnDelete = (ImageView) itemView.findViewById(R.id.characters_item_trash_can);
            mBtnEditCharacter = (ImageView) itemView.findViewById(R.id.characters_item_edit);
            mBtnEditDm = (ImageView) itemView.findViewById(R.id.dm_item_edit);
            mEditLayout = (LinearLayout) itemView.findViewById(R.id.characters_edit_layout);

        }
    }
}

