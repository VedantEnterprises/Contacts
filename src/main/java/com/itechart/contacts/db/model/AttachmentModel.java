package com.itechart.contacts.db.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class AttachmentModel {
    @SerializedName("attachment_id")
    private int id;
    private int contactId;

    private String fileUuid;
    @SerializedName("file_name")
    private String fileName;
    @SerializedName("upload_date")
    private Timestamp uploadDate;
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

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
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
        return "AttachmentModel{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", fileUuid='" + fileUuid + '\'' +
                ", fileName='" + fileName + '\'' +
                ", uploadDate=" + uploadDate +
                ", comment='" + comment + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}