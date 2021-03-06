package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;


public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume searchResume = new Resume(uuid, "");
        return Arrays.binarySearch(storage, 0, size, searchResume, Comparator.comparing(Resume::getUuid));
    }

    @Override
    protected void insertElement(int index, Resume resume) {
        int insertPosition = -index - 1;
        if (insertPosition < size) {
            System.arraycopy(storage, insertPosition, storage, insertPosition + 1, size - insertPosition);
        }
        storage[insertPosition] = resume;
    }

    @Override
    protected void fillDeletedElement(int index) {
        System.arraycopy(storage, index + 1, storage, index, size - index - 1);
    }


}
