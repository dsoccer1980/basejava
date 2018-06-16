package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private final int STORAGE_LIMIT = 10000;
    private Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    @Override
    public void update(Resume r) {
        if (r == null) {
            System.out.println("Warning: Resume is null");
            return;
        }
        int index = getIndex(r.getUuid());
        if (index != -1) {
            storage[index] = r;
        } else {
            System.out.println("Warning: Resume '" + r + "' does not exist in storage");
        }
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

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
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index != -1) {
            return storage[index];
        } else {
            System.out.println("Warning: Resume '" + uuid + "' does not exist in storage");
            return null;
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

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    @Override
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    public int size() {
        return size;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
