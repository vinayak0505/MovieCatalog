package com.example.bharatagri.dataLoader;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.bharatagri.data.MovieContract;
import com.example.bharatagri.movieAdapter.movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class QueryUtils {
    private QueryUtils() {
    }

    public static ArrayList<movie> extractDocument(String JSON) {
        ArrayList<movie> movies = new ArrayList<>();
        try {

            JSONObject movieObject = new JSONObject(JSON);
            JSONArray movieArray = movieObject.getJSONArray("results");
            ContentValues values = new ContentValues();
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject currentMovieDetail = movieArray.getJSONObject(i);
                String id = currentMovieDetail.getString("id");
                String Title = currentMovieDetail.getString("original_title");
                String description = currentMovieDetail.getString("release_date");
                int rating = currentMovieDetail.getInt("vote_average") / 2;
                String image = "https://image.tmdb.org/t/p/w500" + currentMovieDetail.getString("poster_path");
                URL url = new URL(image);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                movie currentMovie = new movie(id, Title, description, rating, bmp, image);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 0, stream);
                values.put(MovieContract._ID, id);
                values.put(MovieContract.Title, Title);
                values.put(MovieContract.Description, description);
                values.put(MovieContract.Rating, rating);
                values.put(MovieContract.ImageByte, stream.toByteArray());
                values.put(MovieContract.ImageUrl, image);
                movies.add(currentMovie);
            }

        } catch (JSONException | MalformedURLException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return movies;

    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHttpRequest: urlConnection error code = " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: ioException" + e);
            return null;
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;
    }

    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static URL createUrl(String StringUrl){
        URL url;
        try{
            url =  new URL(StringUrl);
            return url;
        } catch (MalformedURLException e) {
            Log.e(TAG, "createUrl: malformedUrlException" +e);
        }
        return null;
    }
}
