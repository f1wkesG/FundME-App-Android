package com.example.crowdfunding.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.crowdfunding1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ImageView user_image, image_add;
    private TextView username, useremail;
    private Button btnSave;
    private EditText profilecreated;
    private ProgressBar imgUploadLoading;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    private Uri pickedImgUri = null;
    private String imageUrl;


    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;







    public ProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_profile, container, false);
        username = fragmentView.findViewById(R.id.user_profile_name);
        useremail = fragmentView.findViewById(R.id.user_profile_email_tv);
        user_image = fragmentView.findViewById(R.id.user_profile_img);
        //profilecreated = fragmentView.findViewById(R.id.profile_created);
        btnSave= fragmentView.findViewById(R.id.user_profile_saveBtn);
        imgUploadLoading = fragmentView.findViewById(R.id.imageUploadprogressBar);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(!String.valueOf(currentUser.getPhotoUrl()).equals(null)){
            user_image.setBackgroundResource(0);
        }
        username.setText(currentUser.getDisplayName());
        useremail.setText(currentUser.getEmail());
        //profilecreated.setText(timestampToString(currentUser.getMetadata().getCreationTimestamp()));
        System.out.println(currentUser.getPhotoUrl());
        Glide.with(this).load(currentUser.getPhotoUrl()).into(user_image);



        imgUploadLoading.setVisibility(View.INVISIBLE);

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_image.setVisibility(View.INVISIBLE);
                imgUploadLoading.setVisibility(View.VISIBLE);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_images");
                final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setPhotoUri(pickedImgUri)
                                        .build();
                                currentUser.updateProfile(profileUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    showMessage("Informations changed");
                                                    user_image.setImageURI(pickedImgUri);
                                                    user_image.setVisibility(View.VISIBLE);
                                                    imgUploadLoading.setVisibility(View.INVISIBLE);
                                                }
                                            }
                                        });
                                imageUrl = imageUrl + uri.toString() + ";";
                                showMessage("Image uploaded");
                                user_image.setImageDrawable(null);

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




        return fragmentView;

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUESCODE && data != null){

            pickedImgUri = data.getData();
            user_image.setImageURI(pickedImgUri);

        }
    }

    private void checkAndRequestForPermission(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){


                Toast.makeText(getActivity(), "Veuillez accepter les permissions requises", Toast.LENGTH_SHORT).show();


            }else{


                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);


            }

        }else{
            openGallery();
        }
    }


    private String timestampToString(long time){

        Calendar calendar = Calendar.getInstance(Locale.FRANCE);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;

    }

    private void openGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }
    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
