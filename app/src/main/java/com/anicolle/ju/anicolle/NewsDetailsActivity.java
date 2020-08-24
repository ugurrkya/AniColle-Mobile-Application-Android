package com.anicolle.ju.anicolle;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NewsDetailsActivity extends AppCompatActivity {

    TextView mTitlename,mContentname;

    ImageView newsPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);


        ActionBar actionBar = getSupportActionBar();



        mTitlename = (TextView)findViewById(R.id.dttitlename);
        mContentname = (TextView) findViewById(R.id.dtcontent);

        newsPhoto = (ImageView)findViewById(R.id.dtnewsImage);




        //get data from intent
        String img = getIntent().getStringExtra("newsimage");
        String tit = getIntent().getStringExtra("title");
        String cont = getIntent().getStringExtra("content");



        mTitlename.setText(tit);
        mContentname.setText(cont);
        Picasso.get().load(img).into(newsPhoto);

    }


}
