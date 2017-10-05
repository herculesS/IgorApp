package com.devapps.igor.Screens.CreateNewAdventure;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.DataObject.Adventure;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Screens.AdventureProgress.AdventureProgressFragment;
import com.google.firebase.database.DatabaseReference;


public class CreateNewAdventureFragment extends Fragment {
    private ImageView mReadyButton;
    private ImageView mCloseButton;
    private EditText mNameEditText;
    private ImageView mFinishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_adventure, container, false);
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
                if (!name.equals("")) {
                    adventure = new Adventure(name);
                } else {
                    adventure = new Adventure();
                }
                DatabaseReference ref = Database.getAdventuresReference();
                ref = ref.push();
                ref.setValue(adventure);
                String key = ref.getKey();

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

    }
}
