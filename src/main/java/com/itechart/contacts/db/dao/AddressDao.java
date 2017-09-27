package com.itechart.contacts.db.dao;

import com.itechart.contacts.db.model.AddressModel;
import com.itechart.contacts.exception.TransactionException;

public interface AddressDao {

    int add(AddressModel address) throws TransactionException;

    int update(AddressModel address) throws TransactionException;

    int delete(int id) throws TransactionException;

    AddressModel get(int id) throws TransactionException;
}