package com.example.android.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String idiomaTrailer;
    private String keyTrailer;
    private String idTrailer;
    private String nombreTrailer;
    private String miniaturaTrailer;
    private String alojamientoTrailer;
    private int sizeScreenTrailer;

    public Trailer() {
    }

    public Trailer(String idiomaTrailer, String keyTrailer, String nombreTrailer,
                   String id, String miniatura, String alojamientoTrailer, int sizeScreenTrailer) {
        this.idiomaTrailer = idiomaTrailer;
        this.keyTrailer = keyTrailer;
        this.idTrailer = id;
        this.nombreTrailer = nombreTrailer;
        this.miniaturaTrailer = miniatura;
        this.alojamientoTrailer = alojamientoTrailer;
        this.sizeScreenTrailer = sizeScreenTrailer;
    }

    protected Trailer(Parcel in) {
        idiomaTrailer = in.readString();
        keyTrailer = in.readString();
        idTrailer = in.readString();
        nombreTrailer = in.readString();
        miniaturaTrailer = in.readString();
        alojamientoTrailer = in.readString();
        sizeScreenTrailer = in.readInt();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getIdiomaTrailer() {
        return idiomaTrailer;
    }

    public void setIdiomaTrailer(String idiomaTrailer) {
        this.idiomaTrailer = idiomaTrailer;
    }

    public String getKeyTrailer() {
        return keyTrailer;
    }

    public void setKeyTrailer(String keyTrailer) {
        this.keyTrailer = keyTrailer;
    }

    public String getIdTrailer() {
        return idTrailer;
    }

    public void setIdTrailer(String idTrailer) {
        this.idTrailer = idTrailer;
    }

    public String getNombreTrailer() {
        return nombreTrailer;
    }

    public void setNombreTrailer(String nombreTrailer) {
        this.nombreTrailer = nombreTrailer;
    }

    public String getMiniaturaTrailer() {
        return miniaturaTrailer;
    }

    public void setMiniaturaTrailer(String miniaturaTrailer) {
        this.miniaturaTrailer = miniaturaTrailer;
    }

    public String getAlojamientoTrailer() {
        return alojamientoTrailer;
    }

    public void setAlojamientoTrailer(String alojamientoTrailer) {
        this.alojamientoTrailer = alojamientoTrailer;
    }

    public int getSizeScreenTrailer() {
        return sizeScreenTrailer;
    }

    public void setSizeScreenTrailer(int sizeScreenTrailer) {
        this.sizeScreenTrailer = sizeScreenTrailer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idiomaTrailer);
        dest.writeString(keyTrailer);
        dest.writeString(idTrailer);
        dest.writeString(nombreTrailer);
        dest.writeString(miniaturaTrailer);
        dest.writeString(alojamientoTrailer);
        dest.writeInt(sizeScreenTrailer);
    }
}
