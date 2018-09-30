package com.example.andrineia.iscb.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Phones {



    /**
     * id : 1
     * description : First place in our list goes to the excellent Moto E4 Plus. It's a cheap phone that features phenomenal battery life, a fingerprint scanner and a premium feel design, plus it's a lot cheaper than the Moto G5 below. It is a little limited with its power, but it makes up for it by being able to last for a whole two days from a single charge. If price and battery are the most important features for you, the Moto E4 Plus will suit you perfectly.
     * rating : 4.9
     * price : 179.99
     * brand : Samsung
     * thumbImageURL : https://cdn.mos.cms.futurecdn.net/grwJkAGWQp4EPpWA3ys3YC-650-80.jpg
     * name : Moto E4 Plus
     */

    private int id;
    private String description;
    private double rating;
    private double price;
    private String brand;
    private String thumbImageURL;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getThumbImageURL() {
        return thumbImageURL;
    }

    public void setThumbImageURL(String thumbImageURL) {
        this.thumbImageURL = thumbImageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void decode(JSONObject json) throws JSONException {
        if (json != null) {
            if (json.length() > 0) {
                this.id = json.optInt("id");
                this.description = json.optString("description");
                this.rating = json.optDouble("rating");
                this.brand = json.optString("brand");
                this.thumbImageURL = json.optString("thumbImageURL");
                this.name = json.optString("name");
                this.price = json.optInt("price");
            }
        }
    }
}
