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

public class AnimesFragment extends Fragment {
    RecyclerView mRecyclerView;
    FirebaseDatabase mfirebaseDatabase;

    DatabaseReference mRef;
    Button summerbtn,springbtn,winterbtn,fallbtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    LayoutManager mLayoutManager;

    //database reference

    DatabaseReference mDatabaseRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_animes,container,false);


        //set layout as LL

            }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);
        Toolbar toolbar = getView().findViewById(R.id.toolbar);



        summerbtn = (Button) getView().findViewById(R.id.Summer);
        springbtn =(Button) getView().findViewById(R.id.Spring);
        winterbtn = (Button) getView().findViewById(R.id.Winter);
        fallbtn =(Button) getView().findViewById(R.id.Fall);

        //RecyclerView
        mRecyclerView = getView().findViewById(R.id.recyclerView);

        //set layout as LL
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //send query to FB
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mfirebaseDatabase.getReference("animes");







        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    final String userId = user.getUid();
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
                    mDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(userId)) {


                            } else {
                               // getActivity().finish();
                                // Intent moveToSetupProfile = new Intent(AnimesFragment.this.getActivity(), UserProfileActivity.class);
                                //moveToSetupProfile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               // startActivity(moveToSetupProfile);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    //getActivity().finish();
                    //startActivity(new Intent(AnimesFragment.this.getActivity(), LoginActivity.class));
                }
            }

        };

        mAuth.addAuthStateListener(mAuthListener);

        summerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query sumseason = mRef.orderByChild("season").equalTo("Summer");


                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.row,
                                ViewHolder.class,
                                sumseason
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
                fallbtn.setBackgroundColor(Color.TRANSPARENT);
                summerbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
                springbtn.setBackgroundColor(Color.TRANSPARENT);
                winterbtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });


        springbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query springseason = mRef.orderByChild("season").equalTo("Spring");



                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.row,
                                ViewHolder.class,
                                springseason
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
                fallbtn.setBackgroundColor(Color.TRANSPARENT);
                summerbtn.setBackgroundColor(Color.TRANSPARENT);
                springbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
                winterbtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        fallbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query fallseason = mRef.orderByChild("season").equalTo("Fall");



                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.row,
                                ViewHolder.class,
                                fallseason
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
                fallbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
                summerbtn.setBackgroundColor(Color.TRANSPARENT);
                springbtn.setBackgroundColor(Color.TRANSPARENT);
                winterbtn.setBackgroundColor(Color.TRANSPARENT);
            }
        });
        winterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query winterseason = mRef.orderByChild("season").equalTo("Winter");



                FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                        new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                                animodel.class,
                                R.layout.row,
                                ViewHolder.class,
                                winterseason
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
                fallbtn.setBackgroundColor(Color.TRANSPARENT);
                summerbtn.setBackgroundColor(Color.TRANSPARENT);
                springbtn.setBackgroundColor(Color.TRANSPARENT);
                winterbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));

            }
        });
    }

    //search data
    private void firebaseSearch(String searchText){

        Query firebaseSearchQuery = mRef.orderByChild("animename").startAt(searchText).endAt(searchText+"\uf8ff");


        FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                        animodel.class,
                        R.layout.row,
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
        Query fallseason = mRef.orderByChild("season").equalTo("Fall");




        FirebaseRecyclerAdapter<animodel, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<animodel, ViewHolder>(
                        animodel.class,
                        R.layout.row,
                        ViewHolder.class,
                        fallseason
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
        fallbtn.setBackgroundColor(getResources().getColor(R.color.butoncolor));
        summerbtn.setBackgroundColor(Color.TRANSPARENT);
        springbtn.setBackgroundColor(Color.TRANSPARENT);
        winterbtn.setBackgroundColor(Color.TRANSPARENT);

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


