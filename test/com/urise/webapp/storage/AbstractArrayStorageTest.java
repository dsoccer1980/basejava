package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void save() throws Exception {
        Resume newResume = new Resume();
        storage.save(newResume);
        assertEquals(4, storage.size());
        assertEquals(newResume.getUuid(), storage.get(newResume.getUuid()).toString());
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistResume() throws Exception {
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void overFlow() throws Exception {
        for (int i = 0; i < 9997; i++) {
            storage.save(new Resume());
        }
        assertEquals(10000, storage.size());
        try {
            storage.save(new Resume());
            fail();
        } catch (StorageException e) {
            //OK
        }
    }

    @Test
    public void get() throws Exception {
        assertEquals(UUID_2, storage.get(UUID_2).toString());
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get("dummy");
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete("dummy");
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        storage.update(new Resume(UUID_1));
        assertEquals(3, storage.size());
        assertEquals(UUID_1, storage.get(UUID_1).toString());
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(new Resume("dummy"));
    }

    @Test
    public void getAll() throws Exception {
        assertEquals(String.format("[%s, %s, %s]", UUID_1, UUID_2, UUID_3), Arrays.toString(storage.getAll()));
    }

    @Test
    public void size() throws Exception {
        assertEquals(3, storage.size());
    }

}