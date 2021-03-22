package com.example.crowdfunding.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.crowdfunding.Adapters.CommentAdapter;
import com.example.crowdfunding.Models.Comment;
import com.example.crowdfunding1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost, imgUserPost, imgCurrentUser;
    TextView textPostTitle, textPostDesc, textPostDateName, fPercentage, nbrContribution, dateExp, montantProject,date_expiration;
    EditText editTextComment, montantContribute;
    Button addCommentBtn, contributeBtn;
    ProgressBar progressF;
    String postKey,postTitle,userpostImage,postDesc,postImage;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;

    RecyclerView rvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;
    static String COMMENT_KEY = "Comment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_post_detail);


        // set the statue bar to transparent


         /*Window w = getWindow();
         w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
         getSupportActionBar().hide();*/

        // init Views

        rvComment = findViewById(R.id.rv_comment);

        imgPost = findViewById(R.id.post_detail_image);
        imgUserPost = findViewById(R.id.post_detail_user);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        textPostTitle = findViewById(R.id.post_detail_title);
        textPostDesc = findViewById(R.id.post_detail_description);
        textPostDateName = findViewById(R.id.post_detail_date_name);
        fPercentage = findViewById(R.id.tv_percentage);
        date_expiration = findViewById(R.id.tv_date_expiration);
        dateExp = findViewById(R.id.tv_date_expiration);
        montantProject = findViewById(R.id.tv_montant_info);
        contributeBtn = findViewById(R.id.contribute_btn);

        progressF = findViewById(R.id.financedProgress);

        editTextComment = findViewById(R.id.post_detail_comment);
        nbrContribution = findViewById(R.id.contribute_montant);
        addCommentBtn = findViewById(R.id.post_detail_add_comm);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = firebaseDatabase.getInstance();

        /*
                get POST data
         */

        // get post id
        postKey = getIntent().getExtras().getString("postKey");

        postImage = getIntent().getExtras().getString("postImage");
        Glide.with(this).load(postImage).into(imgPost);

        postTitle = getIntent().getExtras().getString("title");
        textPostTitle.setText(postTitle);

        userpostImage = getIntent().getExtras().getString("imgUserPost");
        Glide.with(this).load(userpostImage).into(imgUserPost);

        postDesc = getIntent().getExtras().getString("description");
        textPostDesc.setText(postDesc);



        // add Comment button click listener

        addCommentBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                addCommentBtn.setVisibility(View.INVISIBLE);

                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(postKey).push();
                String comm_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String username = firebaseUser.getDisplayName();
                String userimg = firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null;
                Comment comment = new Comment(uid, comm_content, userimg, username);

                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("Comment added !");
                        editTextComment.setText("");
                        addCommentBtn.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Adding comment failed !! " + e.getMessage());
                    }
                });
            }
        });


        contributeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent postDetailActivity = new Intent(getApplicationContext(), CardList.class);

                postDetailActivity.putExtra("idproject", postKey);
                postDetailActivity.putExtra("title", postTitle);
                postDetailActivity.putExtra("iduser", firebaseUser.getUid());
                postDetailActivity.putExtra("amount", Integer.parseInt(nbrContribution.getText().toString()));

                startActivity(postDetailActivity);
            }
        });




        // set comment image
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);


        System.out.println(postKey);

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        textPostDateName.setText(date);
        int montant = getIntent().getExtras().getInt("montant");
        int montant_r = getIntent().getExtras().getInt("montant_r");
        System.out.println(montant + "\n" + montant_r);
        fPercentage.setText((montant_r * 100 / montant) + "% financed!");
        dateExp.setText("Expriration date : "+getIntent().getExtras().getString("dateExp"));

        progressF.setProgress((montant_r * 100 / montant));
        montantProject.setText(montant_r + "$ / " + montant + "$");

        // init RecycleView Comments
        initRvComment();
    }

    private void initRvComment() {

        rvComment.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(postKey);
        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap: dataSnapshot.getChildren()){

                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);
                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                rvComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private String timestampToString(long time){

        Calendar calendar = Calendar.getInstance(Locale.FRANCE);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}
