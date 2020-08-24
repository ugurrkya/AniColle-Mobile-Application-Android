package com.anicolle.ju.anicolle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.anicolle.ju.anicolle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class AnimeDetailsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    TextView mAnimeName, mProducer, mStudio, mSeason, mType, mSourcee, mCountry, mDuration, mCurrentE,mPlannedE,mGenre,mBroadcast, mRelation;
    EditText editEpNumber;
    Button ratebutton, addbutton;
    ImageView animePhoto;
    Spinner aniSpinner;
    private RatingBar aniratebar;
    private DatabaseReference profileUserRef;
    private String currentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference listRef;
    private DatabaseReference rateRef;
    private int rt;
    private DatabaseReference ratingbutRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_details);


        ActionBar actionBar = getSupportActionBar();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
        listRef= FirebaseDatabase.getInstance().getReference();
        aniSpinner = (Spinner)findViewById(R.id.dtlistname);
        ratingbutRef =  FirebaseDatabase.getInstance().getReference();
       final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.lists,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aniSpinner.setAdapter(adapter);
        aniSpinner.setOnItemSelectedListener(this);


        ratebutton = (Button)findViewById(R.id.ratebtn);
        addbutton = (Button)findViewById(R.id.addbtn);

        aniratebar =(RatingBar)findViewById(R.id.ratingBar);
        editEpNumber =(EditText)findViewById(R.id.edittextepnum);

        mAnimeName = (TextView)findViewById(R.id.dtanimeName);
        mProducer = (TextView) findViewById(R.id.dtproducer);
        mStudio =  (TextView) findViewById(R.id.dtstudio);
        mSeason =(TextView) findViewById(R.id.dtseason);
        mType = (TextView)findViewById(R.id.dttype);
        mSourcee =(TextView) findViewById(R.id.dtsource);
        mCountry =(TextView) findViewById(R.id.dtcountry);
        mDuration = (TextView) findViewById(R.id.dtduration);
        mCurrentE = (TextView)findViewById(R.id.dtcurrentep);
        mPlannedE = (TextView)findViewById(R.id.dtplannedep);
        mGenre =(TextView)findViewById(R.id.dtgenre);
        mBroadcast = (TextView)findViewById(R.id.dtbro);
        mRelation = (TextView)findViewById(R.id.dtrelation);
        animePhoto = (ImageView)findViewById(R.id.dtanimeImage);




        //get data from intent
        String image = getIntent().getStringExtra("animephoto");
        String aniname = getIntent().getStringExtra("animename");
        String prod = getIntent().getStringExtra("producer");
        String seas = getIntent().getStringExtra("season");
        String typ = getIntent().getStringExtra("type");
        String curre = getIntent().getStringExtra("currentep");
        String pllan = getIntent().getStringExtra("plannedep");
        String durat = getIntent().getStringExtra("duration");
        String couun = getIntent().getStringExtra("country");
        String broadd = getIntent().getStringExtra("broadcast");
        String relat = getIntent().getStringExtra("relation");
        String gennr = getIntent().getStringExtra("genre");
        String souu = getIntent().getStringExtra("source");
        String stdi = getIntent().getStringExtra("studio");


        mAnimeName.setText(aniname);
        mProducer.setText(prod);
        mStudio.setText(stdi);
        mRelation.setText(relat);
        mCurrentE.setText(curre);
        mPlannedE.setText(pllan);
        mGenre.setText(gennr);
        mBroadcast.setText(broadd);
        mType.setText(typ);
        mSeason.setText(seas);
        mCountry.setText(couun);
        mSourcee.setText(souu);
        mDuration.setText(durat);
        Picasso.get().load(image).into(animePhoto);

















        rateRef = FirebaseDatabase.getInstance().getReference().child("animes");


        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String myUsername = dataSnapshot.child("username").getValue().toString();
                    String myAnimeName= mAnimeName.getText().toString();
                    String whom = myUsername+myAnimeName;
                   DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("lists").child(myUsername).child(whom);

                   checkRef.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if(dataSnapshot.exists()){
                          // String userrate = dataSnapshot.child("rating").getValue().toString();
                           String userepisode = dataSnapshot.child("watchedep").getValue().toString();
                           String userlist = dataSnapshot.child("listname").getValue().toString();
                           editEpNumber.setText(userepisode);
                          // float rating = Float.parseFloat(userrate);
                         //  aniratebar.setRating(rating);
                           aniSpinner.setSelection(adapter.getPosition(userlist));
                       }
                   }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String myUsername = dataSnapshot.child("username").getValue().toString();
                    String myAnimeName= mAnimeName.getText().toString();
                    String whom = myUsername+myAnimeName;
                    DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference().child("ratings").child(whom);

                    checkRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String userrate = dataSnapshot.child("rating").getValue().toString();

                                float rating = Float.parseFloat(userrate);
                                 aniratebar.setRating(rating);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addlist();
            }
        });

        ratebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratetoanime();
            }
        });
    }

    private void ratetoanime() {

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    final String myUsername = dataSnapshot.child("username").getValue().toString();
                    final String myAnimeName= mAnimeName.getText().toString();
                    float rating = (aniratebar.getRating());
                    String ratingg= Float.toString(rating);
                    int rat = Float.floatToIntBits(rating);
                    final HashMap<String, String> rateMap = new HashMap<>();
                    rateMap.put("username",myUsername);
                    rateMap.put("animename",myAnimeName);
                    rateMap.put("rating",ratingg);
                    final String whom = myUsername+myAnimeName;
                                ratingbutRef.child("ratings").child(whom).setValue(rateMap)
                                        . addOnCompleteListener(new OnCompleteListener<Void>() {

                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){

                                                    Toast.makeText(AnimeDetailsActivity.this, "You rated the anime", Toast.LENGTH_SHORT).show();


                                                }else{
                                                    String message = task.getException().toString();
                                                    Toast.makeText(AnimeDetailsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }




    private void addlist() {
        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    String myProfileImage = dataSnapshot.child("imageurl").getValue().toString();
                    final String myUsername = dataSnapshot.child("username").getValue().toString();
                    final String myAnimeName= mAnimeName.getText().toString();
                    final String myListName = aniSpinner.getSelectedItem().toString();
                    String mywatchedep = editEpNumber.getText().toString();
                    String myplannedep = mPlannedE.getText().toString();
                    String myanimephoto= getIntent().getStringExtra("animephoto");

                    String prod = getIntent().getStringExtra("producer");
                    String seas = getIntent().getStringExtra("season");
                    String typ = getIntent().getStringExtra("type");
                    String curre = getIntent().getStringExtra("currentep");
                    String pllan = getIntent().getStringExtra("plannedep");
                    String durat = getIntent().getStringExtra("duration");
                    String couun = getIntent().getStringExtra("country");
                    String broadd = getIntent().getStringExtra("broadcast");
                    String relat = getIntent().getStringExtra("relation");
                    String gennr = getIntent().getStringExtra("genre");
                    String souu = getIntent().getStringExtra("source");
                    String stdi = getIntent().getStringExtra("studio");


                 //   float rating = (aniratebar.getRating());
                   // String ratingg= Float.toString(rating);
                   // int rat = Float.floatToIntBits(rating);
                    final HashMap<String, String> profileMap = new HashMap<>();
                    profileMap.put("username",myUsername);
                    profileMap.put("userphoto",myProfileImage);
                    profileMap.put("animename",myAnimeName);
                    profileMap.put("listname", myListName);
                    profileMap.put("watchedep", mywatchedep);
                    profileMap.put("plannedep", myplannedep);
                    profileMap.put("animephoto", myanimephoto);


                    profileMap.put("producer", prod);
                    profileMap.put("season", seas);
                    profileMap.put("type", typ);
                    profileMap.put("currentep",curre);
                    profileMap.put("duration", durat);
                    profileMap.put("country", couun);
                    profileMap.put("broadcast", broadd);
                    profileMap.put("relation",relat);
                    profileMap.put("genre", gennr);
                    profileMap.put("source", souu);
                    profileMap.put("studio", stdi);


                    final String whom = myUsername+myAnimeName;


                                listRef.child("lists").child(myUsername).child(whom).setValue(profileMap)
                                        . addOnCompleteListener(new OnCompleteListener<Void>() {

                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(AnimeDetailsActivity.this, "You added the anime to the "+myListName+" list", Toast.LENGTH_SHORT).show();


                                                }else{
                                                    String message = task.getException().toString();
                                                    Toast.makeText(AnimeDetailsActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                        }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });









    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text= parent.getItemAtPosition(position).toString();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
