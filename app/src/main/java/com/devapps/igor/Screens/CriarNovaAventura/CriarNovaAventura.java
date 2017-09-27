package com.devapps.igor.Screens.CriarNovaAventura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.devapps.igor.R;

public class CriarNovaAventura extends AppCompatActivity {
    private ImageView mReadyButton;
    private ImageView mCloseButton;
    private EditText mNameEditText;
    private ImageView mFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_nova_aventura);

        initializeMenbers();
        setListeners();

    }

    private void setListeners() {

        mReadyButton.setOnClickListener(new View.OnClickListener() {
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
        });

    }

    private void initializeMenbers() {
        mReadyButton = (ImageView) findViewById(R.id.criar_aventura_botao_pronto);
        mCloseButton = (ImageView) findViewById(R.id.criar_aventura_botao_fechar);
        mFinishButton = (ImageView) findViewById(R.id.criar_aventura_botao_criar_aventura);
        mNameEditText = (EditText) findViewById(R.id.criar_aventura_editText);

    }
}
