package de.thb.sparefood_app.model;

import java.util.List;

public class Meal {
    private String name;
    private String description;
    private int id;
    private Location location;
    private List<PROPERTIES> properties;

    public Meal() {
        this("", "", -1, null);
    }

    public Meal(String name, String description) {
        this(name, description, -1, null);
    }

    public Meal(String name, String description, int _id, Location location) {
        this.name = name;
        this.description = description;
        this.id = _id;
        this.location = location;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<PROPERTIES> getProperties() {
        return properties;
    }

    public void setProperties(List<PROPERTIES> filter) {
        this.properties = filter;
    }

    public void addProperty(PROPERTIES property) {
        this.properties.add(property);
    }
}
