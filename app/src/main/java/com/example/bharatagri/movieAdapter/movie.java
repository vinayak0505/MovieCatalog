package com.example.bharatagri.movieAdapter;

import android.graphics.Bitmap;

public class movie {
    private String Id;
    private String Title;
    private String Description;
    private float rating;
    private Bitmap image;
    private String imageString;

    public movie(String id, String title, String description, float rating, Bitmap image, String imageString) {
        Id = id;
        Title = title;
        Description = description;
        this.rating = rating;
        this.image = image;
        this.imageString = imageString;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getId() {
        return Id;
    }
    public void setId(String id) {
        Id = id;
    }
}
