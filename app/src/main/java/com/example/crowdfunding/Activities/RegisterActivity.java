package com.example.crowdfunding.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.crowdfunding1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText userEmail, userPassword, userPassword2, username, userFirstName, userLastName;
    private ProgressBar loadingProgress;
    private ImageView previous;
    private Button regBtn;


    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);

        // views
        userEmail = findViewById(R.id.emailReg);
        username = findViewById(R.id.usernameReg);
        userPassword = findViewById(R.id.RegPassword);
        userPassword2 = findViewById(R.id.repasswordReg);
        loadingProgress = findViewById(R.id.regProgressBar);
        regBtn = findViewById(R.id.btnReg);
        previous = findViewById(R.id.previous);
        mAuth = FirebaseAuth.getInstance();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        regBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String name = username.getText().toString();
                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();
                final String password2 = userPassword2.getText().toString();

                if(email.isEmpty() || name.isEmpty() || password.isEmpty() || !password2.equals(password)){

                    // Erreur dans les inputs
                    // affichage d'une erreur

                    showMessage("Veuillez verifier tous les champs");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }else{
                    // tout ce passe bien

                    CreateUser(name, email, password);
                }


            }

            private void CreateUser(final String name, String email, String password) {

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    // user account created succesfully
                                    showMessage("Account created");
                                    // apres creation du compte, on doit mettre a jour ses information
                                    FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();



                                    DatabaseReference myRef = database.getReference("Compte").child(mAuth.getCurrentUser().getUid());

                                    Map<String, String> cInfo = new HashMap<>();
                                    cInfo.put("paypal", "rien");
                                    myRef.setValue(cInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                                    updateUserInfo(name, mAuth.getCurrentUser());

                                }else{

                                    // account creating failed
                                    showMessage("Votre compte n'est pas créé");
                                    regBtn.setVisibility(View.VISIBLE);
                                    loadingProgress.setVisibility(View.INVISIBLE);

                                }
                            }
                        });
            }

            private void showMessage(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
            private void updateUserInfo(String name, FirebaseUser currentUser) {
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build();
                currentUser.updateProfile(profileUpdate)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    showMessage("Inscription complete");
                                    updateUI();
                                }
                            }
                        });

            }

            private void updateUI() {
                Intent homeActivity = new Intent(getApplicationContext(), Home.class);
                startActivity(homeActivity);
                finish();
            }
        });
        loadingProgress.setVisibility(View.INVISIBLE);

    }
}
