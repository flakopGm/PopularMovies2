package com.example.android.movieapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.android.movieapp.data.MovieContract.MovieEntry.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {

    public final static int TABLA_COMPLETA = 100;
    public final static int ID_TABLA = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIE, TABLA_COMPLETA);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIE + "/#", ID_TABLA);

        return uriMatcher;
    }

    private MovieDbHelper movieDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = movieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case TABLA_COMPLETA:
                cursor = database.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("No es posible realizar la operación " + uri.toString());
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri uriMovie;
        switch (match) {
            case TABLA_COMPLETA:
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    uriMovie = ContentUris.withAppendedId(
                            MovieContract.MovieEntry.CONTENT_URI,
                            id);
                } else {
                    throw new android.database.SQLException("Error al insertar la fila en " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Uri desconocido: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return uriMovie;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int elementoBorrado;
        switch (match) {
            case TABLA_COMPLETA:
                // Eliminar todas las filas que coinciden con los args de selección y selección.
                elementoBorrado = database.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            case ID_TABLA:
                // Eliminar una sola fila dada por el ID en el URI.
                selection = MovieContract.MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                elementoBorrado = database.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("No se puede con esa Uri -> " + uri);
        }
        //  Notificación de cambios.
        if (elementoBorrado != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return elementoBorrado;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
