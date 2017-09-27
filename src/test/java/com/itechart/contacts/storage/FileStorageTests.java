package com.itechart.contacts.storage;

import com.itechart.contacts.exception.StorageException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class FileStorageTests {

    private static final String TEXT = "Gag The Fag 3";
    private static final int ID = 1;

    @Test
    public void saveAndGetFileTest() {
        String savedText = null;
        File testFile = null;
        FileOutputStream out = null;
        try {
            testFile = File.createTempFile("test-", ".txt");
            out = new FileOutputStream(testFile);
            out.write(TEXT.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }

        if (testFile != null) {
            BufferedReader in = null;
            try {
                LocalFileStorage fileStorage = new LocalFileStorage();
                String uuid = fileStorage.saveFile(ID, testFile);
                File savedFile = fileStorage.getFile(ID, uuid, "gag.txt");
                in = new BufferedReader(new FileReader(savedFile));
                savedText = in.readLine();
            } catch (StorageException | IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

        assertEquals(TEXT, savedText);
    }
}