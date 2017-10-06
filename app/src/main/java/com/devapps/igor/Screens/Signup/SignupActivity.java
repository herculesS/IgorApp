package com.devapps.igor.Screens.Signup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.devapps.igor.DataObject.Profile;
import com.devapps.igor.R;
import com.devapps.igor.RequestManager.Database;
import com.devapps.igor.Util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    private Activity mActivity;

    private EditText _inputEmail;
    private EditText _inputPassword;
    private EditText _inputRetypePassword;
    private EditText _userName;
    private Utils.Sex userSex = Utils.Sex.MALE;
    private Button _btnSignUp;
    private TextView _birthDate;

    private int birthYear;
    private int birthDay;
    private int birthMonth;
    private Calendar today = Calendar.getInstance();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference userDatabaseReference;

    private Profile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mActivity = this;

        //Get Firebase auth instance
        userDatabaseReference = Database.getUsersReference();
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String name = _userName.getText().toString();
                    String email = _inputEmail.getText().toString().trim();
                    //TODO Birth date

                    userProfile = new Profile();
                    userProfile.setId(user.getUid());
                    userProfile.setName(name);
                    userProfile.setEmail(email);
                    userProfile.setSex(userSex);
                    userProfile.setBirthDay(birthDay);
                    userProfile.setBirthMonth(birthMonth);
                    userProfile.setBirthYear(birthYear);

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
            }
        };

        _btnSignUp = (Button) findViewById(R.id.sign_up_button);
        _inputEmail = (EditText) findViewById(R.id.email);
        _inputPassword = (EditText) findViewById(R.id.password);
        _inputRetypePassword = (EditText) findViewById(R.id.r_password);
        _userName = (EditText) findViewById(R.id.name);
        _birthDate = (TextView) findViewById(R.id.birthDate);

        _btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        birthDay = today.get(Calendar.DAY_OF_MONTH);
        birthMonth = today.get(Calendar.MONTH);
        birthYear = today.get(Calendar.YEAR);

        _birthDate.setText(birthDay + "\\" + (birthMonth + 1) + "\\" + birthYear);

        _birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        //Change date on TextView
                        setDate(selectedday, selectedmonth, selectedyear);
                        _birthDate.setText(selectedday + "\\" + (selectedmonth + 1) + "\\" + selectedyear);
                    }
                }, birthYear, birthMonth, birthDay);
                mDatePicker.setTitle("Início");
                mDatePicker.show();
            }
        });
    }

    private void setDate (int day, int month, int year) {
        birthDay = day;
        birthMonth = month;
        birthYear = year;
    }

    public void signup() {
        Log.d(TAG, "Signup");

        _btnSignUp.setEnabled(false);

        if (!validate()) {
            onSignupFailed();
            return;
        }

        final String email = _inputEmail.getText().toString().trim();
        final String password = _inputPassword.getText().toString().trim();

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
                        _btnSignUp.setEnabled(true);
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
                        _btnSignUp.setEnabled(true);
                    }
                }).show();
    }

    public boolean validate() {
        boolean valid = true;

        String name = _userName.getText().toString();
        String email = _inputEmail.getText().toString();
        String password = _inputPassword.getText().toString();
        String rPassword = _inputRetypePassword.getText().toString();
        //TODO birthdate

        if(name.isEmpty()){
            _userName.setError("Informe seu nome");
            valid = false;
        }else{
            _userName.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _inputEmail.setError("Email inválido");
            valid = false;
        } else {
            _inputEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 ) {
            _inputPassword.setError("Mínimo 6 carateres");
            valid = false;
        } else {
            _inputPassword.setError(null);
        }

        if (rPassword.isEmpty() || !password.equals(rPassword)) {
            _inputRetypePassword.setError("As senhas não são iguais");
            valid = false;
        } else {
            _inputRetypePassword.setError(null);
        }

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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if(checked) {
            switch(view.getId()) {
                case R.id.male_rbutton:
                    userSex = Utils.Sex.MALE;
                    break;
                case R.id.female_rbutton:
                    userSex = Utils.Sex.FEMALE;
                    break;
            }
        }
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