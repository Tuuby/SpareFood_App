package de.thb.sparefood_app.ui.home;

public class Card {
    private final String text;
    private final String distance;

    public Card(String text, String distance) {
        this.text = text;
        this.distance = distance;
    }

    public String getText() {
        return text;
    }

    public String getDistance() {
        return distance;
    }
}
