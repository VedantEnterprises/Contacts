package com.itechart.contacts.db.dao;

import com.itechart.contacts.db.model.AttachmentModel;
import com.itechart.contacts.exception.TransactionException;

import java.util.List;

public interface AttachmentDao {

    int add(List<AttachmentModel> attachments) throws TransactionException;

    int update(List<AttachmentModel> attachments) throws TransactionException;

    List<AttachmentModel> get(int contactId) throws TransactionException;
}