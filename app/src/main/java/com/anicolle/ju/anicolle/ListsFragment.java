package com.anicolle.ju.anicolle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

public class ListsFragment extends Fragment {
    RecyclerView mRecyclerView;
    FirebaseDatabase mfirebaseDatabase;

    private DatabaseReference mRef;
    Button watchingbtn, ptwbtn,droppedbtn,completedbtn,favoritebtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    LayoutManager mLayoutManager;
    private DatabaseReference checkRef;
    //database reference
    private DatabaseReference aniRef;
    DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lists,container,false);


        //set layout as LL

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        Toolbar toolbar = getView().findViewById(R.id.toolbar);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("animes");

        watchingbtn = (Button) getView().findViewById(R.id.watching);
        droppedbtn =(Button) getView().findViewById(R.id.dropped);
        completedbtn = (Button) getView().findViewById(R.id.completed);
        ptwbtn =(Button) getView().findViewById(R.id.ptw);
        favoritebtn =(Button) getView().findViewById(R.id.favorite);
        //RecyclerView
        mRecyclerView = getView().findViewById(R.id.recyclerView);

        //set layout as LL
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //send query to FB






        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    final String userId = user.getUid();

                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);;

                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                             final String myUsername = dataSnapshot.child("username").getValue().toString();

                                checkRef = FirebaseDatabase.getInstance().getReference().child("lists").child(myUsername);
                                aniRef = FirebaseDatabase.getInstance().getReference();
                                aniRef.child("animes").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for(DataSnapshot uniquekeySnapshot : dataSnapshot.getChildren()){
                                            for(DataSnapshot animesSnapshot : uniquekeySnapshot.child("animename").getChildren()){
                                                String anime= animesSnapshot.getValue().toString();
                                                String whom = myUsername+anime;
                                                checkRef = FirebaseDatabase.getInstance().getReference().child("lists").child(myUsername).child(whom);
                                            }
                                        }


                                        // String AnimeName= dataSnapshot.child("animename").getValue().toString();
                                        //String whom = myUsername+AnimeName;
                                        //checkRef = FirebaseDatabase.getInstance().getReference().child("lists").child(myUsername).child(whom);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            } else {
                               // getActivity().finish();
                               // Intent moveToSetupProfile = new Intent(ListsFragment.this.getActivity(), UserProfileActivity.class);
                               // moveToSetupProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               // startActivity(moveToSetupProfile);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                   // getActivity().finish();
                    //startActivity(new Intent(ListsFragment.this.getActivity(), LoginActivity.class));
                }
            }

        };

        mAuth.addAuthStateListener(mAuthListener);

        watchingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query watchlist = checkRef.orderByChild("listname").equalTo("Watching");


                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.rowlist,
                                ViewHolder.class,
                                watchlist
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHolder viewHolder, animodel animodel, int position) {

                                viewHolder.setDetails(getActivity().getApplicationContext(), animodel.getAnimename(), animodel.getAnimephoto(),animodel.getProducer()
                                        ,animodel.getBroadcast(),animodel.getCountry(),animodel.getCurrentep(),animodel.getDuration(),animodel.getGenre(),animodel.getPlannedep(),
                                        animodel.getRelation(),animodel.getSource(),animodel.getSeason(),animodel.getStudio(),animodel.getType()


                                );

                            }

                            @Override
                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mAniName = getItem(position).getAnimename();
                                        String mImage =getItem(position).getAnimephoto();
                                        String mproducer = getItem(position).getProducer();
                                        String mbroad = getItem(position).getBroadcast();
                                        String countr = getItem(position).getCountry();
                                        String curre = getItem(position).getCurrentep();
                                        String dur = getItem(position).getDuration();
                                        String gen = getItem(position). getGenre();
                                        String plan = getItem(position).getPlannedep();
                                        String rel = getItem(position).getRelation();
                                        String sou = getItem(position).getSource();
                                        String seas = getItem(position).getSeason();
                                        String std = getItem(position).getStudio();
                                        String typee = getItem(position).getType();

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),AnimeDetailsActivity.class);
                                        intent.putExtra("animename", mAniName);
                                        intent.putExtra("animephoto",mImage);
                                        intent.putExtra("producer",mproducer);
                                        intent.putExtra("broadcast", mbroad);
                                        intent.putExtra("country", countr);
                                        intent.putExtra("currentep",curre);
                                        intent.putExtra("duration", dur);
                                        intent.putExtra("genre",gen);
                                        intent.putExtra("plannedep",plan);
                                        intent.putExtra("relation",rel);
                                        intent.putExtra("source",sou);
                                        intent.putExtra("season",seas);
                                        intent.putExtra("studio",std);
                                        intent.putExtra("type",typee);

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                droppedbtn.setBackgroundColor(Color.TRANSPARENT);
                watchingbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
                ptwbtn.setBackgroundColor(Color.TRANSPARENT);
                completedbtn.setBackgroundColor(Color.TRANSPARENT);
                favoritebtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        droppedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query droppedlist =checkRef.orderByChild("listname").equalTo("Dropped");



                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.rowlist,
                                ViewHolder.class,
                                droppedlist
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHolder viewHolder, animodel animodel, int position) {

                                viewHolder.setDetails(getActivity().getApplicationContext(), animodel.getAnimename(), animodel.getAnimephoto(),animodel.getProducer()
                                        ,animodel.getBroadcast(),animodel.getCountry(),animodel.getCurrentep(),animodel.getDuration(),animodel.getGenre(),animodel.getPlannedep(),
                                        animodel.getRelation(),animodel.getSource(),animodel.getSeason(),animodel.getStudio(),animodel.getType()


                                );

                            }

                            @Override
                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mAniName = getItem(position).getAnimename();
                                        String mImage =getItem(position).getAnimephoto();
                                        String mproducer = getItem(position).getProducer();
                                        String mbroad = getItem(position).getBroadcast();
                                        String countr = getItem(position).getCountry();
                                        String curre = getItem(position).getCurrentep();
                                        String dur = getItem(position).getDuration();
                                        String gen = getItem(position). getGenre();
                                        String plan = getItem(position).getPlannedep();
                                        String rel = getItem(position).getRelation();
                                        String sou = getItem(position).getSource();
                                        String seas = getItem(position).getSeason();
                                        String std = getItem(position).getStudio();
                                        String typee = getItem(position).getType();

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),AnimeDetailsActivity.class);
                                        intent.putExtra("animename", mAniName);
                                        intent.putExtra("animephoto",mImage);
                                        intent.putExtra("producer",mproducer);
                                        intent.putExtra("broadcast", mbroad);
                                        intent.putExtra("country", countr);
                                        intent.putExtra("currentep",curre);
                                        intent.putExtra("duration", dur);
                                        intent.putExtra("genre",gen);
                                        intent.putExtra("plannedep",plan);
                                        intent.putExtra("relation",rel);
                                        intent.putExtra("source",sou);
                                        intent.putExtra("season",seas);
                                        intent.putExtra("studio",std);
                                        intent.putExtra("type",typee);

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                droppedbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
                watchingbtn.setBackgroundColor(Color.TRANSPARENT);
                ptwbtn.setBackgroundColor(Color.TRANSPARENT);
                completedbtn.setBackgroundColor(Color.TRANSPARENT);
                favoritebtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        ptwbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query ptwlist = checkRef.orderByChild("listname").equalTo("PTW");



                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.rowlist,
                                ViewHolder.class,
                                ptwlist
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHolder viewHolder, animodel animodel, int position) {

                                viewHolder.setDetails(getActivity().getApplicationContext(), animodel.getAnimename(), animodel.getAnimephoto(),animodel.getProducer()
                                        ,animodel.getBroadcast(),animodel.getCountry(),animodel.getCurrentep(),animodel.getDuration(),animodel.getGenre(),animodel.getPlannedep(),
                                        animodel.getRelation(),animodel.getSource(),animodel.getSeason(),animodel.getStudio(),animodel.getType()


                                );

                            }

                            @Override
                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mAniName = getItem(position).getAnimename();
                                        String mImage =getItem(position).getAnimephoto();
                                        String mproducer = getItem(position).getProducer();
                                        String mbroad = getItem(position).getBroadcast();
                                        String countr = getItem(position).getCountry();
                                        String curre = getItem(position).getCurrentep();
                                        String dur = getItem(position).getDuration();
                                        String gen = getItem(position). getGenre();
                                        String plan = getItem(position).getPlannedep();
                                        String rel = getItem(position).getRelation();
                                        String sou = getItem(position).getSource();
                                        String seas = getItem(position).getSeason();
                                        String std = getItem(position).getStudio();
                                        String typee = getItem(position).getType();

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),AnimeDetailsActivity.class);
                                        intent.putExtra("animename", mAniName);
                                        intent.putExtra("animephoto",mImage);
                                        intent.putExtra("producer",mproducer);
                                        intent.putExtra("broadcast", mbroad);
                                        intent.putExtra("country", countr);
                                        intent.putExtra("currentep",curre);
                                        intent.putExtra("duration", dur);
                                        intent.putExtra("genre",gen);
                                        intent.putExtra("plannedep",plan);
                                        intent.putExtra("relation",rel);
                                        intent.putExtra("source",sou);
                                        intent.putExtra("season",seas);
                                        intent.putExtra("studio",std);
                                        intent.putExtra("type",typee);

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                droppedbtn.setBackgroundColor(Color.TRANSPARENT);
                watchingbtn.setBackgroundColor(Color.TRANSPARENT);
                ptwbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
                completedbtn.setBackgroundColor(Color.TRANSPARENT);
                favoritebtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        completedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query completedlist =checkRef.orderByChild("listname").equalTo("Completed");



                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.rowlist,
                                ViewHolder.class,
                                completedlist
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHolder viewHolder, animodel animodel, int position) {

                                viewHolder.setDetails(getActivity().getApplicationContext(), animodel.getAnimename(), animodel.getAnimephoto(),animodel.getProducer()
                                        ,animodel.getBroadcast(),animodel.getCountry(),animodel.getCurrentep(),animodel.getDuration(),animodel.getGenre(),animodel.getPlannedep(),
                                        animodel.getRelation(),animodel.getSource(),animodel.getSeason(),animodel.getStudio(),animodel.getType()


                                );

                            }

                            @Override
                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mAniName = getItem(position).getAnimename();
                                        String mImage =getItem(position).getAnimephoto();
                                        String mproducer = getItem(position).getProducer();
                                        String mbroad = getItem(position).getBroadcast();
                                        String countr = getItem(position).getCountry();
                                        String curre = getItem(position).getCurrentep();
                                        String dur = getItem(position).getDuration();
                                        String gen = getItem(position). getGenre();
                                        String plan = getItem(position).getPlannedep();
                                        String rel = getItem(position).getRelation();
                                        String sou = getItem(position).getSource();
                                        String seas = getItem(position).getSeason();
                                        String std = getItem(position).getStudio();
                                        String typee = getItem(position).getType();

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),AnimeDetailsActivity.class);
                                        intent.putExtra("animename", mAniName);
                                        intent.putExtra("animephoto",mImage);
                                        intent.putExtra("producer",mproducer);
                                        intent.putExtra("broadcast", mbroad);
                                        intent.putExtra("country", countr);
                                        intent.putExtra("currentep",curre);
                                        intent.putExtra("duration", dur);
                                        intent.putExtra("genre",gen);
                                        intent.putExtra("plannedep",plan);
                                        intent.putExtra("relation",rel);
                                        intent.putExtra("source",sou);
                                        intent.putExtra("season",seas);
                                        intent.putExtra("studio",std);
                                        intent.putExtra("type",typee);

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                droppedbtn.setBackgroundColor(Color.TRANSPARENT);
                watchingbtn.setBackgroundColor(Color.TRANSPARENT);
                ptwbtn.setBackgroundColor(Color.TRANSPARENT);
                completedbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
                favoritebtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });
       favoritebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query favoritelist = checkRef.orderByChild("listname").equalTo("Favorite");



                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.rowlist,
                                ViewHolder.class,
                                favoritelist
                        ) {
                            @Override
                            protected void populateViewHolder(ViewHolder viewHolder, animodel animodel, int position) {

                                viewHolder.setDetails(getActivity().getApplicationContext(), animodel.getAnimename(), animodel.getAnimephoto(),animodel.getProducer()
                                        ,animodel.getBroadcast(),animodel.getCountry(),animodel.getCurrentep(),animodel.getDuration(),animodel.getGenre(),animodel.getPlannedep(),
                                        animodel.getRelation(),animodel.getSource(),animodel.getSeason(),animodel.getStudio(),animodel.getType()


                                );

                            }

                            @Override
                            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        //Views


                                        //get data from views

                                        String mAniName = getItem(position).getAnimename();
                                        String mImage =getItem(position).getAnimephoto();
                                        String mproducer = getItem(position).getProducer();
                                        String mbroad = getItem(position).getBroadcast();
                                        String countr = getItem(position).getCountry();
                                        String curre = getItem(position).getCurrentep();
                                        String dur = getItem(position).getDuration();
                                        String gen = getItem(position). getGenre();
                                        String plan = getItem(position).getPlannedep();
                                        String rel = getItem(position).getRelation();
                                        String sou = getItem(position).getSource();
                                        String seas = getItem(position).getSeason();
                                        String std = getItem(position).getStudio();
                                        String typee = getItem(position).getType();

                                        //pass this data to new activity
                                        Intent intent = new Intent(view.getContext(),AnimeDetailsActivity.class);
                                        intent.putExtra("animename", mAniName);
                                        intent.putExtra("animephoto",mImage);
                                        intent.putExtra("producer",mproducer);
                                        intent.putExtra("broadcast", mbroad);
                                        intent.putExtra("country", countr);
                                        intent.putExtra("currentep",curre);
                                        intent.putExtra("duration", dur);
                                        intent.putExtra("genre",gen);
                                        intent.putExtra("plannedep",plan);
                                        intent.putExtra("relation",rel);
                                        intent.putExtra("source",sou);
                                        intent.putExtra("season",seas);
                                        intent.putExtra("studio",std);
                                        intent.putExtra("type",typee);

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        //TODO
                                    }
                                });

                                return viewHolder;
                            }
                        };

                mRecyclerView.setAdapter(firebaseRecyclerAdapter);
                droppedbtn.setBackgroundColor(Color.TRANSPARENT);
                watchingbtn.setBackgroundColor(Color.TRANSPARENT);
                ptwbtn.setBackgroundColor(Color.TRANSPARENT);
                completedbtn.setBackgroundColor(Color.TRANSPARENT);
                favoritebtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
            }
        });
    }

    //search data
    private void firebaseSearch(String searchText){

        Query firebaseSearchQuery = mRef.orderByChild("animename").startAt(searchText).endAt(searchText+"\uf8ff");


        FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                        animodel.class,
                        R.layout.rowlist,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, animodel animodel, int position) {

                        viewHolder.setDetails(getActivity().getApplicationContext(), animodel.getAnimename(), animodel.getAnimephoto(),animodel.getProducer()
                                ,animodel.getBroadcast(),animodel.getCountry(),animodel.getCurrentep(),animodel.getDuration(),animodel.getGenre(),animodel.getPlannedep(),
                                animodel.getRelation(),animodel.getSource(),animodel.getSeason(),animodel.getStudio(),animodel.getType()


                        );

                    }


                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);


                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views
                                String mAniName = getItem(position).getAnimename();
                                String mImage =getItem(position).getAnimephoto();
                                String mproducer = getItem(position).getProducer();
                                String mbroad = getItem(position).getBroadcast();
                                String countr = getItem(position).getCountry();
                                String curre = getItem(position).getCurrentep();
                                String dur = getItem(position).getDuration();
                                String gen = getItem(position). getGenre();
                                String plan = getItem(position).getPlannedep();
                                String rel = getItem(position).getRelation();
                                String sou = getItem(position).getSource();
                                String seas = getItem(position).getSeason();
                                String std = getItem(position).getStudio();
                                String typee = getItem(position).getType();

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(),AnimeDetailsActivity.class);
                                intent.putExtra("animename", mAniName);
                                intent.putExtra("animephoto",mImage);
                                intent.putExtra("producer",mproducer);
                                intent.putExtra("broadcast", mbroad);
                                intent.putExtra("country", countr);
                                intent.putExtra("currentep",curre);
                                intent.putExtra("duration", dur);
                                intent.putExtra("genre",gen);
                                intent.putExtra("plannedep",plan);
                                intent.putExtra("relation",rel);
                                intent.putExtra("source",sou);
                                intent.putExtra("season",seas);
                                intent.putExtra("studio",std);
                                intent.putExtra("type",typee);

                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO
                            }
                        });

                        return viewHolder;
                    }




                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }






    //load data into RWiew onStart
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //infilate the menu this add items to the action bar if it present
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter as you type
                firebaseSearch(newText);
                return false;
            }
        });

    }




    @Override
    public void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }




}


