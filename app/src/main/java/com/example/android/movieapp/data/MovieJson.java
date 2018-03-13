package com.example.android.movieapp.data;

import com.example.android.movieapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joni *FLKPGM* on 07/03/2018.
 */

public final class MovieJson {

    public static Movie[] recogerDatosMovieDeJson(String movieJson)
            throws JSONException {

        final String BUSQUEDA_KEY = "results";
        final String ORIGINAL_TITLE_KEY = "original_title";
        final String TITULO_KEY = "title";
        final String POSTER_KEY = "poster_path";
        final String VOTE_AVERAGE_KEY = "vote_average";
        final String OVERVIEW_KEY = "overview";
        final String RELEASE_DATE_KEY = "release_date";
        final String LANGUAGE_KEY = "original_language";

        // String array para la selección de peliculas.
        Movie[] movies;

        JSONObject carteleraJson = new JSONObject(movieJson);
        JSONArray movieArray = carteleraJson.getJSONArray(BUSQUEDA_KEY);

        // Creamos una matriz de objetos de película donde almacenamos los datos de la cadena
        // JSON capturada.
        movies = new Movie[movieArray.length()];

        // Recorremos al completo el array de películas y obtenemos los datos de cada una de ellas.
        for (int x = 0; x < movieArray.length(); x++) {
            movies[x] = new Movie();
            JSONObject movieInfo = movieArray.getJSONObject(x);
            movies[x].setmTituloOriginal(movieInfo.getString(ORIGINAL_TITLE_KEY));
            movies[x].setmTitulo(movieInfo.getString(TITULO_KEY));

            // Guardamos la parte final de la url de imagen y la guardamos construyendo la url real.
            String p2UrlPortada = movieInfo.getString(POSTER_KEY);
            movies[x].setmPortada(montarUrlImagen(p2UrlPortada));
            movies[x].setmPromedioVoto(movieInfo.getString(VOTE_AVERAGE_KEY));
            movies[x].setmSipnosis(movieInfo.getString(OVERVIEW_KEY));
            movies[x].setmFechaLanzamiento(movieInfo.getString(RELEASE_DATE_KEY));
            movies[x].setmIdiomaOriginal(movieInfo.getString(LANGUAGE_KEY));

        }
        return movies;
    }


      // Recibimos la parte final de la url del poster de la película y creamos su url real.
    public static String montarUrlImagen(String finalUrl) {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + finalUrl;
    }
}
