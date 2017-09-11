package com.itechart.contacts.db.transaction;

import com.itechart.contacts.exception.TransactionException;

public interface Transaction<T> {
    T execute() throws TransactionException;
}