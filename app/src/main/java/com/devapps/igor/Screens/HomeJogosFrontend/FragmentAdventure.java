package com.devapps.igor.Screens.HomeJogosFrontend;

/*
 * Created by danielbarboni on 20/10/17.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.Screens.BackableFragment;
import com.devapps.igor.Screens.CreateAdventure.CreateAdventureFragment;

import com.devapps.igor.Screens.MainActivity;
import com.devapps.igor.Util.DataObjectUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.devapps.igor.R.layout.fragment_home_jogos_frontend;


/**
 * Created by danielbarboni on 06/10/17.
 */


public class FragmentAdventure extends Fragment implements BackableFragment {

//novo abaixo

    private static SeekBar seek_bar;
    private static TextView text_view;
    private static ImageView adventureWindow;
    private String mAdventureId;
    private Adventure mAdventure;
    private ImageView mBtnAddAdventure;
    private FragmentActivity mActivity;
    private static final String ADVENTURE_ID = "ADVENTURE_ID";
    public static ArrayList<Session> sessoes;
    public static int batata = 0;

    public FragmentAdventure() {
        // Required empty public constructor
    }

    public static FragmentAdventure newInstance(String adventureId) {
        FragmentAdventure fragment = new FragmentAdventure();
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
            Log.d("AdventureId", mAdventureId);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();

    }

    private TextView textViewTitle;
    private Context mContext;
    private ListView mSessionsListView;

    private FirebaseDatabase database;
    DatabaseReference databaseAdventures;
    List<Adventure> task;
    RecyclerView showadventuresRecyclerView;
    RecyclerView.Adapter showadventuresrecyclerviewAdapter;
    RecyclerView.LayoutManager showadventuresrecyclerviewLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //Inflate the layout for this fragment
        return inflater.inflate(fragment_home_jogos_frontend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        InitializeMembers(view); // novo
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
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mAdventure = dataSnapshot1.getValue(Adventure.class);
                    MainActivity activity = (MainActivity) mActivity;
                    Profile user = activity.getUserProfile();
                    if (DataObjectUtils.isPlayerInTheAdventure(mAdventure, user.getId())) {


                        Adventure adventure = new Adventure(mAdventure.getId());
                        String Titulo_Aventura = mAdventure.getName();
                        int Background = mAdventure.getBackground();
                        sessoes = mAdventure.getSessions();

                        String Proxima_Sessao = "Aventura sem sessao";
                        if (sessoes.size() != 0) {
                            Proxima_Sessao = sessoes.get(0).getTitle();
                        }

                        adventure.setName(Titulo_Aventura);
                        adventure.setSummary(Proxima_Sessao);
                        adventure.setBackground(Background);
                        adventure.setSessions(sessoes);

                        task.add(adventure);
                    }

                }

                // specifying an adapter
                showadventuresrecyclerviewAdapter = new RecyclerViewAdapter(getActivity(), task);
                showadventuresRecyclerView.setAdapter(showadventuresrecyclerviewAdapter);
                showadventuresRecyclerView.setItemAnimator(new DefaultItemAnimator());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });
        task = new ArrayList<>();

        mBtnAddAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CreateAdventureFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });

    }

    private void InitializeMembers(View view) {
        textViewTitle = (TextView) view.findViewById(R.id.home_jogos_textViewTitle);
        seek_bar = (SeekBar) view.findViewById(R.id.progressBar);
        text_view = (TextView) view.findViewById(R.id.textView);
        adventureWindow = (ImageView) view.findViewById(R.id.adventureWindow);
        mBtnAddAdventure = (ImageView) view.findViewById(R.id.home_jogos_btn_add_adventure);
        mContext = view.getContext();
    }

    @Override
    public void back() {
        mActivity.onBackPressed();
    }

}