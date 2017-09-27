package com.itechart.contacts.db.model;

import com.google.gson.annotations.SerializedName;

public class AddressModel {
    private int id;

    @SerializedName("country")
    private String country;
    @SerializedName("city")
    private String city;
    @SerializedName("detail_address")
    private String detailAddress; // format: street/house/apartment
    @SerializedName("zip")
    private int zip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getFullAddress() {
        return String.format("%s, %s, %s, %d", country, city, detailAddress, zip);
    }

    @Override
    public String toString() {
        return "AddressModel{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", zip=" + zip +
                '}';
    }
}