package de.thb.sparefood_app.ui.home;

public class Card {
    private final String text;
    private final String distance;
    private final String imageURL;

    public Card(String text, String distance, String imageURL) {
        this.text = text;
        this.distance = distance;
        this.imageURL = imageURL;
    }

    public String getText() {
        return text;
    }

    public String getDistance() {
        return distance;
    }

    public String getImageURL() {
        return imageURL;
    }
}
