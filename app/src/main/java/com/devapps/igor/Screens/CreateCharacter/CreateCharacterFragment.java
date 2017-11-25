package com.devapps.igor.Screens.CreateCharacter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreateCharacterFragment extends Fragment {
    private static final String ADVENTURE_ID = "ADVENTURE_ID";
    private static final String PLAYER_ID = "PLAYER_ID";
    private static final String NOTIFICATION_POSITION = "NOTIFICATION_POSITION";

    private String mAdventureId;
    private String mPlayerId;
    private Adventure mAdventure;
    private int mNotificationPosition;
    private FragmentActivity mActivity;

    private EditText mEditTextCharacterName, mEditTextSummary;
    private ImageView mButtonReady;
    private ImageView mButtonClose, mButtonCloseEdit;


    public CreateCharacterFragment() {
        // Required empty public constructor
    }

    public static CreateCharacterFragment newInstance(String adventureId, String playerId, int position) {
        CreateCharacterFragment fragment = new CreateCharacterFragment();
        Bundle args = new Bundle();
        args.putString(ADVENTURE_ID, adventureId);
        args.putString(PLAYER_ID, playerId);
        args.putInt(NOTIFICATION_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAdventureId = getArguments().getString(ADVENTURE_ID);
            mPlayerId = getArguments().getString(PLAYER_ID);
            mNotificationPosition = getArguments().getInt(NOTIFICATION_POSITION);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();

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
        mButtonReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReadyOnClick();
                removeNotification();
                backToAdventureProgress();
            }
        });
    }

    private void removeNotification() {
        MainActivity activity = (MainActivity) mActivity;
        Profile user = activity.getUserProfile();
        user.removeNotification(mNotificationPosition);
        DatabaseReference ref = Database.getUsersReference();
        ref.child(user.getId()).setValue(user);
    }

    private void btnReadyOnClick() {
        final String characterName = mEditTextCharacterName.getText().toString().trim();
        final String characterSummary = mEditTextSummary.getText().toString().trim();

        if (characterName.length() == 0 || characterSummary.length() == 0) {
            createToast("Nome do personagem ou Resumo deve ser informado!");
            return;
        }

        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAdventure = dataSnapshot.getValue(Adventure.class);
                createCharacter(characterName, characterSummary);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createCharacter(String characterName, String characterSummary) {
        Character c = new Character(mPlayerId, characterName, characterSummary);
        mAdventure.addCharacter(c);
        DatabaseReference ref = Database.getAdventuresReference();
        ref.child(mAdventureId).setValue(mAdventure);

    }

    private void backToAdventureProgress() {
        Fragment fragment = AdventureProgressFragment.newInstance(mAdventureId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    private void createToast(String text) {
        if (getActivity() != null) {
            Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
        }
    }


    private void initializeMembers(View view) {
        mEditTextCharacterName = (EditText) view.findViewById(R.id.edit_text_name_character);
        mEditTextSummary = (EditText) view.findViewById(R.id.edit_text_summary_character);
        mButtonReady = (ImageView) view.findViewById(R.id.button_ready);
        mButtonClose = (ImageView) view.findViewById(R.id.create_character_btn_close);
        mButtonCloseEdit = (ImageView) view.findViewById(R.id.create_character_btn_create);
    }
}
