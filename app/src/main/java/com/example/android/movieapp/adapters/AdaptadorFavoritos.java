package com.example.android.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.movieapp.DetailsMovieActivity;
import com.example.android.movieapp.R;
import com.example.android.movieapp.data.MovieContract;
import com.example.android.movieapp.data.MovieContract.MovieEntry;
import com.example.android.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdaptadorFavoritos extends RecyclerView.Adapter<AdaptadorFavoritos.MovieViewHolder> {

    private Context mContext;
    private Cursor mCursor;

    public AdaptadorFavoritos(Context context) {
        this.mContext = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.disposicion_favoritos,
                parent,
                false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        //  Definimos un id con número 15 que se pasa como referencia para la eliminación.
        final int idMovie = mCursor.getInt(mCursor.getColumnIndex(MovieEntry._ID));
        holder.itemView.setTag(R.integer.TagViewHolder, idMovie);
        //  Si el número de elementos es mayor que 0 podemos ocultar las instrucciones o empty view del
        //  recycler y proceder a mostrar el contenido.
        if (getItemCount() > 0) {
            holder.relativeFav.setVisibility(View.VISIBLE);
            holder.tituloFav.setText(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE_MOVIE)));
            Picasso.with(mContext)
                    .load(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_PORTADA)))
                    .error(R.drawable.noimage9)
                    .placeholder(R.drawable.cargandomovie)
                    .into(holder.portada);

            Picasso.with(mContext)
                    .load(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_CONTRAPORTADA)))
                    .error(R.drawable.moviefavnoencontrado9path)
                    .placeholder(R.drawable.cargandomovie)
                    .into(holder.contraPortada);

            //  Escucha para el botón de ficha de la película.
            holder.botonFicha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(position);

                    int idMovieApi = mCursor.getInt(mCursor.getColumnIndex(MovieEntry.COLUMN_ID_MOVIE_API));
                    String tituloOriginal = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_TITULO_ORIGINAL));
                    String titulo = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_TITLE_MOVIE));
                    String portada = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_PORTADA));
                    String contraPortada = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_CONTRAPORTADA));
                    String promedio = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_PROMEDIO_VOTO));
                    String idioma = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_IDIOMA_ORIGINAL));
                    String sipnosis = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_SIPNOSIS));
                    String fecha = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_FECHA_LANZAMIENTO));

                    Movie currentMovie = new Movie(idMovie, idMovieApi, tituloOriginal, titulo, portada,
                            contraPortada, promedio, idioma, sipnosis, fecha);
                    Intent intent = new Intent(mContext, DetailsMovieActivity.class);
                    intent.putExtra(mContext.getString(R.string.intent_current_movie), currentMovie);

                    mContext.startActivity(intent);
                }
            });

            //  Escucha para el botón de más info. (Realiza una búsqueda en Google del título actual).
            holder.botonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(position);
                    String fav = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_TITLE_MOVIE));
                    Uri uri = Uri.parse(mContext.getString(R.string.google_search) + fav);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(intent);
                }
            });

            //  Escucha para el botón de compartir película.
            holder.shareMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCursor.moveToPosition(position);

                    String base = mContext.getString(R.string.youtube_search);
                    String titulo = mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_TITLE_MOVIE)).trim();
                    String tituloSinEspacio = titulo.replace(" ", "");

                    Uri uri = Uri.parse(base + tituloSinEspacio);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(
                            Intent.EXTRA_TEXT,
                            (mContext.getString(
                                    R.string.intent_share) + "\n\n" + titulo + "\n\n") + uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.promo));
                    mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.compartir_peli)));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();
        return 0;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.portada_movie_fav)
        ImageView portada;
        @BindView(R.id.contraportada)
        ImageView contraPortada;
        @BindView(R.id.titulo_favorito)
        TextView tituloFav;
        @BindView(R.id.relative_fav)
        RelativeLayout relativeFav;
        @BindView(R.id.boton_ficha)
        Button botonFicha;
        @BindView(R.id.mas_info)
        Button botonInfo;
        @BindView(R.id.compartir)
        Button shareMovie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Ayuda para actualizar el cursor.
    public void swapCursor(Cursor nuevoCursor) {
        if (nuevoCursor != null) {
            mCursor = nuevoCursor;
            notifyDataSetChanged();
        }
    }
}
