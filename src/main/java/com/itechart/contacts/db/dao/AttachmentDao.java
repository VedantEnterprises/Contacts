package com.itechart.contacts.db.dao;

import com.itechart.contacts.db.model.AttachmentModel;
import com.itechart.contacts.exception.TransactionException;

import java.util.List;

public interface AttachmentDao {

    int add(AttachmentModel attachment) throws TransactionException;

    int update(AttachmentModel attachment) throws TransactionException;

    int delete(int id) throws TransactionException;

    List<AttachmentModel> get(int contactId) throws TransactionException;
}