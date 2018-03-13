package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Clase para mostrar los detalles de la película seleccionada.
 */

public class DetailsMovieActivity extends AppCompatActivity {

    private Context context;
    @BindView(R.id.poster) ImageView poster;
    @BindView(R.id.bandera) ImageView bandera;
    @BindView(R.id.original_title) TextView originalTitle;
    @BindView(R.id.fecha_lanza) TextView fechaLanzamiento;
    @BindView(R.id.idioma_original) TextView idiomaOriginal;
    @BindView(R.id.sipnosis) TextView sipnosis;
    @BindView(R.id.vote_average)RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        ButterKnife.bind(DetailsMovieActivity.this);

        context = getApplicationContext();

        // Recogemos los datos pasados para proceder a mostrarlos de forma adecuada.
        Intent intent = getIntent();
        String moviePoster = intent.getStringExtra("poster");
        String movieTitleOriginal = intent.getStringExtra("title_original");
        String movieTitle = intent.getStringExtra("title");
        String movieDate = intent.getStringExtra("fecha");
        String movieOverview = intent.getStringExtra("sipnosis");
        String movieVoteAverage = intent.getStringExtra("promedio");
        double voto = Double.parseDouble(movieVoteAverage);
        String idioma = intent.getStringExtra("idioma");

        this.setTitle(movieTitle);
        originalTitle.setText(movieTitleOriginal.toUpperCase());

        // Si la fecha es nula o está vacía definiremos que no hay info, de lo contrario la fecha.
        if (movieDate.equals("") || movieDate == null) {
            fechaLanzamiento.setText("NO DATA");
        } else {
            fechaLanzamiento.setText(movieDate);
        }
        // Promedio aproximado para el RatingBar según la valoración de la película.
        if (voto <= 3.5) {
            ratingBar.setRating(1.5f);
        } else if (voto > 3.5 && voto <= 5.5) {
            ratingBar.setRating(2.5f);
        } else if (voto > 5.5 && voto <= 8) {
            ratingBar.setRating(3.5f);
        } else if (voto > 8 && voto <= 9) {
            ratingBar.setRating(4);
        } else if (voto > 8.5 && voto <= 10) {
            ratingBar.setRating(5);
        } else {
            ratingBar.setRating(0);
        }
        // Según el idioma se establece una bandera del pais, por defecto si no se encuentra el pais
        // en el grupo añadido se oculta la bandera y se muestra las iniciales del pais.
        idiomaOriginal.setText(idioma);
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
            default:
                bandera.setVisibility(View.GONE);
                idiomaOriginal.setVisibility(View.VISIBLE);
        }

        sipnosis.setText(movieOverview);

        // Definimos la imagen redimensionada.
        Picasso.with(context)
                .load(moviePoster)
                .resize(873, 600)

                .into(poster);
    }
}
