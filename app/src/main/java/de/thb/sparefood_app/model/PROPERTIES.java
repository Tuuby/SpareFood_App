package de.thb.sparefood_app.model;

public enum PROPERTIES {
    NO_FISH(0),
    NO_LACTOSE(1),
    PROTEIN(2),
    NO_NUTS(3),
    NOT_HOT(4),
    NO_PORK(5),
    SOY(6),
    VEGAN(7),
    VEGETARIAN(8),
    NO_WHEAT(9);

    public final int id;

    private PROPERTIES(int id) {
        this.id = id;
    }
}
