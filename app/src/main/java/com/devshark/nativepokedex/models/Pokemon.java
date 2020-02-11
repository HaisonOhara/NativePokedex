package com.devshark.nativepokedex.models;

public class Pokemon {
    private int number;
    private String name;

    private String height;
    private String url;

    public Pokemon() {
    }

    public Pokemon(int number, String name, String height) {
        this.number = number;
        this.name = name;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        String[] urlParts = url.split("/");
        return Integer.parseInt(urlParts[urlParts.length - 1]);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String weight) {
        this.height = weight;
    }

    @Override
    public String toString() {
        return "\nNumber" + getNumber()
                + "\nName" + getName()
                + "\nWeight" + getHeight();
    }
}
