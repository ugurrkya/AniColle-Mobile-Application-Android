package com.anicolle.ju.anicolle;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;

    public ViewHolder(@NonNull View itemView) {
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


    public void setDetails (Context ctx, String animename,String animephoto, String producer, String broadcast,
                       String country, String currentep,String duration, String genre,String plannedep,String relation,
                            String source, String season,String studio,String type


                            ){
        TextView mAnimeNameView = mView.findViewById(R.id.animeName);
        ImageView mAnimeImage = mView.findViewById(R.id.animeImage);

              ;
        mAnimeNameView.setText(animename);
        Picasso.get().load(animephoto).into(mAnimeImage);




    }

    private ViewHolder.ClickListener mClickListener;


    //interface to send callbacks

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){

        mClickListener = clickListener;

    }
}
