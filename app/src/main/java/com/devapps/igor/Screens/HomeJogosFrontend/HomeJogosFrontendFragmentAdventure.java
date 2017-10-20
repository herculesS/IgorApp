package com.devapps.igor.Screens.HomeJogosFrontend;

/**
 * Created by danielbarboni on 20/10/17.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devapps.igor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by danielbarboni on 06/10/17.
 */


public class HomeJogosFrontendFragmentAdventure extends Fragment {

   // private static final String URL_DATA = "https://simplifiedcoding.net/demos/marvel/";

    RecyclerView recyclerViewAdventures;
   // private RecyclerView recyclerViewAdventures;
    //private RecyclerView.Adapter adapter;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseAdventures; // codigo novo - excluir

    List<HomeJogosFrontEndListAdventure> adventureList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_jogos_frontend, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        databaseAdventures = FirebaseDatabase.getInstance().getReference("adventures"); // codigo novo - excluir

        recyclerViewAdventures = (RecyclerView) getActivity().findViewById(R.id.recyclerViewAdventures);
        recyclerViewAdventures.setHasFixedSize(true);
        recyclerViewAdventures.setLayoutManager(new LinearLayoutManager(getActivity()));

        adventureList = new ArrayList<>();

        //loadRecyclerViewData();  //--descomentar


        //DatabaseReference ref = Database.getAdventuresReference(); // comentar a partir daqui
    }

    @Override
    public void onStart() {
        super.onStart();

        databaseAdventures.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adventureList.clear();

                for (DataSnapshot adventureSnapshot : dataSnapshot.getChildren()) {
                   // HomeJogosFrontEndListAdventure adventure = adventureSnapshot.getValue(HomeJogosFrontEndListAdventure.class);

                    adventureList.add(adventureSnapshot.getValue(HomeJogosFrontEndListAdventure.class));
//                    adventureList.add(adventure);
                }


                HomeJogosFrontendAdapterAdventure adapter = new HomeJogosFrontendAdapterAdventure(getActivity(),adventureList);



                recyclerViewAdventures.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}