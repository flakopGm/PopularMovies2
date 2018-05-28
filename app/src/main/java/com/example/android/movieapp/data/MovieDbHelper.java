package com.example.android.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movieapp.data.MovieContract.MovieEntry;

/**
 * Created by Joni on 26/03/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favoritesMovies.db";
    public static final int DATABASE_VERSION =1;

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COLUMN_ID_MOVIE_API + " INTEGER NOT NULL, "
                + MovieEntry.COLUMN_TITLE_MOVIE + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_TITULO_ORIGINAL + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_PORTADA + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_CONTRAPORTADA + " TEXT, "
                + MovieEntry.COLUMN_PROMEDIO_VOTO + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_IDIOMA_ORIGINAL + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_SIPNOSIS + " TEXT NOT NULL, "
                + MovieEntry.COLUMN_FECHA_LANZAMIENTO + " TEXT DEFAULT 'NO DATA'"
                + ");";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
