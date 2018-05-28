package com.example.android.movieapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Joni on 26/03/2018.
 */

public class MovieContract {

    /*  1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
    */

    public static final String AUTHORITY = "com.example.android.movieapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "favoritesMovies";

    private MovieContract(){
    }

    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MOVIE).build();

        public static final String TABLE_NAME ="favoritesMovies" ;
        public static final String COLUMN_ID_MOVIE_API = "idMovieApi";
        public static final String COLUMN_TITLE_MOVIE = "titleMovie";
        public static final String COLUMN_TITULO_ORIGINAL = "titleOriginalMovie";
        public static final String COLUMN_PORTADA = "portadaMovie";
        public static final String COLUMN_CONTRAPORTADA = "contraPortadaMovie";
        public static final String COLUMN_PROMEDIO_VOTO = "promedioVotoMovie";
        public static final String COLUMN_IDIOMA_ORIGINAL = "idiomaOriginalMovie";
        public static final String COLUMN_SIPNOSIS = "sipnosisMovie";
        public static final String COLUMN_FECHA_LANZAMIENTO = "fechaMovie";

        /**
         * Construye una {@link Uri} para el {@link #} solicitado.
         */
        public static Uri construirUriMovie(String idMovie) {
            return CONTENT_URI.buildUpon().appendEncodedPath(idMovie).build();
        }
    }
}
