package com.example.android.movieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable{

    private String mComentario;
    private String mUrlComentario;

    public Review(){

    }

    public Review(String mComentario, String mUrlComentario) {
        this.mComentario = mComentario;
        this.mUrlComentario = mUrlComentario;
    }

    private Review(Parcel in){

        mComentario = in.readString();
        mUrlComentario = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getmComentario() {
        return mComentario;
    }

    public void setmComentario(String mComentario) {
        this.mComentario = mComentario;
    }

    public String getmUrlComentario() {
        return mUrlComentario;
    }

    public void setmUrlComentario(String mUrlComentario) {
        this.mUrlComentario = mUrlComentario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mComentario);
        dest.writeString(mUrlComentario);
    }
}
