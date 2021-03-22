package com.example.crowdfunding.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crowdfunding.Adapters.CardAdapter;
import com.example.crowdfunding.Models.Card;
import com.example.crowdfunding1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CardList extends AppCompatActivity {

    RecyclerView cardRecyclerView;
    CardAdapter cardAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Card> cardList;
    Bundle bundle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_card_list);
        FloatingActionButton fab = findViewById(R.id.add);



        bundle = getIntent().getExtras();

        cardRecyclerView  = findViewById(R.id.cardRV);
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        firebaseDatabase  = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserCards").child(String.valueOf(bundle.get("iduser"))).child("cards");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCard = new Intent(getBaseContext(), PaymentActivity.class);

                addCard.putExtra("idproject",String.valueOf(bundle.get("idproject")));
                addCard.putExtra("title",String.valueOf(bundle.get("title")));
                addCard.putExtra("iduser",String.valueOf(bundle.get("iduser")));
                addCard.putExtra("amount",String.valueOf(bundle.get("amount")));

                startActivity(addCard);
            }
        });


    }

    @Override
    public void onStart()  {
        super.onStart();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cardList = new ArrayList<>();
                for (DataSnapshot cardsnap : dataSnapshot.getChildren()){
                    Card card = new Card(String.valueOf(cardsnap.child("number").getValue()),
                                         String.valueOf(bundle.get("idproject")),
                                         String.valueOf(bundle.get("title")),
                                         String.valueOf(bundle.get("iduser")),
                                         String.valueOf(bundle.get("amount")));
                    cardList.add(card);
                }
                cardAdapter = new CardAdapter(getApplicationContext(), cardList);
                cardRecyclerView.setAdapter(cardAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}