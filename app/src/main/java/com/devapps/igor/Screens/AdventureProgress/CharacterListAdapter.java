package com.devapps.igor.Screens.AdventureProgress;

import android.app.Dialog;
import android.content.Context;
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
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.AdventureRequestManager;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.Edit.EditCharacterFragment;
import com.devapps.igor.Screens.Edit.EditSessionFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by hercules on 25/10/17.
 */

public class CharacterListAdapter extends RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder> {

    private final FragmentActivity mActivity;
    private ArrayList<Character> mCharacters;
    private ArrayList<Profile> mProfiles;
    private String mAdventureId;
    private String mUserId;
    private Character mDmCharacter;
    private boolean mEditMode = false;
    private Context mContext;

    public CharacterListAdapter(String adventureId, ArrayList<Character> characters,
                                ArrayList<Profile> profiles, Character dmCharacter, boolean editMode, FragmentActivity activity) {
        mAdventureId = adventureId;
        mActivity = activity;
        mCharacters = characters;
        mProfiles = profiles;
        mDmCharacter = dmCharacter;
        mEditMode = editMode;
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    @Override
    public CharacterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
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
                holder.mBtnEditDm.setTag(position);
                holder.mBtnEditDm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onEditDm(view);

                    }
                });
            } else {
                holder.mTextViewDMName.setText(R.string.no_master_assigned);
                holder.mTextViewDMSummary.setVisibility(View.INVISIBLE);
            }
        } else {
            position--;
            if (mEditMode) {
                if (mUserId.equals(mProfiles.get(position).getId()))
                    holder.mEditLayout.setVisibility(View.VISIBLE);
                else
                    holder.mEditLayout.setVisibility(View.GONE);
            } else {
                holder.mEditLayout.setVisibility(View.GONE);
            }

            holder.mTextViewCharacterName.setText(mCharacters.get(position).getName());
            holder.mTextViewPlayerName.setText(mProfiles.get(position).getName());
            holder.mBtnDelete.setTag(position);
            final int finalPosition = position;
            holder.mBtnEditCharacter.setTag(position);

            holder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    onDeleteCharacter(view, finalPosition);
                }
            });
            holder.mBtnEditCharacter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onEditCharacter(view);
                }
            });
        }


    }

    private void onEditDm(View view) {
        int pos = (int) view.getTag();
        Fragment fragment = EditCharacterFragment.newInstance(mAdventureId, pos, true);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    private void onEditCharacter(View view) {
        int position = (int) view.getTag();
        Fragment fragment = EditCharacterFragment.newInstance(mAdventureId, position, false);
        mActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    private void onDeleteCharacter(final View view, int finalPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Deseja deletar o personagem "
                + mCharacters.get(finalPosition).getName() + "?");

        builder.setPositiveButton("Sim", new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int pos = (int) view.getTag();
                mCharacters.remove(pos);
                mProfiles.remove(pos);
                AdventureRequestManager.saveCharacters(mAdventureId, mCharacters);
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

