package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public abstract class AbstractStorageTest {
    protected Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String DUMMY = "dummy";
    protected static int countResumesInStorageBeforeTest;
    private Resume resume1 = new Resume(UUID_1);
    private Resume resume2 = new Resume(UUID_2);
    private Resume resume3 = new Resume(UUID_3);
    protected Resume resume4 = new Resume();
    private Resume dummyResume = new Resume(DUMMY);

    @Before
    public void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
        countResumesInStorageBeforeTest = storage.size();
    }

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void save() {
        storage.save(resume4);
        assertEquals(countResumesInStorageBeforeTest + 1, storage.size());
        assertEquals(resume4, storage.get(resume4.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistResume() {
        storage.save(resume3);
    }

    @Test
    public void get() {
        assertEquals(resume2, storage.get(UUID_2));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(DUMMY);
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        assertEquals(countResumesInStorageBeforeTest - 1, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(DUMMY);
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        storage.update(resume1);
        assertEquals(countResumesInStorageBeforeTest, storage.size());
        assertEquals(resume1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(dummyResume);
    }

    @Test
    public void getAll() {
        Resume[] resumes = {resume1, resume2, resume3};
        assertArrayEquals(resumes, storage.getAll());
    }

    @Test
    public void size() {
        assertEquals(countResumesInStorageBeforeTest, storage.size());
    }

}