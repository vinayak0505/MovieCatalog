package com.example.bharatagri.dataLoader;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.bharatagri.movieAdapter.movie;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class movieLoader extends AsyncTaskLoader<List<movie>> {
    String url;
    public movieLoader(@NonNull Context context,String main_url) {
        super(context);
        url = main_url;
    }

    @Nullable
    @Override
    public List<movie> loadInBackground() {
        String jsonResponse;
        ArrayList<movie> data = null;
        URL MAIN_URL = QueryUtils.createUrl(url);
        try {
            assert MAIN_URL != null;
            jsonResponse = QueryUtils.makeHttpRequest(MAIN_URL);
            data = QueryUtils.extractDocument(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
