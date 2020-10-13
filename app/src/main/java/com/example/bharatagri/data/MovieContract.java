package com.example.bharatagri.data;
import android.net.Uri;

public final class MovieContract {

    private MovieContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.bharatagri";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final String TableName = "movieDetail";

    public static final String _ID = "_id";
    public static final String Title = "title";
    public static final String Description = "description";
    public static final String Rating = "rating";
    public static final String ImageByte = "imageByte";
    public static final String ImageUrl = "imageUrl";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MOVIES);

}