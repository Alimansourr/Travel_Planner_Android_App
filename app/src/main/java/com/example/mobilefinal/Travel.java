package com.example.mobilefinal;

import java.util.List;

public class Travel {

    private int id;
    private String name;
    private List<Note> notes;
    private Location location;

    private List<Images> photos;

    public Travel(int id, List<Note> notes, Location location, List<Images> photos) {
        this.id = id;
        this.notes = notes;
        this.location = location;
        this.photos = photos;
    }

    public Travel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Images> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Images> photos) {
        this.photos = photos;
    }
}
