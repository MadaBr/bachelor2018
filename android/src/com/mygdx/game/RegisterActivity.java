package com.mygdx.game;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity {

    EditText username,email,password;
    Button signUp;
    Spinner nativeLanguages, studyingLanguages;

    String emailData, passwordData, usernameData, nativeLanguage, studyingLanguage;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        username = (EditText)findViewById(R.id.registerUsernameET);
        email = (EditText)findViewById(R.id.registerEmailET);
        password = (EditText)findViewById(R.id.registerPasswordET);
        signUp = (Button)findViewById(R.id.registerButton);
        nativeLanguages = (Spinner)findViewById(R.id.nativeLanguageSpinner);
        studyingLanguages = (Spinner)findViewById(R.id.studingLanguageSpinner);


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailData = email.getText().toString();
                passwordData = password.getText().toString();
                usernameData = username.getText().toString();
                nativeLanguage = nativeLanguages.getSelectedItem().toString();
                studyingLanguage = studyingLanguages.getSelectedItem().toString();
                Log.wtf("Register", nativeLanguage + " " +studyingLanguage );
                registerUser(emailData, passwordData);
            }
        });
    }

    public void registerUser(String email, String stringPassword){

        if(checkPasswordConstraints(stringPassword)) {
            mAuth.createUserWithEmailAndPassword(email, stringPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        LoginActivity.current_user = mAuth.getCurrentUser();

                        addUserToDB();

                        Intent intent = new Intent(RegisterActivity.this, CategoryActivity.class);
                        startActivity(intent);
                        finish();

                    }
                    else{
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            password.setError("Password must be at least 6 characters long!");
        }

    }

    public boolean checkPasswordConstraints(String password){
        if(password.length()<6){
            return false;
        }
        else{
            return true;
        }
    }

    public void addUserToDB(){
        DatabaseReference ref = firebaseDatabase.getReference().child("Users");
        DatabaseReference new_user_ref = ref.child(LoginActivity.current_user.getUid());

        String deviceTokenID = FirebaseInstanceId.getInstance().getToken();
        new_user_ref.setValue(new User(usernameData,emailData,passwordData, nativeLanguage, studyingLanguage));

        ref.child(LoginActivity.current_user.getUid())
                .child("tokenID").setValue(deviceTokenID);

        LoginActivity.retrieveLanguageConfig(LoginActivity.current_user.getUid());

    }
}
