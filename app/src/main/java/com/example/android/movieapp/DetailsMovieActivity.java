package com.example.android.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.Utilities.NetworkUtils;
import com.example.android.movieapp.adapters.AdapterReviews;
import com.example.android.movieapp.adapters.AdapterTrailers;
import com.example.android.movieapp.data.MovieContract;
import com.example.android.movieapp.data.MovieContract.MovieEntry;
import com.example.android.movieapp.data.MovieJson;
import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.model.Review;
import com.example.android.movieapp.model.Trailer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Clase para mostrar los detalles de la película seleccionada.
 */

public class DetailsMovieActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.poster)
    ImageView poster;
    @BindView(R.id.bandera)
    ImageView bandera;
    @BindView(R.id.original_title)
    TextView originalTitle;
    @BindView(R.id.fecha_lanza)
    TextView fechaLanzamiento;
    @BindView(R.id.idioma_original)
    TextView idiomaOriginal;
    @BindView(R.id.sipnosis)
    TextView sipnosis;
    @BindView(R.id.vote_average)
    RatingBar ratingBar;
    @BindView(R.id.icono_like)
    ImageView corazonFavorito;
    @BindView(R.id.recycler_reviews)
    RecyclerView recyclerReviews;
    @BindView(R.id.recycler_trailers)
    RecyclerView recyclerTrailers;

    private Movie currentMovie;
    AdapterReviews adapterReviews;
    AdapterTrailers adapterTrailers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        if (MainActivity.definirCol(getApplicationContext()) == 4) {
            getSupportActionBar().hide();
        }
        ButterKnife.bind(DetailsMovieActivity.this);

        final Context context = getApplicationContext();

        // Recogemos los datos pasados para proceder a mostrarlos de forma adecuada.
        Intent intent = getIntent();
        currentMovie = intent.getParcelableExtra("movieSelect");

        // Recogemos id Api de la película para el trabajo de fondo con las reviews y los trailers.
        String idApiMovie = String.valueOf(currentMovie.getmApiIdMovie());
        new ReviewsAsyncTask().execute(idApiMovie);
        new TrailerAsyncTask().execute(idApiMovie);

        // TITULO
        this.setTitle(currentMovie.getmTitulo());

        // FECHA
        String movieDate = currentMovie.getmFechaLanzamiento();
        // Si la fecha es nula o está vacía definiremos que no hay info, de lo contrario la fecha.
        if (movieDate.equals("") || movieDate == null) {
            fechaLanzamiento.setText(R.string.no_data);
        } else {
            fechaLanzamiento.setText(movieDate);
        }

        // VALORACIÓN
        String movieVoteAverage = currentMovie.getmPromedioVoto();
        double voto = Double.parseDouble(movieVoteAverage);
        // Promedio aproximado para el RatingBar según la valoración de la película.
        if (voto <= 3) {
            ratingBar.setRating(1);
        } else if (voto > 3 && voto <= 5.5) {
            ratingBar.setRating(2);
        } else if (voto > 5.5 && voto <= 7.5) {
            ratingBar.setRating(3);
        } else if (voto > 7.5 && voto <= 8.5) {
            ratingBar.setRating(4);
        } else if (voto > 8.5 && voto <= 10) {
            ratingBar.setRating(5);
        } else {
            ratingBar.setRating(0);
        }

        // TITULO ORIGINAL
        originalTitle.setText(currentMovie.getmTituloOriginal().toUpperCase());

        // IDIOMA ORIGINAL
        String idioma = currentMovie.getmIdiomaOriginal();
        // Según el idioma se establece una bandera del pais, por defecto si no se encuentra el pais
        // en el grupo añadido se oculta la bandera y se muestra las iniciales del pais.
        idiomaOriginal.setText(currentMovie.getmIdiomaOriginal());
        switch (idioma) {
            case "es":
                bandera.setBackgroundResource(R.drawable.spain);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            case "en":
                bandera.setBackgroundResource(R.drawable.estadosunidos);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            case "ja":
                bandera.setBackgroundResource(R.drawable.japon);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            case "it":
                bandera.setBackgroundResource(R.drawable.italia);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            case "pt":
                bandera.setBackgroundResource(R.drawable.portugal);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            case "hi":
                bandera.setBackgroundResource(R.drawable.hindu);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            case "de":
                bandera.setBackgroundResource(R.drawable.aleman);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            case "tl":
                bandera.setBackgroundResource(R.drawable.timor_oriental);
                idiomaOriginal.setVisibility(View.GONE);
                break;
            default:
                bandera.setVisibility(View.GONE);
                idiomaOriginal.setVisibility(View.VISIBLE);
        }

        // SIPNOSIS
        sipnosis.setText(currentMovie.getmSipnosis());

        // PORTADA
        Picasso.with(context)
                .load(currentMovie.getmPortada())
                .resize(getResources().getInteger(R.integer.TargetWidth), getResources().getInteger(R.integer.TargetHeight))
                .into(poster);

        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {MovieEntry.COLUMN_ID_MOVIE_API};
        return new CursorLoader(
                this,
                MovieContract.MovieEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        corazonFavorito.setBackgroundResource(R.drawable.ic_favorite_movie_border_);
        corazonFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertarFavorito();
            }
        });
        while (data.moveToNext()) {
            if (currentMovie.getmApiIdMovie() ==
                    (data.getInt(data.getColumnIndex(MovieEntry.COLUMN_ID_MOVIE_API)))) {
                corazonFavorito.setBackgroundResource(R.drawable.ic_favorite_movie);
                corazonFavorito.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quitarFavorito();
                    }
                });
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    public class TrailerAsyncTask extends AsyncTask<String, Void, Trailer[]> {

        @Override
        protected Trailer[] doInBackground(String... strings) {
            URL urlDefault = NetworkUtils.buildUrlTrailers(strings[0]);
            try {
                String jsonMovie = NetworkUtils.getResponseFromHttpUrl(urlDefault);
                return MovieJson.recogerTrailerMovie(jsonMovie);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Trailer[] trailers) {
            if (trailers != null) {
                adapterTrailers = new AdapterTrailers(getApplicationContext(), trailers);
                LinearLayoutManager layoutManager = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
                recyclerTrailers.setLayoutManager(layoutManager);
                recyclerTrailers.setAdapter(adapterTrailers);
            }
        }
    }

    public class ReviewsAsyncTask extends AsyncTask<String, Void, Review[]> {

        @Override
        protected Review[] doInBackground(String... strings) {
            URL urlDefault = NetworkUtils.buildUrlReviews(strings[0]);
            try {
                String jsonMovie = NetworkUtils.getResponseFromHttpUrl(urlDefault);
                return MovieJson.recogerReviewsMovie(jsonMovie);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Review[] reviews) {
            if (reviews != null) {
                adapterReviews = new AdapterReviews(getApplicationContext(), reviews);
                LinearLayoutManager layoutManager = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false);
                recyclerReviews.setLayoutManager(layoutManager);
                recyclerReviews.setAdapter(adapterReviews);
            }
        }
    }

    private void insertarFavorito() {

        int idMovieApi = currentMovie.getmApiIdMovie();
        String titulo = currentMovie.getmTitulo();
        String tituloOriginal = currentMovie.getmTituloOriginal();
        String portada = currentMovie.getmPortada();
        String contraPortada = currentMovie.getmContraPortada();
        String promedio = currentMovie.getmPromedioVoto();
        String idiomaOriginal = currentMovie.getmIdiomaOriginal();
        String sipnosis = currentMovie.getmSipnosis();
        String fecha = currentMovie.getmFechaLanzamiento();


        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_ID_MOVIE_API, idMovieApi);
        values.put(MovieEntry.COLUMN_TITLE_MOVIE, titulo);
        values.put(MovieEntry.COLUMN_TITULO_ORIGINAL, tituloOriginal);
        values.put(MovieEntry.COLUMN_PORTADA, portada);
        values.put(MovieEntry.COLUMN_CONTRAPORTADA, contraPortada);
        values.put(MovieEntry.COLUMN_PROMEDIO_VOTO, promedio);
        values.put(MovieEntry.COLUMN_IDIOMA_ORIGINAL, idiomaOriginal);
        values.put(MovieEntry.COLUMN_SIPNOSIS, sipnosis);
        values.put(MovieEntry.COLUMN_FECHA_LANZAMIENTO, fecha);

        // Inserta los valores de contenido a través de ContentResolver
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);

        if (uri != null) {
            Toast.makeText(getBaseContext(),
                    String.format(getString(R.string.incluidoFav), titulo),
                    Toast.LENGTH_LONG).show();
        }
    }


    private void quitarFavorito() {
        String titulo = currentMovie.getmTitulo();
        int idMovieApi = currentMovie.getmApiIdMovie();
        String projection = MovieEntry.COLUMN_ID_MOVIE_API + "=" + String.valueOf(idMovieApi);
        int rowsDeleted = getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                projection,
                null);
        getSupportLoaderManager().restartLoader(1, null, this);
        Log.i(getString(R.string.logDelete), String.valueOf(rowsDeleted));
        Toast.makeText(
                this, String.format(getString(R.string.eliminadoFav), titulo),
                Toast.LENGTH_SHORT).show();
    }
}
