package com.itechart.contacts.db.dao;

import com.itechart.contacts.db.model.ContactModel;
import com.itechart.contacts.exception.TransactionException;

import java.util.List;

public interface ContactDao {

    int add(ContactModel contact) throws TransactionException;

    int update(ContactModel contact) throws TransactionException;

    ContactModel get(int id) throws TransactionException;

    List<ContactModel> getContactsByPage(int page, int amount) throws TransactionException;

    int delete(int id) throws TransactionException;

    int size() throws TransactionException;
}