package com.itechart.contacts.db.model;

import com.google.gson.annotations.SerializedName;

public class PhoneModel {
    @SerializedName("phone_id")
    private int id;
    private int contactId;

    @SerializedName("country_code")
    private int countryCode;
    @SerializedName("operator_code")
    private int operatorCode;
    @SerializedName("phone_num")
    private int phoneNumber;
    @SerializedName("phone_type")
    private String type;
    @SerializedName("comment")
    private String comment;

    @SerializedName("mark")
    private String mark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public int getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public int getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(int operatorCode) {
        this.operatorCode = operatorCode;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullPhoneNumber() {
        return String.format("%s-%s-%s", countryCode, operatorCode, phoneNumber);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "PhoneModel{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", countryCode=" + countryCode +
                ", operatorCode=" + operatorCode +
                ", phoneNumber=" + phoneNumber +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}