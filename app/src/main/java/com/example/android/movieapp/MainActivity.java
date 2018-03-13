package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.Settings.SettingActivity;
import com.example.android.movieapp.Utilities.MovieAdapter;
import com.example.android.movieapp.Utilities.MovieAdapter1;
import com.example.android.movieapp.Utilities.NetworkUtils;
import com.example.android.movieapp.data.MovieJson;
import com.example.android.movieapp.model.Movie;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //@BindView(R.id.gridview) GridView gridView;
    @BindView(R.id.noconecttion) ImageView imageViewNoConecttion;
    @BindView(R.id.text_noconecttion) TextView textViewNoConecttion;

    //private MovieAdapter1 movieAdapter;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;

    // Constantes auxiliares.
    private static final String ORDEN_POPULAR = "popular";
    private static final String ORDEN_DEFAULT = ORDEN_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        imageViewNoConecttion.setImageResource(R.drawable.sinconexion);

        // Iniciamos el trabajo de fondo que nos presentará las portadas de las películas.
        cargarPortadas(definirPreferences());

        // Definimos la escucha al GridView y según su selección pasamos la información.
    /*    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsMovieActivity.class);
                Movie currentMovie = (Movie) parent.getItemAtPosition(position);
                String posterMovie = currentMovie.getmPortada();
                intent.putExtra("poster", posterMovie);
                String originalTitle = currentMovie.getmTituloOriginal();
                intent.putExtra("title_original", originalTitle);
                String titleMovie = currentMovie.getmTitulo();
                intent.putExtra("title", titleMovie);
                String fechaLanzamiento = currentMovie.getmFechaLanzamiento();
                intent.putExtra("fecha", fechaLanzamiento);
                String sipnosisMovie = currentMovie.getmSipnosis();
                intent.putExtra("sipnosis", sipnosisMovie);
                String promedioVotoMovie = currentMovie.getmPromedioVoto();
                intent.putExtra("promedio", promedioVotoMovie);
                String idioma_original = currentMovie.getmIdiomaOriginal();
                intent.putExtra("idioma", idioma_original);

                // Comprobación.
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });*/
    }
    // Si hay conexión a internet (podemos realizar la solicitud de conexión) entonces procedemos
    // con el trabajo de fondo de lo contrario avisamos de la falla de conexión.
    private void cargarPortadas(String ordenMovies) {
        if (hayInternet()) {
            new MovieAsyncTask().execute(ordenMovies);
            recyclerView.setVisibility(View.VISIBLE);
           // gridView.setVisibility(View.VISIBLE);
            textViewNoConecttion.setVisibility(View.GONE);
            imageViewNoConecttion.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            //gridView.setVisibility(View.GONE);
            textViewNoConecttion.setVisibility(View.VISIBLE);
            imageViewNoConecttion.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.toast, Toast.LENGTH_SHORT).show();
        }
    }
    // Escucha para cambios de preferencias.
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(R.string.key_orden_settings)) {
            cargarPortadas(sharedPreferences.getString(getString(R.string.key_orden_settings),
                    getString(R.string.popular)));
        }
    }

    public class MovieAsyncTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... strings) {

            URL urlDefault = NetworkUtils.buildUrl(strings[0]);
            try {
                String jsonMovie = NetworkUtils.getResponseFromHttpUrl(urlDefault);

                Movie[] simpleMovieJsonData = MovieJson.recogerDatosMovieDeJson(jsonMovie);

                return simpleMovieJsonData;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {

            if (movies != null) {
                adapter = new MovieAdapter(MainActivity.this, movies);
                //gridView.setAdapter(movieAdapter);
                recyclerView.setAdapter(adapter);

            } else {
                Toast.makeText(MainActivity.this, R.string.ocurrioError, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Comprobación de conexión.
    private boolean hayInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Definición de preferences.
    public String definirPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderUser = sharedPreferences.getString(getString(R.string.key_orden_settings), getString(R.string.order_popular));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (orderUser.equals("")) {
            orderUser = ORDEN_DEFAULT;
        }
        return orderUser;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.orden_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.orden_preference:
                lanzarSettings();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Método auxiliar, sólo lanza la actividad de ajustes.
    private void lanzarSettings() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
