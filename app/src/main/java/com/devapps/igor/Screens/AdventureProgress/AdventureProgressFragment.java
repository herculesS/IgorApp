package com.devapps.igor.Screens.AdventureProgress;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AdventureProgressFragment extends Fragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;

    public AdventureProgressFragment() {
        // Required empty public constructor
    }


    public static AdventureProgressFragment newInstance(String adventureId) {
        AdventureProgressFragment fragment = new AdventureProgressFragment();
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
            DatabaseReference ref = Database.getAdventuresReference();
            ref.child(mAdventureId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mAdventure = dataSnapshot.getValue(Adventure.class);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_details, container, false);
    }



}
