package com.devapps.igor.Screens.AdventureProgress;

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
import com.devapps.igor.RequestManager.AdventureLoader;
import com.devapps.igor.Screens.AddPlayer.AddPlayerFragment;
import com.devapps.igor.Screens.BackableFragment;
import com.devapps.igor.Screens.CreateNewSession.CreateNewSessionFragment;
import com.devapps.igor.Screens.HomeJogosFrontend.FragmentAdventure;
import com.devapps.igor.Util.DataObjectUtils;
import com.google.firebase.auth.FirebaseAuth;


import static android.content.Context.INPUT_METHOD_SERVICE;

public class AdventureProgressFragment extends Fragment implements BackableFragment, AdventureLoader.AdventureLoaderListener {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;

    private TextView mAdventureTitleTextView;
    private ImageView mBtnAdd;
    private Button mBtnProgress;
    private Button mBtnPlayers;
    private ImageView mBtnEdit;
    private ImageView mBgTab;
    private boolean mFirstTabSelected = true;
    private boolean mEditMode = false;
    private String mUserId;
    private ImageView mBgImageView;
    private AddSessionListener mAddSessionListener;
    private AddPlayerListener mAddPlayerListener;
    private ImageView mBtnAdventureEdit;

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

        mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AdventureLoader loader = new AdventureLoader();
        loader.setAdventureLoaderListener(this);
        loader.load(mAdventureId);

        setClickListeners();

        Fragment fragment = DetailsFragment.newInstance(mAdventureId);
        Editable f = (Editable) fragment;
        f.editMode(mEditMode);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.adventure_players_container, fragment).commit();

    }

    private void setClickListeners() {
        mBtnAdventureEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditMode) {
                    mEditMode = false;

                } else {
                    mEditMode = true;

                }


                Fragment f = getChildFragmentManager().
                        findFragmentById(R.id.adventure_players_container);
                if (mAdventure.getDMChar() != null && mUserId.
                        equals(mAdventure.getDMChar().getPlayerId())) {
                    mBtnAdventureEdit.setVisibility(View.VISIBLE);
                } else {
                    mBtnAdventureEdit.setVisibility(View.GONE);
                }
                Editable editable = (Editable) f;
                editable.editMode(mEditMode);
            }
        });
        mBtnProgress.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (!mFirstTabSelected) {
                    if (mAdventure.getDMChar() != null) {
                        if (mAdventure.getDMChar().getPlayerId().equals(mUserId)) {
                            mBtnEdit.setVisibility(View.VISIBLE);
                        } else {
                            mBtnEdit.setVisibility(View.GONE);
                            mBtnAdventureEdit.setVisibility(View.GONE);
                            mEditMode = false;

                        }
                    } else {
                        mBtnEdit.setVisibility(View.GONE);
                        mBtnAdventureEdit.setVisibility(View.GONE);
                        mEditMode = false;

                    }
                    mBgTab.setImageResource(R.drawable.adventure_tab_first_selected);
                    mBtnAdd.setOnClickListener(mAddSessionListener);
                    mBtnAdd.setImageResource(R.drawable.btn_add_session);
                    Fragment fragment = DetailsFragment.newInstance(mAdventureId);
                    Editable f = (Editable) fragment;
                    f.editMode(mEditMode);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.adventure_players_container, fragment).commit();
                    mFirstTabSelected = true;

                }
            }
        });

        mBtnPlayers.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (mFirstTabSelected) {
                    mBgTab.setImageResource(R.drawable.adventure_tab_second_selected);
                    mBtnAdd.setOnClickListener(mAddPlayerListener);
                    mBtnAdd.setImageResource(R.drawable.btn_add_player);
                    mBtnEdit.setVisibility(View.VISIBLE);
                    Fragment fragment = PlayersFragment.newInstance(mAdventureId);
                    Editable f = (Editable) fragment;
                    f.editMode(mEditMode);
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.adventure_players_container, fragment).commit();
                    mFirstTabSelected = false;
                }
            }
        });
    }

    private void InitializeMembers(View view) {
        mAdventureTitleTextView = (TextView) view.findViewById(R.id.adventure_progress_adventure_title);
        mBtnAdd = (ImageView) view.findViewById(R.id.adventure_progress_btn_add_session);
        mBtnEdit = (ImageView) view.findViewById(R.id.adventure_progress_btn_edit);
        mBtnAdventureEdit = (ImageView) view.findViewById(R.id.adventure_edit);
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

    @Override
    public void onAdventureLoaded(Adventure a) {
        mAdventure = a;
        mAdventureTitleTextView.setText(mAdventure.getName());
        if (mAdventure.getDMChar() != null) {
            if (mAdventure.getDMChar().getPlayerId().equals(mUserId)) {
                mBtnEdit.setVisibility(View.VISIBLE);
            }
        }
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
