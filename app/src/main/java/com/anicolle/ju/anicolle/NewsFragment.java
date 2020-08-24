package com.anicolle.ju.anicolle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsFragment extends Fragment {

    RecyclerView mRecyclerView;


    private DatabaseReference allnewsRef;



    //database reference

    DatabaseReference mDatabaseRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        setHasOptionsMenu(true);
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //set layout as LL
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        allnewsRef = FirebaseDatabase.getInstance().getReference("news");


        FirebaseRecyclerAdapter<newsmodel, NewsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<newsmodel, NewsViewHolder>(
                        newsmodel.class,
                        R.layout.rownews,
                        NewsViewHolder.class,
                        allnewsRef
                ) {
                    @Override
                    protected void populateViewHolder(NewsViewHolder newsviewHolder, newsmodel newsmodel, int position) {

                        newsviewHolder.setDetails(getActivity().getApplicationContext(), newsmodel.getTitle(), newsmodel.getNewsimage(), newsmodel.getContent()

                        );

                    }

                    @Override
                    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        NewsViewHolder newsviewHolder = super.onCreateViewHolder(parent, viewType);


                        newsviewHolder.setOnClickListener(new NewsViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views


                                //get data from views

                                String mTitlename = getItem(position).getTitle();
                                String mnewsimage = getItem(position).getNewsimage();
                                String mContent= getItem(position).getContent();

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(),NewsDetailsActivity.class);
                                intent.putExtra("title", mTitlename);
                                intent.putExtra("newsimage",mnewsimage);
                                intent.putExtra("content", mContent);

                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO
                            }
                        });

                        return newsviewHolder;
                    }
                };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);





    }

    @Override
    public void onStart() {
        super.onStart();

    }



}
