package com.devapps.igor.Screens.HomeJogosFrontend;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.EditSummary.EditSummaryFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielbarboni on 06/10/17.
 */

//public class HomeJogosFrontendFragment extends AppCompatActivity {
public class HomeJogosFrontendFragment extends Fragment {

    private static final String URL_DATA = "https://simplifiedcoding.net/demos/marvel/";

    private RecyclerView recyclerViewAdventures;
    private RecyclerView.Adapter adapter;


    private List<HomeJogosFrontendListItem> listItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_jogos_frontend, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewAdventures = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerViewAdventures.setHasFixedSize(true);
        recyclerViewAdventures.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();

        loadRecyclerViewData();  //--descomentar


       /* DatabaseReference ref = Database.getAdventuresReference();
        ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Adventure listItems = child.getValue(Adventure.class);
                }

                mAdventureTitleTextView.setText(mAdventure.getName());
                mAdventureSummaryTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fragment = EditSummaryFragment.newInstance(Adventure.ADVENTURE_TAG, mAdventureId, 0);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, fragment).commit();
                    }
                });
                mAdventureSummaryTextView.setText(mAdventure.getSummary());
                mAdventureSummaryTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdventureSummaryTextView.getLineCount() > 6) {
                            mAdventureSummaryTextView.setMaxLines(6);
                            mBtnSeeMore.setVisibility(View.VISIBLE);
                        }

                    }
                });

                mSessionsListView.setAdapter(new AdventureProgressFragment.SessionsAdapter(mContext, mAdventure.getSessions()));
                mBtnSeeMore.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       if (mAdventureSummaryTextView.getMaxLines() > 6) {
                                                           mAdventureSummaryTextView.setMaxLines(6);
                                                           mAdventureSummaryTextView.getMaxLines();
                                                       } else {
                                                           mAdventureSummaryTextView.setMaxLines(mAdventureSummaryTextView.getLineCount());
                                                           mAdventureSummaryTextView.getMaxLines();
                                                       }

                                                   });

*/
      /*  for (int i = 0; i<=10; i++) {
            ListItem listItem = new ListItem(
                    "heading " + (i + 1),
                    "Lorem ipsum dummy text"
            );


            listItems.add(listItem);

        }

        adapter = new MyAdapter(listItems, getActivity());

        recyclerView.setAdapter(adapter);
*/
    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s){
                        progressDialog.dismiss();
                        try {


                            // JSONObject jsonObject = new JSONObject(s);
                            //JSONArray array = jsonObject.getJSONArray("heroes");

                            JSONArray array = new JSONArray(s);
                            for(int i = 0; i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                HomeJogosFrontendListItem item = new HomeJogosFrontendListItem(
                                        //o.getString("name"),
                                        //o.getString("about"),
                                        //o.getString("image")
                                        o.getString("name"),
                                        o.getString("realname"),
                                        o.getString("team"),
                                        o.getString("firstappearance"),
                                        o.getString("createdby"),
                                        o.getString("publisher"),
                                        o.getString("imageurl"),
                                        o.getString("bio")
                                );
                                listItems.add(item);
                            }


                            adapter = new HomeJogosFrontendMyAdapter(listItems, getActivity().getApplicationContext());
                            recyclerViewAdventures.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError){
                        progressDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        queue.add(stringRequest);
        //  requestQueue.add(stringRequest);



    }

}

