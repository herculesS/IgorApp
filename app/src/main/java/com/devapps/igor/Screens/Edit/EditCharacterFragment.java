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
import com.devapps.igor.Screens.BackableFragment;

public class EditCharacterFragment extends Fragment implements AdventureRequestManager.AdventureLoaderListener,BackableFragment {

    private static final String ADVENTURE_ID = "ADVENTURE_ID";
    private static final String CHARACTER_ID = "CHARACTER_ID";
    private static final String IS_DM = "IS_DM";

    private String mAdventureId;
    private int mCharacterId;
    private boolean mIsDm;

    private EditText mCharacterNameEditText;
    private EditText mCharacterSummaryEditText;
    private ImageView mBtnReady;


    public EditCharacterFragment() {
        // Required empty public constructor
    }

    public static EditCharacterFragment newInstance(String adventureId, int characterId, boolean isDm) {
        EditCharacterFragment fragment = new EditCharacterFragment();
        Bundle args = new Bundle();
        args.putString(ADVENTURE_ID, adventureId);
        args.putInt(CHARACTER_ID, characterId);
        args.putBoolean(IS_DM, isDm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdventureId = getArguments().getString(ADVENTURE_ID);
            mCharacterId = getArguments().getInt(CHARACTER_ID);
            mIsDm = getArguments().getBoolean(IS_DM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_character, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeViews(view);
        AdventureRequestManager loader = new AdventureRequestManager();
        loader.setAdventureLoaderListener(this);
        loader.load(mAdventureId);
    }

    private void initializeViews(View view) {
        mCharacterNameEditText = (EditText) view.findViewById(R.id.edit_text_name);
        mCharacterSummaryEditText = (EditText) view.findViewById(R.id.edit_text_summary);
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
        if (mIsDm) {
            mCharacterNameEditText.setText(a.getDMChar().getName());
            mCharacterSummaryEditText.setText(a.getDMChar().getSummary());
            mBtnReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    a.getDMChar().setName(mCharacterNameEditText.getText().toString().trim());
                    a.getDMChar().setSummary(mCharacterSummaryEditText.getText().toString().trim());
                    AdventureRequestManager.saveDm(a.getId(), a.getDMChar());
                    Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();
                }
            });

        } else {
            mCharacterNameEditText.setText(a.getCharacters().get(mCharacterId).getName());
            mCharacterSummaryEditText.setText(a.getCharacters().get(mCharacterId).getSummary());
            mBtnReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    a.getCharacters().get(mCharacterId).setName(mCharacterNameEditText.getText().toString().trim());
                    a.getCharacters().get(mCharacterId).setSummary(mCharacterSummaryEditText.getText().toString().trim());
                    AdventureRequestManager.saveCharacters(a.getId(), a.getCharacters());

                    Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment).commit();
                }
            });
        }
    }

    @Override
    public void back() {
        Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }
}
