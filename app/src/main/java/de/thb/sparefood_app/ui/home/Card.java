package de.thb.sparefood_app.ui.home;

import android.graphics.Bitmap;

public class Card {
    private final String text;
    private final String distance;
    private final Bitmap image;

    public Card(String text, String distance, Bitmap image) {
        this.text = text;
        this.distance = distance;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public String getDistance() {
        return distance;
    }

    public Bitmap getImageURL() {
        return image;
    }
}
