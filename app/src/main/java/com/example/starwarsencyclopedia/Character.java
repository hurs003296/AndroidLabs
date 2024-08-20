package com.example.starwarsencyclopedia;

public class Character {
    private String name;
    private String height;
    private String mass;
    private String hairColor;

    public Character(String name, String height, String mass, String hairColor) {
        this.name = name;
        this.height = height;
        this.mass = mass;
        this.hairColor = hairColor;
    }

    public String getName() {
        return name;
    }

    public String getHeight() {
        return height;
    }

    public String getMass() {
        return mass;
    }

    public String getHairColor() {
        return hairColor;
    }
}

