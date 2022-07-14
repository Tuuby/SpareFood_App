package de.thb.sparefood_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PROPERTIES {
    @JsonProperty("NO_FISH") NO_FISH(0, "NO_FISH"),
    @JsonProperty("NO_LACTOSE") NO_LACTOSE(1, "NO_LACTOSE"),
    @JsonProperty("PROTEIN") PROTEIN(2, "PROTEIN"),
    @JsonProperty("NO_NUTS") NO_NUTS(3, "NO_NUTS"),
    @JsonProperty("NOT_HOT") NOT_HOT(4, "NO_HOT"),
    @JsonProperty("NO_PORK") NO_PORK(5, "NO_PORK"),
    @JsonProperty("SOY") SOY(6, "SOY"),
    @JsonProperty("VEGAN") VEGAN(7, "VEGAN"),
    @JsonProperty("VEGETARIAN") VEGETARIAN(8, "VEGETARIAN"),
    @JsonProperty("NO_WHEAT") NO_WHEAT(9, "NO_WHEAT");

    public final int id;
    public final String name;

    private PROPERTIES(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
