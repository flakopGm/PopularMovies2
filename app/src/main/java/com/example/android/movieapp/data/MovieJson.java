package com.example.android.movieapp.data;

import com.example.android.movieapp.model.Movie;
import com.example.android.movieapp.model.Review;
import com.example.android.movieapp.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public final class MovieJson {

    public static Movie[] recogerInfoMovie(String movieJson)
            throws JSONException {
        final String ID_KEY = "id";
        final String BUSQUEDA_KEY = "results";
        final String ORIGINAL_TITLE_KEY = "original_title";
        final String TITULO_KEY = "title";
        final String POSTER_KEY = "poster_path";
        final String CONTRAPORTADA_KEY = "backdrop_path";
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
            movies[x].setmApiIdMovie(movieInfo.getInt(ID_KEY));
            movies[x].setmTituloOriginal(movieInfo.getString(ORIGINAL_TITLE_KEY));
            movies[x].setmTitulo(movieInfo.getString(TITULO_KEY));
        // Guardamos la parte final de la url de imagen y la guardamos construyendo la url real.
            String p2UrlPortada = movieInfo.getString(POSTER_KEY);
            String p2UrlContrPortda = movieInfo.getString(CONTRAPORTADA_KEY);
            movies[x].setmPortada(montarUrlImagen(p2UrlPortada));
            movies[x].setmContraPortada(montarUrlImagen(p2UrlContrPortda));
            movies[x].setmPromedioVoto(movieInfo.getString(VOTE_AVERAGE_KEY));
            movies[x].setmSipnosis(movieInfo.getString(OVERVIEW_KEY));
            movies[x].setmFechaLanzamiento(movieInfo.getString(RELEASE_DATE_KEY));
            movies[x].setmIdiomaOriginal(movieInfo.getString(LANGUAGE_KEY));
        }
        return movies;
    }

    public static Review[] recogerReviewsMovie(String reviewJson) throws JSONException {
        final String BUSQUEDA_KEY = "results";
        final String COMENTARIO_KEY = "content";
        final String URL_COMEN_KEY = "url";

        Review[] reviews;

        JSONObject comentariosJson = new JSONObject(reviewJson);
        JSONArray arrayComentarios = comentariosJson.getJSONArray(BUSQUEDA_KEY);

        reviews = new Review[arrayComentarios.length()];

        for (int x = 0; x < arrayComentarios.length(); x++) {
            reviews[x] = new Review();
            JSONObject reviewMovie = arrayComentarios.getJSONObject(x);
            reviews[x].setmComentario(reviewMovie.getString(COMENTARIO_KEY));
            reviews[x].setmUrlComentario(reviewMovie.getString(URL_COMEN_KEY));
        }
        return reviews;
    }

    // Recibimos la parte final de la url del poster de la película y creamos su url real.
    private static String montarUrlImagen(String finalUrl) {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + finalUrl;
    }

    public static Trailer[] recogerTrailerMovie(String jsonMovie) throws JSONException {

        final String BUSQUEDA_KEY = "results";
        final String ID_KEY = "id";
        final String NOMBRE_KEY = "name";
        final String IDIOMA_KEY = "iso_639_1";
        final String VIDEO_KEY = "key";
        final String SITE_KEY = "site";
        final String SIZE_SCREEN_KEY = "size";

        Trailer[] trailers;

        JSONObject trailerMovieJson = new JSONObject(jsonMovie);
        JSONArray arrayTrailers = trailerMovieJson.getJSONArray(BUSQUEDA_KEY);

        trailers = new Trailer[arrayTrailers.length()];

        for (int x = 0; x < arrayTrailers.length(); x++) {
            trailers[x] = new Trailer();


            JSONObject trailer = arrayTrailers.getJSONObject(x);
            String trailerKey = trailer.getString(VIDEO_KEY);
            trailers[x].setIdTrailer(trailer.getString(ID_KEY));
            trailers[x].setNombreTrailer(trailer.getString(NOMBRE_KEY));
            trailers[x].setMiniaturaTrailer(montarMiniaturaVideo(trailerKey));
            trailers[x].setIdiomaTrailer(trailer.getString(IDIOMA_KEY));
            trailers[x].setKeyTrailer(trailerKey);
            trailers[x].setAlojamientoTrailer(trailer.getString(SITE_KEY));
            trailers[x].setSizeScreenTrailer(trailer.getInt(SIZE_SCREEN_KEY));
        }
        return trailers;
    }

    private static String montarMiniaturaVideo(String idTrailer) {
        final String BASE_URL_MINIATURA = "https://img.youtube.com/vi/";
        final String[] finalesUrl = {"/0.jpg","/1.jpg","/2.jpg"};
        String FINAL_URL = "";

        for(int x=0; x<finalesUrl.length;x++){
            int el = (int)(Math.random()*3);
            FINAL_URL = finalesUrl[el];
        }
        return BASE_URL_MINIATURA + idTrailer + FINAL_URL;
    }
}
