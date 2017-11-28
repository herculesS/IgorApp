package com.devapps.igor.Screens.AdventureProgress;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.AdventureLoader;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AddPlayer.AddPlayerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayersFragment extends Fragment implements AdventureLoader.AdventureLoaderListener {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;
    private Profile mDM;

    private RecyclerView mRecyclerViewCharacter;
    private CharacterListAdapter mCharacterListAdapter;
    private TextView mTextViewDMName;
    private TextView mTextViewDMSummary;
    private Context mContext;
    private ImageView mImageViewMasterImage;
    private String mUserId;
    private AdventureLoader mAdventureLoader;


    public PlayersFragment() {
        // Required empty public constructor
    }

    public static PlayersFragment newInstance(String adventureId) {
        PlayersFragment fragment = new PlayersFragment();
        Bundle args = new Bundle();
        args.putString(ADVENTURE_ID, adventureId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdventureId = getArguments().getString(ADVENTURE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_players, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        initializeMembers(view);
        mAdventureLoader = new AdventureLoader();
        mAdventureLoader.setAdventureLoaderListener(this);
        mAdventureLoader.load(mAdventureId);

        mImageViewMasterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DMImageOnclick();
            }
        });

    }

    private void DMImageOnclick() {
        if (mAdventure != null) {
            if (mAdventure.getDMChar() != null) {
                if (mAdventure.getDMChar().getPlayerId().equals(mUserId)) {
                    mAdventure.setDMChar(null);
                    notifyChangeInAdventure(mAdventure);

                }
            } else {
                DatabaseReference ref = Database.getUsersReference();
                ref.child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Profile profile = dataSnapshot.getValue(Profile.class);
                        mAdventure.setDMChar(new Character(mUserId, profile.getName(), "TODO: Summary"));
                        notifyChangeInAdventure(mAdventure);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        }
    }

    private void notifyChangeInAdventure(Adventure adventure) {
        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).setValue(adventure);
    }

    @Override
    public void onAdventureLoaded(Adventure a) {
        mAdventure = a;
        DatabaseReference refUser = Database.getUsersReference();
        setDmViews(refUser);
        if (mAdventure.getCharacters() != null && mAdventure.getCharacters().size() != 0) {
            new LoadCharactersPlayerProfiles().execute();

        } else {
            mCharacterListAdapter = new CharacterListAdapter(new ArrayList<Character>(), new ArrayList<Profile>());
            mRecyclerViewCharacter.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerViewCharacter.setAdapter(mCharacterListAdapter);
        }

    }

    private void setDmViews(DatabaseReference refUser) {
        if (mAdventure.getDMChar() != null) {

            refUser.child(mAdventure.getDMChar().getPlayerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dmOnDataChange(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            mTextViewDMName.setText(R.string.no_master_assigned);
            mTextViewDMSummary.setVisibility(View.GONE);

        }
    }

    private void dmOnDataChange(DataSnapshot dataSnapshot) {
        mDM = dataSnapshot.getValue(Profile.class);
        mTextViewDMName.setText(mDM.getName());
        mTextViewDMSummary.setText(mAdventure.getDMChar().getSummary());
        mTextViewDMSummary.setVisibility(View.VISIBLE);
    }

    private void initializeMembers(View view) {
        mTextViewDMName = (TextView) view.findViewById(R.id.text_view_dm_name);
        mTextViewDMSummary = (TextView) view.findViewById(R.id.text_view_dm_summary);
        mRecyclerViewCharacter = (RecyclerView) view.findViewById(R.id.recycler_view_characters);
        mContext = view.getContext();
        mImageViewMasterImage = (ImageView) view.findViewById(R.id.image_view_set_master);
        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }


    private class LoadCharactersPlayerProfiles extends AsyncTask<Void, Void, Void> implements ValueEventListener {
        ArrayList<Profile> mPlayersList;
        ProgressDialog mDialog;


        public LoadCharactersPlayerProfiles() {
            mPlayersList = new ArrayList<>();
            mDialog = ProgressDialog.show(mContext, "", "Loading. Please wait...", true);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            DatabaseReference refUser = Database.getUsersReference();
            if (mAdventure.getCharacters() != null) {
                for (Character c : mAdventure.getCharacters()) {
                    if (c.getPlayerId() != null) {
                        refUser.child(c.getPlayerId()).addListenerForSingleValueEvent(this);
                    } else {
                        Profile p = new Profile();
                        p.setName("");
                        mPlayersList.add(p);
                    }

                }
            }
            return null;
        }


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            mPlayersList.add(dataSnapshot.getValue(Profile.class));
            if (mPlayersList.size() == mAdventure.getCharacters().size()) {
                endTask(mPlayersList);
                mDialog.dismiss();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private void endTask(ArrayList<Profile> playersList) {
        mCharacterListAdapter = new CharacterListAdapter(mAdventure.getCharacters(), playersList);
        mRecyclerViewCharacter.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewCharacter.setAdapter(mCharacterListAdapter);
    }

}
