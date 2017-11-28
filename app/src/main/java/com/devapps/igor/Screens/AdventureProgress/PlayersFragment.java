package com.devapps.igor.Screens.AdventureProgress;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private RecyclerView mRecyclerViewCharacter;
    private CharacterListAdapter mCharacterListAdapter;
    private Context mContext;
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
    }


    @Override
    public void onAdventureLoaded(Adventure a) {
        mAdventure = a;
        if (mAdventure.getCharacters() != null && mAdventure.getCharacters().size() != 0) {
            new LoadCharactersPlayerProfiles().execute();

        } else {
            mCharacterListAdapter = new CharacterListAdapter(new ArrayList<Character>(), new ArrayList<Profile>(), mAdventure.getDMChar());
            mRecyclerViewCharacter.setLayoutManager(new LinearLayoutManager(mContext));
            mRecyclerViewCharacter.setAdapter(mCharacterListAdapter);
        }

    }


    private void initializeMembers(View view) {
        mRecyclerViewCharacter = (RecyclerView) view.findViewById(R.id.recycler_view_characters);
        mContext = view.getContext();
    }

    private void endTask(ArrayList<Profile> playersList) {
        mCharacterListAdapter = new CharacterListAdapter(mAdventure.getCharacters(), playersList, mAdventure.getDMChar());
        mRecyclerViewCharacter.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewCharacter.setAdapter(mCharacterListAdapter);
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


}
