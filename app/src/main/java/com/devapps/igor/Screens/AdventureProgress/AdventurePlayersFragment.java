package com.devapps.igor.Screens.AdventureProgress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdventurePlayersFragment extends Fragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;
    private Profile mDM;

    private RecyclerView mRecyclerViewCharacter;
    private TextView mTextViewDMName;
    private TextView mTextViewDMSummary;


    public AdventurePlayersFragment() {
        // Required empty public constructor
    }

    public static AdventurePlayersFragment newInstance(String adventureId) {
        AdventurePlayersFragment fragment = new AdventurePlayersFragment();
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

        mTextViewDMName = (TextView) view.findViewById(R.id.text_view_dm_name);
        mTextViewDMSummary = (TextView) view.findViewById(R.id.text_view_dm_summary);
        mRecyclerViewCharacter = (RecyclerView) view.findViewById(R.id.recycler_view_characters);

        DatabaseReference ref = Database.getAdventuresReference();

        ref.child(mAdventureId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAdventure = dataSnapshot.getValue(Adventure.class);
                DatabaseReference refUser = Database.getUsersReference();

                refUser.child(mAdventure.getDMid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDM = dataSnapshot.getValue(Profile.class);
                        mTextViewDMName.setText(mDM.getName());


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                final ArrayList<Profile> profiles = new ArrayList<Profile>();
                for (Character character : mAdventure.getCharacters()) {

                    refUser.child(character.getPlayerId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            profiles.add(dataSnapshot.getValue(Profile.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
