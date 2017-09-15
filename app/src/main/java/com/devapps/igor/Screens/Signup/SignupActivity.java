package com.devapps.igor.Screens.Signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.Screens.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private EditText inputEmail, inputPassword;
    private Button btnSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference userDatabaseReference;

    private Profile userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //String name = _name.getText().toString();
                    String email = inputEmail.getText().toString().trim();
                    //TODO Name, Birth date, sex

                    userProfile = new Profile();
                    userProfile.setId(user.getUid());
                    //userProfile.setName(name);
                    userProfile.setEmail(email);
                    //TODO Name, Birth date, sex

                    userDatabaseReference.child(user.getUid()).setValue(userProfile)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendVerificationEmail(user);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            user.delete();
                            onSignupFailed();
                        }
                    });

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        btnSignUp.setEnabled(false);

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final String email = inputEmail.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            onSignupFailed();
                        }
                        // ...
                    }
                });
    }

    public void onSignupSuccess() {
        new SweetAlertDialog(this,SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Conta criada com sucesso!")
                .setContentText("Por Favor, verifique seu email para a validação da conta.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        btnSignUp.setEnabled(true);
                        setResult(RESULT_OK, null);
                        finish();
                    }
                }).show();
    }

    public void onSignupFailed() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Falha ao criar conta.")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        btnSignUp.setEnabled(true);
                    }
                }).show();
    }

    public boolean validate() {
        boolean valid = true;

        //String name = _name.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();
        //String rPassword = _rPasswordText.getText().toString();

      /*  if(name.isEmpty()){
            _name.setError("campo vazio");
            valid = false;
        }else{
            _name.setError(null);
        }*/

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("endereço email inválido");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            inputPassword.setError("senha tem que ter mais do que 6 carateres");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        /*if(rPassword.isEmpty() || !rPassword.equals(password)){
            _rPasswordText.setError("senhas incompatíveis");
            valid = false;
        }else{
            _rPasswordText.setError(null);
        }*/

        return valid;
    }

    private void sendVerificationEmail(final FirebaseUser user) {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // email sent
                    // after email is sent just logout the user and finish this activity
                    FirebaseAuth.getInstance().signOut();
                    onSignupSuccess();
                } else {
                    // email not sent, so display message and restart the activity or do whatever you wish to do

                    //restart this activity
                    overridePendingTransition(0, 0);
                    onSignupFailed();

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}