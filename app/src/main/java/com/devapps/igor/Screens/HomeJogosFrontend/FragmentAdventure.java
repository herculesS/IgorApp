package com.devapps.igor.Screens.HomeJogosFrontend;

/**
 * Created by danielbarboni on 20/10/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.EditSummary.EditSummaryFragment;
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


public class FragmentAdventure extends Fragment {


    FirebaseDatabase database; //novo
    DatabaseReference databaseAdventures;
    List<AdventureList> task;
    RecyclerView showadventuresRecyclerView;
    RecyclerView.Adapter showadventuresrecyclerviewAdapter;
    RecyclerView.LayoutManager showadventuresrecyclerviewLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_jogos_frontend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showadventuresRecyclerView = (RecyclerView) getActivity().findViewById(R.id.showadventuresRecyclerView);
        // used to improve performance, once changes
        // in content do not change the layout size of the RecyclerView
        showadventuresRecyclerView.setHasFixedSize(true);
        // using a linear layout manager
        showadventuresrecyclerviewLayoutManager = new LinearLayoutManager(getActivity());
        showadventuresRecyclerView.setLayoutManager(showadventuresrecyclerviewLayoutManager);

        database = FirebaseDatabase.getInstance();
        databaseAdventures = database.getReference("adventures");

        databaseAdventures.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                task.clear();
                task = new ArrayList<AdventureList>();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                    AdventureList value = dataSnapshot1.getValue(AdventureList.class);
                    AdventureList adventure = new AdventureList();
                    String Titulo_Aventura = value.getTitle();
                    String Proxima_Sessao = value.getNextsession();
                    String Barra_Progresso = value.getProgressbar();
                    adventure.setTitle(Titulo_Aventura);
                    adventure.setNextsession(Proxima_Sessao);
                    adventure.setProgressbar(Barra_Progresso);
                    task.add(adventure);

                }
                // specifying an adapter
                showadventuresrecyclerviewAdapter = new RecyclerViewAdapter(getActivity(),task);
                showadventuresRecyclerView.setAdapter(showadventuresrecyclerviewAdapter);
                showadventuresRecyclerView.setItemAnimator( new DefaultItemAnimator());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });
        task = new ArrayList<>();
    }

}