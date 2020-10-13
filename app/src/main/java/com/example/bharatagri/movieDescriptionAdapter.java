package com.example.bharatagri;

import android.graphics.Bitmap;

public class movieDescriptionAdapter {

    public final String Id;
    public final String Title;
    public final String Description;
    public final Bitmap image;
    public final String imageString;
    public final Bitmap backImage;
    public final String backImageString;

    public movieDescriptionAdapter(String id, String title, String description, Bitmap image, String imageString, Bitmap backImage, String backImageString) {
        Id = id;
        Title = title;
        Description = description;
        this.image = image;
        this.imageString = imageString;
        this.backImage = backImage;
        this.backImageString = backImageString;
    }
}
