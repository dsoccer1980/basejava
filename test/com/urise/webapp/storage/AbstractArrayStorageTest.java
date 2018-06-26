package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.Test;


import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveOverFlow() throws Exception {
        try {
            int storageLimit = AbstractArrayStorage.STORAGE_LIMIT;
            for (int i = 0; i < storageLimit - COUNT_RESUMES_IN_STORAGE_BEFORE_TEST; i++) {
                storage.save(new Resume());
            }
            assertEquals(storageLimit, storage.size());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        storage.save(resume4);
    }

}