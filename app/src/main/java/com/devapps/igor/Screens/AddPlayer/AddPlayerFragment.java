package com.devapps.igor.Screens.AddPlayer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.AdventureLoader;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.BackableFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AddPlayerFragment extends Fragment implements BackableFragment, AdventureLoader.AdventureLoaderListener {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    ImageView mBtn_close, mBtn_close_edit;
    Button mBtn_search;
    EditText mEditTextPlayerName;
    RecyclerView mSearchedPlayerList;
    Context mContext;
    SearchedPlayersListAdapter mListAdapter;
    private String mAdventureId;
    private FragmentActivity mActivity;
    private Adventure mAdventure;
    private AdventureLoader mAdventureLoader;


    public AddPlayerFragment() {
        // Required empty public constructor
    }

    public static AddPlayerFragment newInstance(String adventureId) {

        AddPlayerFragment fragment = new AddPlayerFragment();
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
        return inflater.inflate(R.layout.fragment_add_player, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //initializeMenbers
        initializeMembers(view);

        //load Adventure
        mAdventureLoader = new AdventureLoader();
        mAdventureLoader.setAdventureLoaderListener(this);
        mAdventureLoader.load(mAdventureId);


        //setClickListeners
        setClickListeners();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();

    }

    private void setClickListeners() {
        mBtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Teste",""+mAdventureLoader.isFinished()+mAdventureId);
                if (mAdventureLoader.isFinished()) {
                    new PlayerSearcher(mEditTextPlayerName.getText()
                            .toString().trim());
                }
            }
        });
    }

    private void initializeMembers(View view) {
        mBtn_close = (ImageView) view.findViewById(R.id.add_player_btn_close);
        mBtn_close_edit = (ImageView) view.findViewById(R.id.add_player_btn_close_edit);
        mBtn_search = (Button) view.findViewById(R.id.button_search);
        mEditTextPlayerName = (EditText) view.findViewById(R.id.edit_text_name_player);
        mSearchedPlayerList = (RecyclerView) view.findViewById(R.id.recycler_view_players);
        mListAdapter = new SearchedPlayersListAdapter(new ArrayList<Profile>(), mAdventureId, mActivity, mAdventure);
        mSearchedPlayerList.setLayoutManager(new LinearLayoutManager(mContext));
        mSearchedPlayerList.setAdapter(mListAdapter);
    }

    @Override
    public void back() {
        Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onAdventureLoaded(Adventure a) {
        mAdventure = a;
        mListAdapter.setAdventure(mAdventure);

    }


    private class PlayerSearcher implements ValueEventListener {
        DatabaseReference mUserRef;
        String mNameToSearch;

        PlayerSearcher(String nameToSearch) {
            mNameToSearch = nameToSearch;
            mUserRef = Database.getUsersReference();
            mUserRef.orderByChild("name").startAt(mNameToSearch).endAt(mNameToSearch + "\uf8ff")
                    .limitToFirst(10).addListenerForSingleValueEvent(this);
        }

        void endTask(ArrayList<Profile> p) {
            mListAdapter.changeData(p);
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            new AddPlayerTask(dataSnapshot).execute();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        private class AddPlayerTask extends AsyncTask<Void, Void, Void> {
            DataSnapshot mPlayersSnapshot;
            ArrayList<Profile> mPlayersList;
            ProgressDialog mDialog;


            public AddPlayerTask(DataSnapshot playersSnapshot) {
                mPlayersSnapshot = playersSnapshot;
                mPlayersList = new ArrayList<Profile>();
                mDialog = ProgressDialog.show(mContext, "", "Loading. Please wait...", true);
            }


            @Override
            protected Void doInBackground(Void... voids) {
                for (DataSnapshot child : mPlayersSnapshot.getChildren()) {
                    Profile pf = child.getValue(Profile.class);
                    mPlayersList.add(pf);

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                endTask(mPlayersList);
                mDialog.dismiss();
            }
        }
    }
}





