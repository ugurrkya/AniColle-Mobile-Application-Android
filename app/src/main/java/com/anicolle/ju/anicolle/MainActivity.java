package com.anicolle.ju.anicolle;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   private TextView nameuser;
   private ImageView imageuser;
   private DatabaseReference profileUserRef;
    private DrawerLayout drawer;
    private String currentUserId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageuser = (ImageView) findViewById(R.id.imageofuser);
        nameuser = (TextView) findViewById(R.id.nameofuser);


        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                  String myProfileImage = dataSnapshot.child("imageurl").getValue().toString();
                        String myUsername = dataSnapshot.child("username").getValue().toString();
                   Picasso.get().load(myProfileImage).into(imageuser);
                        nameuser.setText(myUsername);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        if(savedInstanceState == null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AnimesFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_animes);}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_lists:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ListsFragment()).commit();
                break;
            case R.id.nav_animes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AnimesFragment()).commit();
                break;
            case R.id.nav_broadcast:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BroadcastFragment()).commit();
                break;
            case R.id.nav_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UsersFragment()).commit();
                break;
            case R.id.nav_signout:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SignOutFragment()).commit();
                break;
            case R.id.nav_news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NewsFragment()).commit();
                break;
            case R.id.nav_editprofile:
                Intent i = new Intent(MainActivity.this, EditProfileActivity.class);
                        startActivity(i);
                        break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }
}
