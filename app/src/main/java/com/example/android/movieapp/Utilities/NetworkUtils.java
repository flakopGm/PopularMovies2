package com.example.android.movieapp.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Joni on 07/03/2018.
 * Clase de ayuda para construir la URL de consulta y para la solicitud de conexión.
 */

public class NetworkUtils {

    private static final String API_MOVIE_URL = "http://api.themoviedb.org/3/movie?";
    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY = "4926e96a132309b56560f4f86df98d4d";

    // Recibimos un String con el órden y creamos la URL de la misma.
    public static URL buildUrl(String ordenVisualizacion) {
        Uri.Builder builder = Uri.parse(API_MOVIE_URL).buildUpon();
        builder.appendPath(ordenVisualizacion);
        builder.appendQueryParameter(API_KEY_PARAM, API_KEY);
        builder.build();
        URL url = null;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    // Este método devuelve el resultado completo de la respuesta HTTP.
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
