package com.devapps.igor.Screens.AdventureProgress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AddPlayer.AddPlayerFragment;
import com.devapps.igor.Screens.BackableFragment;
import com.devapps.igor.Screens.CreateNewSession.CreateNewSessionFragment;
import com.devapps.igor.Screens.HomeJogosFrontend.FragmentAdventure;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import static android.content.Context.INPUT_METHOD_SERVICE;

public class AdventureProgressFragment extends Fragment implements BackableFragment{
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;

    private TextView mAdventureTitleTextView;
    private ImageView mBtnAdd;
    private Context mContext;
    private Button mBtnProgress;
    private Button mBtnPlayers;
    private ImageView mBgTab;
    private boolean mFirstTabSelected = true;
    private ImageView mBgImageView;
    private AddSessionListener mAddSessionListener;
    private AddPlayerListener mAddPlayerListener;

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
            Log.d("AdventureId", mAdventureId);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adventure_progress, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        InitializeMembers(view);

        if (getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }

        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAdventure = dataSnapshot.getValue(Adventure.class);
                mAdventureTitleTextView.setText(mAdventure.getName());
                switch (mAdventure.getBackground()) {
                    case 1:
                        mBgImageView.setImageResource(R.drawable.miniatura_imagem_automatica);
                        break;
                    case 2:
                        mBgImageView.setImageResource(R.drawable.miniatura_krevast);
                        break;
                    case 3:
                        mBgImageView.setImageResource(R.drawable.miniatura_coast);
                        break;
                    case 4:
                        mBgImageView.setImageResource(R.drawable.miniatura_corvali);
                        break;
                    case 5:
                        mBgImageView.setImageResource(R.drawable.miniatura_heartlands);
                        break;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mBtnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mFirstTabSelected) {
                    mBgTab.setImageResource(R.drawable.adventure_tab_first_selected);
                    mBtnAdd.setOnClickListener(mAddSessionListener);
                    mBtnAdd.setImageResource(R.drawable.btn_add_session);
                    Fragment fragment = DetailsFragment.newInstance(mAdventureId);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.adventure_players_container, fragment).commit();
                    mFirstTabSelected = true;

                }
            }
        });

        mBtnPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFirstTabSelected) {
                    mBgTab.setImageResource(R.drawable.adventure_tab_second_selected);
                    mBtnAdd.setOnClickListener(mAddPlayerListener);
                    mBtnAdd.setImageResource(R.drawable.btn_add_player);
                    Fragment fragment = PlayersFragment.newInstance(mAdventureId);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.adventure_players_container, fragment).commit();
                    mFirstTabSelected = false;
                }
            }
        });

        Fragment fragment = DetailsFragment.newInstance(mAdventureId);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.adventure_players_container, fragment).commit();

    }

    private void InitializeMembers(View view) {
        mAdventureTitleTextView = (TextView) view.findViewById(R.id.adventure_progress_adventure_title);
        mBtnAdd = (ImageView) view.findViewById(R.id.adventure_progress_btn_add_session);
        mBgImageView = (ImageView) view.findViewById(R.id.adventure_bg);
        mBtnProgress = (Button) view.findViewById(R.id.btn_progress);
        mBtnPlayers = (Button) view.findViewById(R.id.btn_players);
        mBgTab = (ImageView) view.findViewById(R.id.bg_tab_select);
        mAddPlayerListener = new AddPlayerListener();
        mAddSessionListener = new AddSessionListener();
        mBtnAdd.setOnClickListener(mAddSessionListener);
    }

    @Override
    public void back() {
        Fragment fragment = FragmentAdventure.newInstance(mAdventureId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    private class AddSessionListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Fragment fragment = CreateNewSessionFragment.newInstance(mAdventureId);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();

        }
    }

    private class AddPlayerListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            AddPlayerFragment fragment = AddPlayerFragment.newInstance(mAdventureId);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }
}
