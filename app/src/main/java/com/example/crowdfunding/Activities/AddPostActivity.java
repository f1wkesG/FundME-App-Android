package com.example.crowdfunding.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crowdfunding.Models.PostCategory;
import com.example.crowdfunding.Models.Project;
import com.example.crowdfunding1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPostActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;

    private Uri pickedImgUri = null;

    private String imageUrl;

    private TextView title , desc, date, website, videoLink, montant;
    private Spinner category;
    private Button postBtn, chooseImageBtn, uploadImageBtn;
    private ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        imageUrl = "";

        title = (TextView) findViewById(R.id.projetTitleTxt);
        desc = (TextView) findViewById(R.id.projectDescTxt);
        montant = (TextView) findViewById(R.id.montantProjet);

        date = (TextView) findViewById(R.id.projetDate);
        website = (TextView) findViewById(R.id.websiteTxt);
        videoLink = (TextView) findViewById(R.id.videoLinkTxt);
        category = (Spinner) findViewById(R.id.categoryS);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.post_categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        category.setAdapter(adapter);

        imagePreview = (ImageView) findViewById(R.id.imagePreview);


        postBtn= (Button) findViewById(R.id.createProjectBtn);
        chooseImageBtn = (Button) findViewById(R.id.chooseImageBtn);
        uploadImageBtn = (Button) findViewById(R.id.uploadImagesBtn);

        chooseImageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("project_images");
                final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = imageUrl + uri.toString() + ";";
                                showMessage("Image uploaded");
                                imagePreview.setImageDrawable(null);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage(e.getMessage());

                            }
                        });
                    }
                });
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!title.getText().toString().isEmpty()){
                    if(!desc.getText().toString().isEmpty()){
                        if(!montant.getText().toString().isEmpty()){
                            Project p = new Project(currentUser.getUid(), title.getText().toString(), desc.getText().toString(), Integer.parseInt(montant.getText().toString()), imageUrl, website.getText().toString(), videoLink.getText().toString(), category.getSelectedItem().toString(), date.getText().toString());
                            addProject(p);

                            Intent homeActivity = new Intent(getApplicationContext(), Home.class);
                            startActivity(homeActivity);
                            finish();

                        }else{
                            showMessage("You must give an amount !!");

                        }

                    }else{

                    }

                }else{
                    showMessage("You must enter a title !!");
                }
            }
        });
    }

    private void checkAndRequestForPermission(){
        if(ContextCompat.checkSelfPermission(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(AddPostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)){


                Toast.makeText(AddPostActivity.this, "Veuillez accepter les permissions requises", Toast.LENGTH_SHORT).show();


            }else{


                ActivityCompat.requestPermissions(AddPostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);


            }

        }else{
            openGallery();
        }
    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESCODE && data != null){

            pickedImgUri = data.getData();
            imagePreview.setImageURI(pickedImgUri);

        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void addProject(Project p){

        FirebaseDatabase database = FirebaseDatabase.getInstance().getInstance();
        DatabaseReference myRef = database.getReference("Projects").push();

        // avoir un id unique pour chaque post
        String project_id = myRef.getKey();
        p.setProjectId(project_id);
        DatabaseReference myRef2 = database.getReference("Categories").child(category.getSelectedItem().toString()).push();
        PostCategory pc = new PostCategory();
        pc.setP_id(project_id);
        myRef2.setValue(pc);
        // inserer les données dans la base de données firebase
        myRef.setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Project added successfully");
            }
        });

    }
}
