package com.example.android.movieapp.model;

/**
 * Created by Joni *FLKPGM* on 07/03/2018.
 * <p>
 * Clase Base de una pelicula, contiene su título, título original, valoración de usuario, idioma
 * original, la sipnosis y su fecha de lanzamiento.
 */

public class Movie {
    private String mTituloOriginal;
    private String mTitulo;
    private String mPortada;
    private String mPromedioVoto;
    private String mIdiomaOriginal;
    private String mSipnosis;
    private String mFechaLanzamiento;

    public Movie() {
    }

    public Movie(String titulo, String portada, String promedio, String idioma,
                 String tituloOriginal, String sipnosis, String fecha) {
        this.mTitulo = titulo;
        this.mPortada = portada;
        this.mPromedioVoto = promedio;
        this.mIdiomaOriginal = idioma;
        this.mTituloOriginal = tituloOriginal;
        this.mSipnosis = sipnosis;
        this.mFechaLanzamiento = fecha;
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
}
