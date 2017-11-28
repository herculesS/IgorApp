package com.devapps.igor.Screens.CreateAdventure;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.DataObject.Character;
import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.devapps.igor.Screens.MainActivity;
import com.google.firebase.database.DatabaseReference;


public class CreateAdventureFragment extends Fragment {
    private ImageView mReadyButton;
    private ImageView mCloseButton;
    private EditText mNameEditText;
    private ImageView mFinishButton;
    private FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_adventure, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initializeMembers(view);
        setListeners();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setListeners() {

        mReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString().trim();
                Adventure adventure;
                DatabaseReference ref = Database.getAdventuresReference();
                ref = ref.push();
                String key = ref.getKey();
                if (!name.equals("")) {
                    adventure = new Adventure(name, key);
                } else {
                    adventure = new Adventure(key);
                }
                MainActivity activity = (MainActivity) mActivity;
                Profile user = activity.getUserProfile();
                adventure.setDMChar(new Character(user.getId(), user.getName(), ""));
                ref.setValue(adventure);


                Fragment fragment = AdventureProgressFragment.newInstance(key);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit();

            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void initializeMembers(View view) {
        mReadyButton = (ImageView) view.findViewById(R.id.criar_adventure_botao_pronto);
        mCloseButton = (ImageView) view.findViewById(R.id.create_adventure_btn_close);
        mFinishButton = (ImageView) view.findViewById(R.id.create_adventure_btn_create);
        mNameEditText = (EditText) view.findViewById(R.id.create_adventure_editText);
        mNameEditText.requestFocus();
        if (getActivity() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mNameEditText, InputMethodManager.SHOW_IMPLICIT);
        }

    }
}
