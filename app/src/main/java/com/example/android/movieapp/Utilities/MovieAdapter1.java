package com.example.android.movieapp.Utilities;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Joni on 08/03/2018.
 */

public class MovieAdapter1 extends BaseAdapter {

    private Context mContext;
    private Movie[] movies;
    private ImageView portada;

    public MovieAdapter1(Context mContext, Movie[] movies) {
        this.mContext = mContext;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public Object getItem(int position) {
        return movies[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
             portada = new ImageView(mContext);
             portada.setAdjustViewBounds(true);
        } else {
            portada = (ImageView) convertView;
        }
        Picasso.with(mContext)
                .load(movies[position].getmPortada())
                .resize(mContext.getResources().getInteger(R.integer.TargetWidth),
                        mContext.getResources().getInteger(R.integer.TargetHeight))
                .centerCrop()
                .error(R.drawable.no_encontrado)
                .placeholder(R.drawable.cargandomovie)
                .into(portada);

        return portada;
    }
}
