package com.itechart.contacts.storage;

import com.itechart.contacts.exception.StorageException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;
import java.util.UUID;

public class LocalFileStorage implements FileStorage {

    private String storagePath;

    public LocalFileStorage() throws StorageException {
        init();
    }

    private void init() throws StorageException {
        InputStream in = LocalFileStorage.class.getClassLoader().getResourceAsStream("storage.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
            storagePath = properties.getProperty("path");
        } catch (IOException e) {
            throw new StorageException("Failed to initialize FileStorage!");
        }
    }

    @Override
    public String saveFile(int contactId, File file) throws StorageException {
        if (storagePath == null) {
            init();
        }

        File dir = new File(storagePath + File.separator + contactId);
        String uuid = UUID.randomUUID().toString();
        File outFile = new File(dir, uuid);
        while (outFile.exists()) {
            uuid = UUID.randomUUID().toString();
            outFile = new File(dir, uuid);
        }

        try {
            FileUtils.copyFile(file, outFile);
            return uuid;
        } catch (IOException e) {
            throw new StorageException("Failed to store a file!");
        }
    }

    @Override
    public String saveFile(int contactId, InputStream in) throws StorageException {
        if (storagePath == null) {
            init();
        }

        File dir = new File(storagePath + File.separator + contactId);
        String uuid = UUID.randomUUID().toString();
        File outFile = new File(dir, uuid);
        while (outFile.exists()) {
            uuid = UUID.randomUUID().toString();
            outFile = new File(dir, uuid);
        }

        try (OutputStream out = new FileOutputStream(outFile)) {
            IOUtils.copy(in, out);
            return uuid;
        } catch (IOException e) {
            throw new StorageException("Failed to store a file!");
        }
    }

    @Override
    public File getFile(int contactId, String uuid) throws StorageException {
        File dir = new File(storagePath + File.separator + contactId);
        File savedFile = new File(dir, uuid);
        if (savedFile.exists()) {
            return savedFile;
        } else {
            throw new StorageException("File not found!");
        }
    }

    @Override
    public File getFile(int contactId, String uuid, String fileName) throws StorageException {
        File dir = new File(storagePath + File.separator + contactId);
        File savedFile = new File(dir, uuid);
        if (savedFile.exists()) {
            File tempDir = FileUtils.getTempDirectory();
            File resultFile = new File(tempDir, fileName);
            try {
                FileUtils.copyFile(savedFile, resultFile);
                return resultFile;
            } catch (IOException e) {
                throw new StorageException("Failed to copy a file", e);
            }
        } else {
            throw new StorageException("File not found!");
        }
    }

    @Override
    public boolean deleteFile(int contactId, String uuid) throws StorageException {
        File dir = new File(storagePath + File.separator + contactId);
        File savedFile = new File(dir, uuid);
        return savedFile.delete();
    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }
}