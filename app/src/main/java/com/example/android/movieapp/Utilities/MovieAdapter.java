package com.example.android.movieapp.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.android.movieapp.R;
import com.example.android.movieapp.model.Movie;
import com.squareup.picasso.Picasso;


/**
 * Created by Joni on 07/03/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Movie[] movies;
    private final Context mContext;
    private ImageView portada;

    public MovieAdapter(Context context, Movie[] movies) {
        this.mContext = context;
        this.movies = movies;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int idLayoutDisposicionMovies = R.layout.dispocion_movie;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idLayoutDisposicionMovies, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies[position];
        Picasso.with(mContext)
                .load(movie.getmPortada())
                .resize(mContext.getResources().getInteger(R.integer.TargetWidth),
                        mContext.getResources().getInteger(R.integer.TargetHeight))
                .error(R.drawable.ic_launcher_foreground)
                .placeholder(R.drawable.ic_launcher_background)
                .into(portada);
    }

    @Override
    public int getItemCount() {
        return movies.length;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

        public MovieViewHolder(View itemView) {
            super(itemView);
            portada = (ImageView) itemView.findViewById(R.id.portada);
        }
    }
}
