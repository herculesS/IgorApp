package com.devapps.igor.Screens.CreateCharacter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreateCharacterFragment extends Fragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";

    private String mAdventureId;
    private Adventure mAdventure;

    private EditText mEditTextCharacterName, mEditTextPlayerName, mEditTextSummary;
    private TextView mTextViewPlayerNameLabel;
    private RadioButton mRadioButtonPlayerName;
    private ImageView mButtonReady;
    private ImageView mButtonClose, mButtonCloseEdit;


    public CreateCharacterFragment() {
        // Required empty public constructor
    }

    public static CreateCharacterFragment newInstance(String adventureId) {
        CreateCharacterFragment fragment = new CreateCharacterFragment();
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
        return inflater.inflate(R.layout.fragment_create_character, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeMembers(view);
        setRadioButtonListener();
        mButtonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String playerName = mEditTextPlayerName.getText().toString().trim();
                final String characterName = mEditTextCharacterName.getText().toString().trim();
                final String characterSummary = mEditTextSummary.getText().toString().trim();

                if (characterName.length() == 0 || characterSummary.length() == 0) {
                    return;
                }
                if (mRadioButtonPlayerName.isChecked()) {
                    if (playerName.length() == 0) {
                        return;
                    }
                }

                DatabaseReference ref = Database.getAdventuresReference();
                ref.child(mAdventureId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mAdventure = dataSnapshot.getValue(Adventure.class);
                        if (mRadioButtonPlayerName.isChecked()) {
                            Log.d("RadioButton", "IsChecked");
                            DatabaseReference userRef = Database.getUsersReference();
                            userRef.orderByChild("name").equalTo(playerName).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        String id = null;
                                        Log.d("RadioButton", "Count " + dataSnapshot.getChildrenCount());
                                        if (dataSnapshot.getChildrenCount() > 1) {

                                            return;
                                        }
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            id = child.child("id").getValue(String.class);

                                            Log.d("RadioButton", "Value " + id);
                                        }

                                        Character character = new Character(id, characterName, characterSummary);
                                        mAdventure.addCharacter(character);
                                        DatabaseReference ref = Database.getAdventuresReference();
                                        ref.child(mAdventureId).setValue(mAdventure);
                                    } else {
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        } else {
                            Log.d("RadioButton", "IsNotChecked");
                            Character character = new Character(null, characterName, characterSummary);
                            mAdventure.addCharacter(character);
                            DatabaseReference ref = Database.getAdventuresReference();
                            ref.child(mAdventureId).setValue(mAdventure);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();
            }


        });
    }


    private void setRadioButtonListener() {
        mRadioButtonPlayerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((RadioButton) view).isChecked()) {
                    mTextViewPlayerNameLabel.setText(R.string.create_character_name_label);
                    mEditTextPlayerName.setText("");
                    mEditTextPlayerName.setVisibility(View.VISIBLE);
                } else {
                    mTextViewPlayerNameLabel.setText(R.string.create_character_no_player_name_selected);
                    mEditTextPlayerName.setText("");
                    mEditTextPlayerName.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initializeMembers(View view) {

        mTextViewPlayerNameLabel = (TextView) view.findViewById(R.id.text_view_player_name_label);
        mEditTextCharacterName = (EditText) view.findViewById(R.id.edit_text_name_character);
        mEditTextPlayerName = (EditText) view.findViewById(R.id.edit_text_name_player);
        mEditTextSummary = (EditText) view.findViewById(R.id.edit_text_summary_character);
        mRadioButtonPlayerName = (RadioButton) view.findViewById(R.id.radio_button_player_name);
        mButtonReady = (ImageView) view.findViewById(R.id.button_ready);
        mButtonClose = (ImageView) view.findViewById(R.id.create_character_btn_close);
        mButtonCloseEdit = (ImageView) view.findViewById(R.id.create_character_btn_create);
    }
}
