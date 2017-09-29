package com.devapps.igor.Screens.CriarNovaAventura;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CriarNovaAventuraFragment extends Fragment {
    private ImageView mReadyButton;
    private ImageView mCloseButton;
    private EditText mNameEditText;
    private ImageView mFinishButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criar_nova_aventura, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeMembers();
        setListeners();
    }

    private void setListeners() {

        /*mReadyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameEditText.getText().toString();
                Intent i = new Intent();
                i.putExtra("result", name);
                setResult(RESULT_OK, i);
                finish();
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });*/

    }

    private void initializeMembers() {
        mReadyButton = (ImageView) getActivity().findViewById(R.id.criar_aventura_botao_pronto);
        mCloseButton = (ImageView) getActivity().findViewById(R.id.criar_aventura_botao_fechar);
        mFinishButton = (ImageView) getActivity().findViewById(R.id.criar_aventura_botao_criar_aventura);
        mNameEditText = (EditText) getActivity().findViewById(R.id.criar_aventura_editText);

    }
}
