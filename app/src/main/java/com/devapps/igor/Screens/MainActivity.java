package com.devapps.igor.Screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devapps.igor.R;
import com.devapps.igor.Screens.CriarNovaAventura.CriarNovaAventura;

public class MainActivity extends AppCompatActivity {
    private Button mInicio;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInicio = (Button) findViewById(R.id.criarAventura);
        mTextView = (TextView) findViewById(R.id.textView3);

        mInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, CriarNovaAventura.class);
                startActivityForResult(i, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {
            mTextView.setText(data.getStringExtra("result"));
        }
    }

}
