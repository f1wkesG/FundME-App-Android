package com.example.crowdfunding.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crowdfunding.Models.CardInsert;
import com.example.crowdfunding1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddCardActivity extends AppCompatActivity {

    EditText number, ccv, mth, yr;
    ProgressBar progress;
    Button  proceed;
    TextView invalid;
    ImageView err;
    DatabaseReference refcard, user;
    Query card;
    String iduser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_add_card);

        number   = findViewById(R.id.cardnumber);
        ccv      = findViewById(R.id.ccv);
        mth      = findViewById(R.id.month);
        yr       = findViewById(R.id.year);
        progress = findViewById(R.id.progressBar);
        proceed  = findViewById(R.id.proceed);
        invalid  = findViewById(R.id.invalidtxt);
        err      = findViewById(R.id.err);

        invalid.setVisibility(View.INVISIBLE);
        err.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();

        iduser   = String.valueOf(extras.get("iduser"));

        number.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(number.getText().length()!=16){
                    number.setError("16 numbers is required!");
                }
            }
        });

        ccv.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(ccv.getText().length()!=3){
                    ccv.setError("3 numbers is required!");
                }
            }
        });

        mth.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(mth.getText().length()!=2){
                    mth.setError("2 number is required!");
                }
            }
        });

        yr.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(yr.getText().length()!=2){
                    yr.setError("2 number is required!");
                }
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String num = number.getText().toString().trim();

                if((number.getText().length()!=16)||
                        (yr.getText().length()!=2)||
                        (mth.getText().length()!=2)||
                        (ccv.getText().length()!=3)){
                    proceed.setError("Please enter valid informations !");
                }else{
                    progress.setVisibility(View.VISIBLE);
                    proceed.setVisibility(View.INVISIBLE);

                    card    = FirebaseDatabase.getInstance().getReference("cards")
                            .child(num);

                    refcard = (DatabaseReference) card;

                    user = FirebaseDatabase.getInstance().getReference("UserCards")
                            .child(iduser).child("cards");

                    final CardInsert cardInsert = new CardInsert();

                    card.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String Sccv = String.valueOf(dataSnapshot.child("ccv").getValue());
                            String Smth = String.valueOf(dataSnapshot.child("month").getValue());
                            String Syr = String.valueOf(dataSnapshot.child("year").getValue());

                            if (Sccv.equals(String.valueOf(ccv.getText())) &&
                                    Smth.equals(String.valueOf(mth.getText())) &&
                                    Syr.equals(String.valueOf(yr.getText()))) {

                                cardInsert.setNumber(num);
                                user.push().setValue(cardInsert);

                                Intent success = new Intent(getBaseContext(), AddSuccessActivity.class);
                                success.putExtra("iduser",iduser);
                                startActivity(success);

                            }else{
                                progress.setVisibility(View.INVISIBLE);
                                proceed.setVisibility(View.VISIBLE);
                                invalid.setVisibility(View.VISIBLE);
                                err.setVisibility(View.VISIBLE);
                                showMessage("Please verify your credentials");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    public void showMessage(String text){
        Toast.makeText(getApplicationContext(), text,
                Toast.LENGTH_SHORT).show();
    }
}