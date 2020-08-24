package com.anicolle.ju.anicolle;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;


        //item click

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });


        //item long click

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });

    }


    public void setDetails (Context ctx, String title, String newsimage, String content


                            ){
        TextView mTitlename = mView.findViewById(R.id.newstitle);
        ImageView mTitleimage = mView.findViewById(R.id.newsimage);

              ;
        mTitlename.setText(title);
        Picasso.get().load(newsimage).into(mTitleimage);




    }

    private NewsViewHolder.ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(NewsViewHolder.ClickListener clickListener){

        mClickListener = clickListener;

    }
}
