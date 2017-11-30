package com.devapps.igor.Screens.Edit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.AdventureRequestManager;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;

public class EditAdventureFragment extends Fragment implements AdventureRequestManager.AdventureLoaderListener {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;

    private EditText mAdventureTitleEditText;
    private EditText mAdventureSummaryEditText;
    private ImageView mBtnReady;


    public EditAdventureFragment() {
        // Required empty public constructor
    }

    public static EditAdventureFragment newInstance(String adventureId) {
        EditAdventureFragment fragment = new EditAdventureFragment();
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
        return inflater.inflate(R.layout.fragment_edit_adventure, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeViews(view);
        AdventureRequestManager loader = new AdventureRequestManager();
        loader.setAdventureLoaderListener(this);
        loader.load(mAdventureId);
    }

    private void initializeViews(View view) {
        mAdventureTitleEditText = (EditText) view.findViewById(R.id.edit_text_name);
        mAdventureSummaryEditText = (EditText) view.findViewById(R.id.edit_text_summary);
        mBtnReady = (ImageView) view.findViewById(R.id.button_ready);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAdventureLoaded(final Adventure a) {
        mAdventureTitleEditText.setText(a.getName());
        mAdventureSummaryEditText.setText(a.getSummary());
        mBtnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.setName(mAdventureTitleEditText.getText().toString().trim());
                a.setSummary(mAdventureSummaryEditText.getText().toString().trim());
                AdventureRequestManager.saveAdventure(a);

                Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }
        });
    }
}
