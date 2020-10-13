package com.example.bharatagri.movieAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bharatagri.R;

import java.util.List;

public class MoviesAdapter extends ArrayAdapter<movie> {
    public MoviesAdapter(@NonNull Context context, int resource, @NonNull List<movie> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.main_gridview, parent, false);
        }
        movie currentMovie = getItem(position);

        TextView title = view.findViewById(R.id.movie_title);
        assert currentMovie != null;
        title.setText(currentMovie.getTitle());

        TextView description = view.findViewById(R.id.movie_date);
        description.setText(currentMovie.getDescription());

        RatingBar ratingBar = view.findViewById(R.id.rating_movie);
        ratingBar.setRating(currentMovie.getRating());

        ImageView imageView = view.findViewById(R.id.movie_image);
        imageView.setImageBitmap(currentMovie.getImage());
        imageView.setContentDescription("connect to internet " + currentMovie.getImageString());
        return view;
    }
}
