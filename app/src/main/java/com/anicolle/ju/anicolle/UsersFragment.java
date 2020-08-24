package com.anicolle.ju.anicolle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
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

public class UsersFragment extends Fragment {

    RecyclerView mRecyclerView;


    private DatabaseReference allDatabaseUserreference;



    //database reference

    DatabaseReference mDatabaseRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users,container,false);
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

        allDatabaseUserreference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<usersmodel,usersmodelViewHolder> firebaseRecyclerAdapter
        = new FirebaseRecyclerAdapter<usersmodel, usersmodelViewHolder>(
                usersmodel.class,
                R.layout.rowusers,
                usersmodelViewHolder.class,
                allDatabaseUserreference
                ) {
            @Override
            protected void populateViewHolder(usersmodelViewHolder usersmodelViewHolder, usersmodel usersmodel, final int position) {
                      usersmodelViewHolder.setUsername(usersmodel.getUsername());
                      usersmodelViewHolder.setImageurl(usersmodel.getImageurl());


                      usersmodelViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              String otheruserid = getRef(position).getKey();

                              Intent otheruserintent = new Intent(UsersFragment.this.getActivity(), OtherProfileActivity.class);
                              otheruserintent.putExtra("otheruserid", otheruserid);
                              startActivity(otheruserintent);
                          }
                      });



            }


        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //search data
    private void firebaseSearch(String searchText){

        Query firebaseSearchQuery = allDatabaseUserreference.orderByChild("username").startAt(searchText).endAt(searchText+"\uf8ff");


        FirebaseRecyclerAdapter<usersmodel, usersmodelViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<usersmodel, usersmodelViewHolder>(
                        usersmodel.class,
                        R.layout.rowusers,
                        usersmodelViewHolder.class,
                        firebaseSearchQuery
                ) {

                    @Override
                    protected void populateViewHolder(usersmodelViewHolder usersmodelViewHolder, usersmodel usersmodel, final int position) {
                        usersmodelViewHolder.setUsername(usersmodel.getUsername());
                        usersmodelViewHolder.setImageurl(usersmodel.getImageurl());


                        usersmodelViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String otheruserid = getRef(position).getKey();

                                Intent otheruserintent = new Intent(UsersFragment.this.getActivity(), OtherProfileActivity.class);
                                otheruserintent.putExtra("otheruserid", otheruserid);
                                startActivity(otheruserintent);
                            }
                        });



                    }


                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
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



    public static class usersmodelViewHolder extends RecyclerView.ViewHolder{

            View mView;
        public usersmodelViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setUsername(String username){
            TextView name = (TextView) mView.findViewById(R.id.userName);
            name.setText(username);
        }
        public void setImageurl(String imageurl){
            ImageView userimage = (ImageView) mView.findViewById(R.id.userImage);
            Picasso.get().load(imageurl).into(userimage);
        }
    }
}
