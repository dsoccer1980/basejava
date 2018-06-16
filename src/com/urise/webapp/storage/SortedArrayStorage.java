package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if (r == null) {
            System.out.println("Warning: Resume is null");
        } else if (size == STORAGE_LIMIT) {
            System.out.println("Warning: Resume was not inserted. Storage is full");
        } else {
            int index = Arrays.binarySearch(storage, 0, size, r);
            if (index >= 0) {
                System.out.println("Warning: Resume '" + r + "' already exists in storage");
            } else {
                int insertPosition = Math.abs(index) - 1;
                if (insertPosition < size) {
                    System.arraycopy(storage, insertPosition, storage, insertPosition + 1, size - insertPosition + 1);
                }
                storage[insertPosition] = r;
                size++;
            }
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            System.arraycopy(storage, index + 1 , storage, index, size - index - 1);
            storage[size] = null;
            size--;
        } else {
            System.out.println("Warning: Resume '" + uuid + "' does not exist in storage");
        }
    }

    @Override
    protected int getIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
