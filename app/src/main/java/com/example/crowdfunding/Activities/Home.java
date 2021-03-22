package com.example.crowdfunding.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.crowdfunding.Fragments.CategoryFragment;
import com.example.crowdfunding.Fragments.HomeFragment;
import com.example.crowdfunding.Fragments.MyProjectsFragment;
import com.example.crowdfunding.Fragments.ProfileFragment;
import com.example.crowdfunding.Models.UserInsert;
import com.example.crowdfunding1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    private Intent AddPostActivity;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);


        // init
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Activities
        AddPostActivity = new Intent(this, com.example.crowdfunding.Activities.AddPostActivity.class);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                updateUI(AddPostActivity);

            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();



        // set the home fragment as the default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        insertUserInfo(currentUser.getUid(), currentUser.getDisplayName(),currentUser.getEmail(), String.valueOf(currentUser.getPhotoUrl()));
    }

    private void checkIfProjetFinanced(){

    }

    private void insertUserInfo(String uid, String username, String email, String photo){
        FirebaseUser user = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref = database.getReference("User").child(uid);

        UserInsert ui = new UserInsert(username, email, photo);
        myref.setValue(ui);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_view);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateNavHeader(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUserEmail = headerView.findViewById(R.id.nav_user_email);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);


        navUserEmail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());
        if(!String.valueOf(currentUser.getPhotoUrl()).equals(null)){
            navUserPhoto.setBackgroundResource(0);
        }
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Gerer les cliques dans la bar de navigation
        int id = item.getItemId();
        if(id == R.id.nav_home){

            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }else if(id == R.id.nav_profile){
            getSupportActionBar().setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
        }else if(id == R.id.nav_pay){
            Intent menuCardActivity = new Intent(getApplicationContext(), MenuCardList.class);

            menuCardActivity.putExtra("iduser", currentUser.getUid());

            startActivity(menuCardActivity);
        }else if (id == R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }else if(id == R.id.nav_my_projects){
            getSupportActionBar().setTitle("My Projects");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyProjectsFragment()).commit();
        }else{
            String category = item.getTitle().toString();
            getSupportActionBar().setTitle("Catégorie " + category);
            Bundle bundle = new Bundle();
            bundle.putString("cat", category);
            CategoryFragment catFragment = new CategoryFragment();
            catFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, catFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI(Intent activity) {
        startActivity(activity);
        finish();
    }
}
