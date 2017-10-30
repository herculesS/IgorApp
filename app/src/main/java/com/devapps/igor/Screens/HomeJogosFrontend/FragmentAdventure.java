package com.devapps.igor.Screens.HomeJogosFrontend;

/**
 * Created by danielbarboni on 20/10/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Session;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.HomeJogosFrontend.FragmentAdventure;
import com.devapps.igor.Screens.CreateNewSession.CreateNewSessionFragment;
import com.devapps.igor.Screens.EditSummary.EditSummaryFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.devapps.igor.R.id.showadventuresRecyclerView;
import static com.devapps.igor.R.id.textViewPublisher;


/**
 * Created by danielbarboni on 06/10/17.
 */


public class FragmentAdventure extends Fragment {

/*
    private FirebaseDatabase database; //novo
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
*/


    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;

    private TextView textViewTitle;

    private ListView mSessionsListView;
    private ImageView mBtnAddAdventure;
    private Context mContext;
    FirebaseDatabase database; //apagar
    DatabaseReference databaseAdventures; //apagar
    private RecyclerView mshowadventuresRecyclerView;

    List<AdventureList> task; //apagar
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
            Log.d("Adventure_Id", mAdventureId);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_jogos_frontend, container, false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        //Recycler View
        mshowadventuresRecyclerView = (RecyclerView) getActivity().findViewById(R.id.showadventuresRecyclerView);
        mshowadventuresRecyclerView.setHasFixedSize(true);
        mshowadventuresRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Send a Query to the database
        database = FirebaseDatabase.getInstance();
        databaseAdventures = database.getReference("adventures");

        FirebaseRecyclerAdapter<Adventure, AdventureListViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Adventure, AdventureListViewHolder>(
                        Adventure.class,
                        R.layout.fragment_home_jogos_frontend_list_adventures,
                        AdventureListViewHolder.class,
                        databaseAdventures) {

                    @Override
                    protected void populateViewHolder(AdventureListViewHolder viewHolder, Adventure adventure, int position) { //alterar aqui para AdventureList e os items abaixo também
                        viewHolder.setName(adventure.getName());
                        viewHolder.setSummary(adventure.getSummary()); //comentar
                       // viewHolder.setSessions(adventure.getSessions()); //comentar
                    }
                };
        mshowadventuresRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //View Holder for Recycler View
    public static class AdventureListViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public AdventureListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.androidsquad.space/"));
                    Intent browserChooserIntent = Intent.createChooser(browserIntent, "Choose browser of yout choice");
                    v.getContext().startActivity(browserChooserIntent);
                }
            });
        }

        public void setName(String name) {
            TextView post_Name = (TextView) mView.findViewById(R.id.home_jogos_textViewTitle);
            post_Name.setText(name);
        }

        public void setSummary(String summary) {
            TextView post_Summary = (TextView) mView.findViewById(R.id.textViewNextSession);
            post_Summary.setText(summary);
        }

    /*    public void setSessions(ArrayList<Session> sessions) {
            ListView post_Sessions = (ListView) mView.findViewById(R.id.home_jogos_sessions_list);
           // post_Sessions.setAdapter(sessions);
        }*/
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        InitializeMembers(view);

        DatabaseReference ref = Database.getAdventuresReference();
        //ref.addValueEventListener(new ValueEventListener() {
        ref.child("-Kvm7648leSxb2hTdtxX").addValueEventListener(new ValueEventListener() {

            //database = FirebaseDatabase.getInstance();
            //databaseAdventures = database.getReference("adventures");
            //databaseAdventures.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //task.clear();
                //task = new ArrayList<AdventureList>();
            //    for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                //mAdventure = dataSnapshot.getValue(Adventure.class);
                //mAdventure = dataSnapshot1.getValue(Adventure.class);
               // textViewTitle.setText(mAdventure.getName());
               // mSessionsListView.setAdapter(new FragmentAdventure.SessionsAdapter(mContext, mAdventure.getSessions()));

           //     }
                //mSessionsListView.setAdapter(new FragmentAdventure.SessionsAdapter(mContext, mAdventure.getSessions()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* mBtnAddAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = CreateNewSessionFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });*/

    }


    private void InitializeMembers(View view) {
        textViewTitle = (TextView) view.findViewById(R.id.home_jogos_textViewTitle);

        //mSessionsListView = (ListView) view.findViewById(R.id.home_jogos_sessions_list);
      //  mBtnAddAdventure = (ImageView) view.findViewById(R.id.home_jogos_btn_add_adventure); //ok
        mContext = view.getContext();
        //mSessionsListView.setItemsCanFocus(true);
    }


    /* Adapter class implementation for the items of the listView that contains the sessions */
    private class SessionsAdapter extends ArrayAdapter<Session> {
        public SessionsAdapter(Context context, ArrayList<Session> sessions) {
            super(context, 0, sessions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Session s = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.sessions_list_view_elem, parent, false);
            }


            TextView sessionDateTextView = (TextView) convertView.findViewById(R.id.session_list_view_item_date);
            TextView sessionTitleTextView = (TextView) convertView.findViewById(R.id.session_list_view_item_title);
            TextView sessionSummaryTextView = (TextView) convertView.findViewById(R.id.session_list_view_item_summary);


            sessionDateTextView.setText(Session.formatSessionDateToDayMonth(s.getDate()));
            sessionTitleTextView.setText(s.getTitle());
            sessionSummaryTextView.setText(s.getSummary());
            sessionSummaryTextView.setTag(position);
            sessionSummaryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int sessionId = (Integer) view.getTag();
                    Fragment fragment = EditSummaryFragment.newInstance(Session.SESSION_TAG, mAdventureId, sessionId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();
                }
            });


            sessionTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View parent = (View) view.getParent();
                    if (parent != null) {
                        View root = (View) view.getParent().getParent();
                        if (root != null) {
                            setSummarySeeMoreBehavior(root);
                        }
                    }
                }
            });

            return convertView;
        }

        private void setSummarySeeMoreBehavior(View root) {
            TextView sessionSummaryTextView = (TextView) root.findViewById(R.id.session_list_view_item_summary);
            if (sessionSummaryTextView != null) {

                if (!sessionSummaryTextView.isShown()) {

                    sessionSummaryTextView.setVisibility(View.VISIBLE);
                } else {
                    sessionSummaryTextView.setVisibility(View.GONE);
                }
            }
        }

    }

}