package com.example.ideo.ideoapp.models;

public class FavouriteLocation {

    String nameLocation;
    String idLocation;

    public FavouriteLocation() {
    }

    public FavouriteLocation(String id, String name) {
        idLocation = id;
        nameLocation = name;
    }

    public String getNameLocation() {
        return nameLocation;
    }

    public String getIdLocation() {
        return idLocation;
    }
}
