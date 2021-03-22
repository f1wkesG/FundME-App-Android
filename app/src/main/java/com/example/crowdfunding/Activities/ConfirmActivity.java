package com.example.crowdfunding.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.crowdfunding.Models.Transaction;
import com.example.crowdfunding1.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ConfirmActivity extends AppCompatActivity {

    TextView  proj, projid, card, amount, balance, succ;
    Button    confirm;
    ImageView err, success;
    ProgressBar prog;
    String cardnumber, idproject, titleproject, iduser, amount2;
    int somme;
    DatabaseReference refcard, refproj, trans;
    Query cardnum, project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_confirm);

        proj    = findViewById(R.id.titlep);
        projid  = findViewById(R.id.idp);
        amount  = findViewById(R.id.amounttxt);
        card    = findViewById(R.id.cardtxt);
        confirm = findViewById(R.id.selectbtn);
        prog    = findViewById(R.id.progress);
        balance = findViewById(R.id.balance);
        err     = findViewById(R.id.err);
        success = findViewById(R.id.success);
        succ    = findViewById(R.id.succ);

        balance.setVisibility(View.INVISIBLE);
        err.setVisibility(View.INVISIBLE);
        success.setVisibility(View.INVISIBLE);
        succ.setVisibility(View.INVISIBLE);

        Bundle extras = getIntent().getExtras();
        iduser        = String.valueOf(extras.get("iduser"));
        cardnumber    = String.valueOf(extras.get("card"));
        idproject     = String.valueOf(extras.get("idproject"));
        titleproject  = String.valueOf(extras.get("title"));
        amount2       = String.valueOf(extras.get("amount"));
        somme         = Integer.parseInt(String.valueOf(extras.get("amount")));

        proj.setText(titleproject);
        projid.setText(idproject);
        amount.setText(amount2);
        card.setText(cardnumber);

        final Transaction transaction = new Transaction();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prog.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.INVISIBLE);

                cardnum    = FirebaseDatabase.getInstance().getReference("cards")
                                .child(cardnumber);
                project    = FirebaseDatabase.getInstance().getReference("Projects")
                                .child(idproject);
                trans      = FirebaseDatabase.getInstance().getReference("transactions");

                refcard    = (DatabaseReference) cardnum;
                refproj    = (DatabaseReference) project;

                cardnum.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final int solde = Integer.parseInt(String.valueOf(dataSnapshot.child("solde").getValue()));
                        if(solde>=somme){
                            project.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnap) {
                                    int mont = Integer.parseInt(String.valueOf(datasnap.child("montant_r").getValue()));

                                    //Transaction

                                    refcard.child("solde").setValue(String.valueOf(solde-somme));
                                    refproj.child("montant_r").setValue(mont+somme);

                                    //Registering transaction

                                    transaction.setAmount(amount2);
                                    transaction.setIduser(iduser);
                                    transaction.setIdcard(cardnumber);
                                    transaction.setIdproject(idproject);

                                    trans.push().setValue(transaction);

                                    succ.setVisibility(View.VISIBLE);
                                    success.setVisibility(View.VISIBLE);
                                    prog.setVisibility(View.INVISIBLE);
                                    confirm.setVisibility(View.VISIBLE);
                                    confirm.setEnabled(false);

                                    showMessage("Success ! Your balance after is " + (solde-somme));

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getApplicationContext(), Home.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        }
                                    }, 1000);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }else{
                            err.setVisibility(View.VISIBLE);
                            balance.setVisibility(View.VISIBLE);
                            prog.setVisibility(View.INVISIBLE);
                            showMessage("Solde insuffisant");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 900);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
    }
    public void showMessage(String text){
        Toast.makeText(getApplicationContext(), text,
                Toast.LENGTH_SHORT).show();
    }
}