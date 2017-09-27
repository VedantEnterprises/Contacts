package com.itechart.contacts.storage;

import com.itechart.contacts.exception.StorageException;

import java.io.File;
import java.io.InputStream;

public interface FileStorage {

    String saveFile(int contactId, File file) throws StorageException;

    String saveFile(int contactId, InputStream in) throws StorageException;

    File getFile(int contactId, String uuid) throws StorageException;

    File getFile(int contactId, String uuid, String fileName) throws StorageException;

    boolean deleteFile(int contactId, String uuid) throws StorageException;
}