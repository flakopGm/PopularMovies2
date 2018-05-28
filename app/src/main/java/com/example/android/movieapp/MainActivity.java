package com.example.android.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.Utilities.NetworkUtils;
import com.example.android.movieapp.adapters.AdaptadorFavoritos;
import com.example.android.movieapp.adapters.MovieListAdapter;
import com.example.android.movieapp.data.MovieContract;
import com.example.android.movieapp.data.MovieJson;
import com.example.android.movieapp.model.Movie;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.recycler_movies)
    RecyclerView recyclerMovies;
    @BindView(R.id.recycler_favoritos)
    RecyclerView recyclerFavoritos;
    @BindView(R.id.linear_emptyview)
    LinearLayout linearEmptyView;
    @BindView(R.id.noconecttion)
    ImageView imageViewNoConecttion;
    @BindView(R.id.text_noconecttion)
    TextView textViewNoConecttion;
    @BindView(R.id.navigation_view)
    BottomNavigationView navigationView;

    private boolean isNavEnFavorito;

    // Constantes auxiliares.
    private static final String ORDEN_POPULAR = "popular";
    private static final String ORDEN_MAS_VALORADOS = "top_rated";
    private static final String ORDEN_FAVORITOS = "favorites";
    // API-KEY
    private static final String API_KEY = NetworkUtils.API_KEY_COMPROBACION;

    // Identificador del cargador de datos del producto
    private static final int MOVIE_ID_LOADER = 1;
    private MovieListAdapter adapter;
    private AdaptadorFavoritos favoritosAdapter;

    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        imageViewNoConecttion.setImageResource(R.drawable.sinconexion9patch);
        hayKeyApi();

        // Según las preferencias de última vez si la hubiese, se posiciona el bottomNavigation y
        // derivados. Y según su opción se ejecuta su trabajo de fondo.
        isNavEnFavorito = leerPreferences().getBoolean(getString(R.string.isnav_favoritos), false);
        posicionarNavigation(leerPreferences());

        if (isNavEnFavorito) {
            GoFavoritos();
        } else {
            // Iniciamos el trabajo de fondo que nos presentará las portadas de las películas.
            cargarPortadas(leerPreferences());
        }

        // Escucha para el BottomNavigattion
        navigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.most_popular:
                                definirPreferences(ORDEN_POPULAR, false);
                                isNavEnFavorito = false;
                                setTitle(R.string.popular);
                                cargarPortadas(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
                                break;
                            case R.id.top_rated:
                                definirPreferences(ORDEN_MAS_VALORADOS, false);
                                isNavEnFavorito = false;
                                setTitle(R.string.top_rated);
                                cargarPortadas(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
                                break;
                            case R.id.favorites:
                                definirPreferences(ORDEN_FAVORITOS, true);
                                isNavEnFavorito = true;
                                setTitle(R.string.favoritos);
                                GoFavoritos();
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    // Si hay conexión a internet (podemos realizar la solicitud de conexión) entonces procedemos
    // con el trabajo de fondo de lo contrario avisamos de la falla de conexión.
    private void cargarPortadas(SharedPreferences ordenMoviesPreference) {
        linearEmptyView.setVisibility(View.GONE);
        recyclerMovies.setVisibility(View.VISIBLE);
        recyclerFavoritos.setVisibility(View.GONE);

        if (hayInternet()) {
            new MovieAsyncTask().execute(ordenMoviesPreference.getString(getString(R.string.key_orden_settings),
                    getString(R.string.order_popular)));
            recyclerMovies.setVisibility(View.VISIBLE);
            textViewNoConecttion.setVisibility(View.GONE);
            imageViewNoConecttion.setVisibility(View.GONE);
        } else {
            if (!isNavEnFavorito) {
                recyclerMovies.setVisibility(View.GONE);
                textViewNoConecttion.setVisibility(View.VISIBLE);
                imageViewNoConecttion.setVisibility(View.VISIBLE);
                Toast.makeText(this, R.string.toast, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void GoFavoritos() {
        // Inicialmente ocultamos el recycler movie, para mostrar el de favoritos.
        recyclerMovies.setVisibility(View.GONE);
        recyclerFavoritos.setVisibility(View.VISIBLE);
        // Ocultamos info no conexión ya que no afecta a los favoritos.
        textViewNoConecttion.setVisibility(View.GONE);
        imageViewNoConecttion.setVisibility(View.GONE);

        linearEmptyView.setVisibility(View.VISIBLE);
        favoritosAdapter = new AdaptadorFavoritos(this);
        recyclerFavoritos.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerFavoritos.setLayoutManager(linearLayoutManager);
        recyclerFavoritos.setAdapter(favoritosAdapter);
        // Iniciamos el cargador con un método auxiliar para la eliminación de películas de favoritos
        // haciendo swipe hacia la derecha o izquierda. Por último cambiamos el título de la activity.
        getSupportLoaderManager().initLoader(MOVIE_ID_LOADER, null, this).forceLoad();
        eliminarFavorito();
        setTitle(getString(R.string.favoritos));
    }

    private void eliminarFavorito() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Guardamos el id con tag nº 15 *Es mi número preferido* ;)
                int id = (int) viewHolder.itemView.getTag(R.integer.TagViewHolder);
                String stringId = Integer.toString(id);
                Uri uri = MovieContract.MovieEntry.construirUriMovie(stringId);

                getContentResolver().delete(uri, null, null);
                Toast.makeText(MainActivity.this,
                        String.format(getString(R.string.eliminado)),
                        Toast.LENGTH_SHORT).show();
                // Restauramos el cargador para actualizar los datos después de eliminar.
                getSupportLoaderManager().restartLoader(
                        MOVIE_ID_LOADER,
                        null,
                        MainActivity.this).forceLoad();

                // Si no hay elementos se muestra el empty view.
                if (recyclerFavoritos.getChildCount() < 0) {
                    linearEmptyView.setVisibility(View.VISIBLE);
                }
            }
        }).attachToRecyclerView(recyclerFavoritos);
    }

    public class MovieAsyncTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... strings) {

            URL urlDefault = NetworkUtils.buildUrlMovies(strings[0]);
            try {
                String jsonMovie = NetworkUtils.getResponseFromHttpUrl(urlDefault);

                return MovieJson.recogerInfoMovie(jsonMovie);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Movie[] movies) {

            if (movies != null) {
                adapter = new MovieListAdapter(MainActivity.this, movies);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(
                        getApplicationContext(),
                        definirCol(getApplicationContext()));
                recyclerMovies.setLayoutManager(gridLayoutManager);
                recyclerMovies.setAdapter(adapter);
            } else {
                if (API_KEY.equals("") || API_KEY.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.faltaKeyApi),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            R.string.ocurrioError,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favoritosAdapter.swapCursor(data);
        if (data.getCount() > 0) {
            linearEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favoritosAdapter.swapCursor(null);
    }

    // Comprobación de conexión.
    private boolean hayInternet() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Lectura de preferencias.
    private SharedPreferences leerPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.getString(
                getString(R.string.key_orden_settings),
                getString(R.string.order_popular));
        sharedPreferences.getBoolean(getString(R.string.isnav_favoritos), false);
        return sharedPreferences;
    }

    // Definición de preferencias.
    private void definirPreferences(String preference, boolean isFav) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.key_orden_settings), preference);
        editor.putBoolean(getString(R.string.isnav_favoritos), isFav);
        editor.commit();
    }

    private String posicionarNavigation(SharedPreferences sharedPreferences) {
        String ordenacion = sharedPreferences.getString(
                getString(R.string.key_orden_settings),
                getString(R.string.order_popular));
        switch (ordenacion) {
            case "popular":
                navigationView.setSelectedItemId(R.id.most_popular);
                break;
            case "top_rated":
                navigationView.setSelectedItemId(R.id.top_rated);
                break;
            case "favorites":
                navigationView.setSelectedItemId(R.id.favorites);
                break;
        }
        return ordenacion;
    }

    // Método auxiliar de aviso para la key.
    private void hayKeyApi() {
        if (API_KEY.isEmpty() || API_KEY.equals("")) {
            Toast.makeText(this, R.string.faltaKeyApi, Toast.LENGTH_LONG).show();
        }
    }

    /*  Método de ayuda para descubrir la orientación de pantalla del dispositivo.
        con @2 para orientación Vertical,
        o con @4 para orientación Horizontal.
     */
    public static int definirCol(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return 2;
            case Surface.ROTATION_90:
            default:
                return 4;
        }
    }
}
