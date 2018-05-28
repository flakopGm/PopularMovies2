package com.example.android.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.model.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.ReviewViewHolder> {

    private final Context mContext;
    private final Review[] reviews;

    public AdapterReviews(Context mContext, Review[] reviews) {
        this.mContext = mContext;
        this.reviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.disposicion_review,
                parent,
                false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {
        String coment = reviews[position].getmComentario();
        if (!coment.isEmpty()) {
            if (coment.length() < R.integer.MaxChar) {
                holder.comentario.setText(coment);
            } else {
                String mostrado = coment.substring(R.integer.Mini, R.integer.MaxChar);
                holder.comentario.setText(mostrado + "\n...");
            }
        } else {
            holder.comentario.setText(R.string.nada_por_ahora);
        }


        //  Escucha para el botón info, redirige a la web en cuestión.
        holder.botonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlReview = reviews[position].getmUrlComentario();
                Uri uriReview = Uri.parse(urlReview);
                Intent intent = new Intent(Intent.ACTION_VIEW, uriReview);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.reviews.length;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comentario)
        TextView comentario;
        @BindView(R.id.boton_info)
        Button botonInfo;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
