package com.example.bharatagri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;

import com.example.bharatagri.data.MovieContract;
import com.example.bharatagri.data.MovieDbHelper;
import com.example.bharatagri.movieAdapter.MoviesAdapter;
import com.example.bharatagri.movieAdapter.movie;
import com.example.bharatagri.dataLoader.movieLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<movie>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "games";
    MovieDbHelper movieDbHelper;
    MoviesAdapter moviesAdapter;
    GridView gridView;
    String Main_url;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        i=1;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        movieDbHelper = new MovieDbHelper(this);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.main_gridView);

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        moviesAdapter = new MoviesAdapter(this, 0, new ArrayList<movie>());
        gridView.setAdapter(moviesAdapter);

        if (isConnected) {
            Main_url = "https://api.themoviedb.org/3/discover/movie?api_key=bfbbc0ae80804a505232e70fc5f9580b&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
            LoaderManager.getInstance(this).initLoader(1, null, this).forceLoad();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    movie currentMovie = moviesAdapter.getItem(i);
                    assert currentMovie != null;
                    Intent intent = new Intent(MainActivity.this,MovieDescription.class);
                    intent.putExtra("idValue",currentMovie.getId());
                    startActivity(intent);
                }
            });
        } else {
            loadFromDatabase();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(MainActivity.this, "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadFromDatabase() {
        Log.e(TAG, "loadFromDatabase: working");
        MovieDbHelper movieDbHelper = new MovieDbHelper(this);
        SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        String[] columns = new String[]{MovieContract._ID,
                MovieContract.Title,
                MovieContract.Description,
                MovieContract.Rating,
                MovieContract.ImageByte,
                MovieContract.ImageUrl};
        {
            ArrayList<movie> movies = new ArrayList<>();
            moviesAdapter.clear();
            Cursor cursor = db.query(MovieContract.TableName, columns, null, null, null, null, null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MovieContract._ID));
                String Title = cursor.getString(cursor.getColumnIndex(MovieContract.Title));
                String description = cursor.getString(cursor.getColumnIndex(MovieContract.Description));
                int rating = cursor.getInt(cursor.getColumnIndex(MovieContract.Rating));
                byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(MovieContract.ImageByte));
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                String image = cursor.getString(cursor.getColumnIndex(MovieContract.ImageUrl));
                movie currentMovie = new movie(id, Title, description, rating, bmp, image);
                movies.add(currentMovie);
            }
            moviesAdapter.addAll(movies);
            cursor.close();
            db.close();
            movieDbHelper.close();
        }
    }


    @NonNull
    @Override
    public Loader<List<movie>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.e(TAG, "onCreateLoader: ");
        return new movieLoader(this, Main_url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<movie>> loader, List<movie> data) {
        moviesAdapter.clear();
        moviesAdapter.addAll((data));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<movie>> loader) {
        loader.reset();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        String sortBy = sharedPreferences.getString(getResources().getString(R.string.pref_sort_key), getResources().getString(R.string.pref_sort_popularity_acc_value));
        Main_url = "https://api.themoviedb.org/3/discover/movie?api_key=bfbbc0ae80804a505232e70fc5f9580b&language=en-US&sort_by=" + sortBy + "&include_adult=false&include_video=false&page=1";
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(isConnected)
        LoaderManager.getInstance(this).initLoader(++i, null, this).forceLoad();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister VisualizerActivity as an OnPreferenceChangedListener to avoid any memory leaks.
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}