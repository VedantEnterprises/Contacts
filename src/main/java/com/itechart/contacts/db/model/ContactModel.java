package com.itechart.contacts.db.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.List;

public class ContactModel {
    @SerializedName("contact_id")
    private int id;

    @SerializedName("first_name")
    private String firstName; // required
    @SerializedName("last_name")
    private String lastName; // required
    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("birthday")
    private Date birthday;
    @SerializedName("sex")
    private String sex;
    @SerializedName("nationality")
    private String nationality;
    @SerializedName("marital_status")
    private String maritalStatus;

    @SerializedName("site")
    private String site;
    @SerializedName("email")
    private String email;
    @SerializedName("job")
    private String currentJob;

    @SerializedName("address")
    private AddressModel address;

    @SerializedName("phones")
    private List<PhoneModel> phones;

    @SerializedName("attachments")
    private List<AttachmentModel> attachments;

    private String avatar;

    public ContactModel(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFullName() {
        return lastName + " " + firstName + " " + middleName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(String currentJob) {
        this.currentJob = currentJob;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public List<PhoneModel> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneModel> phones) {
        this.phones = phones;
    }

    public List<AttachmentModel> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentModel> attachments) {
        this.attachments = attachments;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthday=" + birthday +
                ", sex='" + sex + '\'' +
                ", nationality='" + nationality + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", site='" + site + '\'' +
                ", email='" + email + '\'' +
                ", currentJob='" + currentJob + '\'' +
                ", address=" + address +
                '}';
    }
}