package de.thb.sparefood_app.model;

public class Meal {
    private String name;
    private String description;
    private int _id;
    private int longitude;
    private int latitude;
    private PROPERTIES filter;

    public Meal() {
        this("", "", -1, 0, 0);
    }

    public Meal(String name, String description) {
        this(name, description, -1, 0, 0);
    }

    public Meal(String name, String description, int _id, int longitude, int latitude) {
        this.name = name;
        this.description = description;
        this._id = _id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public PROPERTIES getFilter() {
        return filter;
    }

    public void setFilter(PROPERTIES filter) {
        this.filter = filter;
    }
}
