package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;


public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int getIndex(String uuid) {
        Resume searchResume = new Resume();
        searchResume.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchResume);
    }

    @Override
    protected void insertResume(Resume resume, int index) {
        int insertPosition = -index - 1;
        if (insertPosition < size) {
            System.arraycopy(storage, insertPosition, storage, insertPosition + 1, size - insertPosition);
        }
        storage[insertPosition] = resume;
    }

    @Override
    protected void deleteResume(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }


}
