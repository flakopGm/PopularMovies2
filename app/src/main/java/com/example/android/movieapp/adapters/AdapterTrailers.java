package com.example.android.movieapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.model.Trailer;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterTrailers extends RecyclerView.Adapter<AdapterTrailers.TrailerViewHolder> {

    private Context mContext;
    private Trailer[] trailers;

    public AdapterTrailers(Context contexto, Trailer[] trailers) {
        this.mContext = contexto;
        this.trailers = trailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.disposicion_trailer,
                parent,
                false
        );
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        // Definimos los diferentes valores. nombre, idioma, fecha de lanzamiento, etc

        holder.nombreTrailer.setText(trailers[position].getNombreTrailer());

        // Según el idioma se define su bandera (hay una selección inicial...)
        String idioma = trailers[position].getIdiomaTrailer();
        switch (idioma) {
            case "es":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.spain);
                break;
            case "en":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.estadosunidos);
                break;
            case "ja":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.japon);
                break;
            case "it":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.italia);
                break;
            case "pt":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.portugal);
                break;
            case "hi":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.hindu);
                break;
            case "de":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.aleman);
                break;
            case "tl":
                holder.idiomaTrailer.setBackgroundResource(R.drawable.timor_oriental);
                break;
            default:
                holder.idiomaTrailer.setVisibility(View.GONE);
        }

        String site = trailers[position].getAlojamientoTrailer();
        String youtube = "YouTube";
        if (site.equalsIgnoreCase(youtube) || site.equalsIgnoreCase("youtube")) {
            holder.siteTrailer.setBackgroundResource(R.drawable.youtube);
        } else {
            holder.siteTrailer.setVisibility(View.GONE);
        }

        int size = trailers[position].getSizeScreenTrailer();
        if (size == 1080) {
            holder.sizeTrailer.setBackgroundResource(R.drawable.ic_hd48dp);
        } else {
            holder.siteTrailer.setVisibility(View.GONE);
        }

        Picasso.with(mContext)
                .load(trailers[position].getMiniaturaTrailer())
                .error(R.drawable.ic_play_arrow_black_48dp)
                .placeholder(R.drawable.cargandomovie)
                .into(holder.portadaTrailer);

        // Escucha para la reprodución de los trailers.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baseYoutube = "https://www.youtube.com/watch?v=";
                String keyTrailer = trailers[position].getKeyTrailer();
                String url = baseYoutube + keyTrailer;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.length;
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.portada_trailer)
        ImageView portadaTrailer;
        @BindView(R.id.idioma_trailer)
        ImageView idiomaTrailer;
        @BindView(R.id.nombre_trailer)
        TextView nombreTrailer;
        @BindView(R.id.site_trailer)
        ImageView siteTrailer;
        @BindView(R.id.size_trailer)
        ImageView sizeTrailer;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
