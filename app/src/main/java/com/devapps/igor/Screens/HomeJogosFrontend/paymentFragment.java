package com.devapps.igor.Screens.HomeJogosFrontend;

import android.app.ProgressDialog;
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

import static com.devapps.igor.R.id.recyclerViewAdventures;

/**
 * Created by danielbarboni on 20/10/17.
 */

public class paymentFragment extends Fragment {

    RecyclerView re;
    View v;
    ProgressDialog progress;

    private List<HomeJogosFrontEndListAdventure> adventureList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_jogos_frontend,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v=view;
        init();

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Syncing");
        progress.setCancelable(false);
        progress.show();

        loaddata();

    }

    private void loaddata(){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("adventures");
        db.addListenerForSingleValueEvent(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adventureList.clear();
                for (DataSnapshot single:dataSnapshot.getChildren()) {

                    adventureList.add(single.getValue(HomeJogosFrontEndListAdventure.class));
                }
                progress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        re=(RecyclerView)v.findViewById(recyclerViewAdventures);
        re.setLayoutManager(new LinearLayoutManager(getContext()));
       // HomeJogosFrontendAdapterAdventure adapter = new HomeJogosFrontendAdapterAdventure(getActivity(),adventureList);
        //recyclerViewAdventures.setAdapter(adapter);
    }
}
