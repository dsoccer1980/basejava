package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;


/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if (r == null) {
            System.out.println("Warning: Resume is null");
        } else if (size == STORAGE_LIMIT) {
            System.out.println("Warning: Resume was not inserted. Storage is full");
        } else if (getIndex(r.getUuid()) != -1) {
            System.out.println("Warning: Resume '" + r + "' already exists in storage");
        } else {
            storage[size++] = r;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Warning: Resume '" + uuid + "' does not exist in storage");
        }
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
