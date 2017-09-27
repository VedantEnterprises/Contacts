package com.itechart.contacts.db.dao;

import com.itechart.contacts.db.model.PhoneModel;
import com.itechart.contacts.exception.TransactionException;

import java.util.List;

public interface PhoneDao {

    int add(PhoneModel phone) throws TransactionException;

    int update(PhoneModel phone) throws TransactionException;

    int delete(int id) throws TransactionException;

    List<PhoneModel> get(int contactId) throws TransactionException;
}