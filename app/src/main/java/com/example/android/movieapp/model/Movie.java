package com.example.android.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Joni *FLKPGM* on 07/03/2018.
 * <p>
 * Clase Base de una pelicula, contiene su título, título original, valoración de usuario, idioma
 * original, la sipnosis y su fecha de lanzamiento.
 * Implementa @Parcelable.
 */

public class Movie implements Parcelable {

    private int id;
    private int mApiIdMovie;
    private String mTituloOriginal;
    private String mTitulo;
    private String mPortada;
    private String mContraPortada;
    private String mPromedioVoto;
    private String mIdiomaOriginal;
    private String mSipnosis;
    private String mFechaLanzamiento;

    public Movie() {
    }

    public Movie(int id, int apiId, String tituloOriginal, String titulo, String portada, String contraportada,
                 String promedio, String idioma, String sipnosis, String fecha) {
        this.id = id;
        this.mApiIdMovie = apiId;
        this.mTitulo = titulo;
        this.mPortada = portada;
        this.mContraPortada = portada;
        this.mPromedioVoto = promedio;
        this.mIdiomaOriginal = idioma;
        this.mTituloOriginal = tituloOriginal;
        this.mSipnosis = sipnosis;
        this.mFechaLanzamiento = fecha;
    }

    private Movie(Parcel in) {
        id = in.readInt();
        mApiIdMovie = in.readInt();
        mTitulo = in.readString();
        mPortada = in.readString();
        mContraPortada = in.readString();
        mPromedioVoto = in.readString();
        mIdiomaOriginal = in.readString();
        mTituloOriginal = in.readString();
        mSipnosis = in.readString();
        mFechaLanzamiento = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getmApiIdMovie() {
        return mApiIdMovie;
    }

    public void setmApiIdMovie(int mApiIdMovie) {
        this.mApiIdMovie = mApiIdMovie;
    }

    public String getmTitulo() {
        return mTitulo;
    }

    public void setmTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }

    public String getmPortada() {
        return mPortada;
    }

    public void setmPortada(String mPortada) {
        this.mPortada = mPortada;
    }

    public String getmContraPortada() {
        return mContraPortada;
    }

    public void setmContraPortada(String mContraPortada) {
        this.mContraPortada = mContraPortada;
    }

    public String getmPromedioVoto() {
        return mPromedioVoto;
    }

    public void setmPromedioVoto(String mPromedioVoto) {
        this.mPromedioVoto = mPromedioVoto;
    }

    public String getmIdiomaOriginal() {
        return mIdiomaOriginal;
    }

    public void setmIdiomaOriginal(String mIdiomaOriginal) {
        this.mIdiomaOriginal = mIdiomaOriginal;
    }

    public String getmTituloOriginal() {
        return mTituloOriginal;
    }

    public void setmTituloOriginal(String mTituloOriginal) {
        this.mTituloOriginal = mTituloOriginal;
    }

    public String getmSipnosis() {
        return mSipnosis;
    }

    public void setmSipnosis(String mSipnosis) {
        this.mSipnosis = mSipnosis;
    }

    public String getmFechaLanzamiento() {
        return mFechaLanzamiento;
    }

    public void setmFechaLanzamiento(String mFechaLanzamiento) {
        this.mFechaLanzamiento = mFechaLanzamiento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(mApiIdMovie);
        dest.writeString(mTitulo);
        dest.writeString(mPortada);
        dest.writeString(mContraPortada);
        dest.writeString(mPromedioVoto);
        dest.writeString(mIdiomaOriginal);
        dest.writeString(mTituloOriginal);
        dest.writeString(mSipnosis);
        dest.writeString(mFechaLanzamiento);
    }
}
