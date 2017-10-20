package com.devapps.igor.Screens.Books;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.devapps.igor.R;
import com.devapps.igor.Screens.HomeJogosFrontend.HomeJogosFrontendListItem;
import com.devapps.igor.Screens.HomeJogosFrontend.HomeJogosFrontendMyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danielbarboni on 07/10/17.
 */

public class BooksFragment extends Fragment {
    private static final String URL_DATA = "https://simplifiedcoding.net/demos/marvel/";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<BooksListItem> listItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_books, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItems = new ArrayList<>();

        loadRecyclerViewData();
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
                                BooksListItem item = new BooksListItem(
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


                            adapter = new BooksMyAdapter(listItems, getActivity().getApplicationContext());
                            recyclerView.setAdapter(adapter);
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
    }
}
