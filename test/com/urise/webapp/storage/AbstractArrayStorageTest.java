package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String DUMMY = "dummy";
    private static final int COUNT_RESUMES_IN_STORAGE_BEFORE_TEST = 3;
    private Resume resume1 = new Resume(UUID_1);
    private Resume resume2 = new Resume(UUID_2);
    private Resume resume3 = new Resume(UUID_3);
    private Resume resume4 = new Resume();
    private Resume dummyResume = new Resume(DUMMY);

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void save() throws Exception {
        storage.save(resume4);
        assertEquals(COUNT_RESUMES_IN_STORAGE_BEFORE_TEST + 1, storage.size());
        assertEquals(resume4, storage.get(resume4.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistResume() throws Exception {
        storage.save(resume3);
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() throws Exception {
        try {
            int storageLimit = storage.getStorageLimit();
            for (int i = 0; i < storageLimit - COUNT_RESUMES_IN_STORAGE_BEFORE_TEST; i++) {
                storage.save(new Resume());
            }
            assertEquals(storageLimit, storage.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        storage.save(resume4);
    }

    @Test
    public void get() throws Exception {
        assertEquals(resume2, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() throws Exception {
        storage.get(DUMMY);
    }

    @Test
    public void delete() throws Exception {
        storage.delete(UUID_2);
        assertEquals(COUNT_RESUMES_IN_STORAGE_BEFORE_TEST - 1, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() throws Exception {
        storage.delete(DUMMY);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() throws Exception {
        storage.update(resume1);
        assertEquals(COUNT_RESUMES_IN_STORAGE_BEFORE_TEST, storage.size());
        assertEquals(resume1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() throws Exception {
        storage.update(dummyResume);
    }

    @Test
    public void getAll() throws Exception {
        Resume[] resumes = new Resume[]{resume1, resume2, resume3};
        assertArrayEquals(resumes, storage.getAll());
    }

    @Test
    public void size() throws Exception {
        assertEquals(COUNT_RESUMES_IN_STORAGE_BEFORE_TEST, storage.size());
    }

}