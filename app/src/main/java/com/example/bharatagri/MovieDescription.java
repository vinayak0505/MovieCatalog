package com.example.bharatagri;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bharatagri.dataLoader.QueryUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieDescription extends AppCompatActivity {
    private String url = "https://api.themoviedb.org/3/movie/497582?api_key=bfbbc0ae80804a505232e70fc5f9580b&language=en-US";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_description);
        String id = getIntent().getExtras().getString("idValue");
        url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=bfbbc0ae80804a505232e70fc5f9580b&language=en-US";
        new MovieDiscLoader().execute();
    }

    public void onLoadComplete(movieDescriptionAdapter movieDescriptionAdapter) {
        TextView textView = findViewById(R.id.name_of_the_moveie);
        textView.setText(movieDescriptionAdapter.Title);
        TextView textView1 = findViewById(R.id.desc_date);
        textView1.setText(movieDescriptionAdapter.Id);
        TextView textView2 = findViewById(R.id.main_description);
        textView2.setText(movieDescriptionAdapter.Description);
        ImageView imageView = findViewById(R.id.description_background);
        imageView.setImageBitmap(movieDescriptionAdapter.backImage);
        imageView.setContentDescription(movieDescriptionAdapter.backImageString);
        ImageView imageView1 = findViewById(R.id.main_description_image);
        imageView1.setImageBitmap(movieDescriptionAdapter.image);
        imageView1.setContentDescription(movieDescriptionAdapter.imageString);
    }

    private class MovieDiscLoader extends AsyncTask<URL, Void, movieDescriptionAdapter> {

        @Override
        protected movieDescriptionAdapter doInBackground(URL... urls) {
            URL mainUrl = QueryUtils.createUrl(url);
            String jsonResponse = null;
            movieDescriptionAdapter movie = null;
            try {
                assert mainUrl != null;
                jsonResponse = QueryUtils.makeHttpRequest(mainUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Log.e("games", "doInBackground: ");

                assert jsonResponse != null;
                JSONObject movieObject = new JSONObject(jsonResponse);
                String backdrop_path = movieObject.getString("backdrop_path");
                URL backUrl = new URL("https://image.tmdb.org/t/p/w500" + backdrop_path);
                String original_title = movieObject.getString("original_title");
                String overview = movieObject.getString("overview");
                String poster_path = movieObject.getString("poster_path");
                URL url = new URL("https://image.tmdb.org/t/p/w500" + poster_path);
                String release_date = movieObject.getString("release_date");
                Log.e("games", "doInBackground: " + release_date);
                Bitmap backBmp = BitmapFactory.decodeStream(backUrl.openConnection().getInputStream());
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return new movieDescriptionAdapter(release_date, original_title, overview, bmp, poster_path, backBmp, backdrop_path);
            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(movieDescriptionAdapter movieDescriptionAdapter) {
            super.onPostExecute(movieDescriptionAdapter);
            onLoadComplete(movieDescriptionAdapter);
        }
    }

}