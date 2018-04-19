package com.mygdx.game;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button login, sendToRegister;

    private FirebaseAuth mAuth;
    public static FirebaseUser current_user = null;
    public static String nativeLanguage, studyingLanguage;
    public static DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        user_ref= FirebaseDatabase.getInstance().getReference().child("Users");

        email = (EditText)findViewById(R.id.loginEmailET);
        password = (EditText)findViewById(R.id.loginPasswordET);
        login  = (Button) findViewById(R.id.loginButton);
        sendToRegister = (Button) findViewById(R.id.sendToRegisterButton);

        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                                 .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                     @Override
                                                     public void onComplete(@NonNull Task<AuthResult> task) {
                                                         if (task.isSuccessful()) {
                                                             LoginActivity.current_user = mAuth.getCurrentUser();

                                                             String deviceTokenID = FirebaseInstanceId.getInstance().getToken();
                                                             Log.wtf("token", deviceTokenID);
                                                             user_ref.child(LoginActivity.current_user.getUid())
                                                                     .child("tokenID").setValue(deviceTokenID);

                                                             retrieveLanguageConfig(current_user.getUid());

                                                             Intent intent = new Intent(LoginActivity.this, CategoryActivity.class);
                                                             startActivity(intent);
                                                             finish();


                                                         }
                                                         else
                                                         {
                                                             Toast.makeText(LoginActivity.this,"Login failed: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                         }
                                                     }
                                                 });
                                     }
                                 });


        sendToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void retrieveLanguageConfig(String userUID){
        user_ref.child(userUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                switch( dataSnapshot.child("nativeLanguage").getValue().toString()){
                    case "English": {
                        nativeLanguage = "en";
                        break;
                    }
                    case "French": {
                        nativeLanguage = "fr";
                        break;
                    }
                    case "German": {
                        nativeLanguage = "de";
                        break;
                    }
                    case "Korean": {
                        nativeLanguage = "kor";
                        break;
                    }
                    case "Romanian": {
                        nativeLanguage = "ro";
                        break;
                    }
                }

                Log.wtf("LOGIN", nativeLanguage);
                switch(dataSnapshot.child("studyingLanguage").getValue().toString()){
                    case "English": {
                        studyingLanguage = "en";
                        break;
                    }
                    case "French": {
                        studyingLanguage = "fr";
                        break;
                    }
                    case "German": {
                        studyingLanguage = "de";
                        break;
                    }
                    case "Korean": {
                        studyingLanguage = "kor";
                        break;
                    }
                    case "Romanian": {
                        studyingLanguage = "ro";
                        break;
                    }
                }
                Log.wtf("LOGIN", studyingLanguage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
