package com.itechart.contacts.db.transaction;

import com.itechart.contacts.exception.TransactionException;

public interface TransactionManager {
    <T> T executeTransaction(Transaction<T> transaction) throws TransactionException;
}