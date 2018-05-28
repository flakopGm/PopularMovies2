package com.example.android.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.android.movieapp.DetailsMovieActivity;
import com.example.android.movieapp.R;
import com.example.android.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolderMovie> {

    private final Context mContext;
    private final Movie[] movies;


    public MovieListAdapter(Context context, Movie[] listaPeliculas) {
        this.mContext = context;
        this.movies = listaPeliculas;
    }

    @Override
    public ViewHolderMovie onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.disposicion_movie,
                parent,
                false
        );
        return new ViewHolderMovie(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderMovie holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailsMovieActivity.class);

                Movie currentMovie = movies[position];
                intent.putExtra(mContext.getString(R.string.intent_current_movie), currentMovie);

                mContext.startActivity(intent);
            }
        });

        Picasso.with(mContext)
                .load(this.movies[position].getmPortada())
                .error(R.drawable.noimage9)
                .placeholder(R.drawable.cargandomovie)
                .into(holder.portadaMovie);

        // VALORACIÓN
        String movieVoteAverage = movies[position].getmPromedioVoto();
        double voto = Double.parseDouble(movieVoteAverage);
        // Promedio aproximado para el RatingBar según la valoración de la película.
        if (voto <= 3) {
            holder.ratingMovie.setRating(1);
        } else if (voto > 3 && voto <= 5.5) {
            holder.ratingMovie.setRating(2);
        } else if (voto > 5.5 && voto <= 7.5) {
            holder.ratingMovie.setRating(3);
        } else if (voto > 7.5 && voto <= 8.5) {
            holder.ratingMovie.setRating(4);
        } else if (voto > 8.5 && voto <= 10) {
            holder.ratingMovie.setRating(5);
        } else {
            holder.ratingMovie.setRating(0);
        }
    }

    @Override
    public int getItemCount() {
        return this.movies.length;
    }


    class ViewHolderMovie extends RecyclerView.ViewHolder {

        @BindView(R.id.portada_movie)
        ImageView portadaMovie;
        @BindView(R.id.rating_movie)
        RatingBar ratingMovie;

        public ViewHolderMovie(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
