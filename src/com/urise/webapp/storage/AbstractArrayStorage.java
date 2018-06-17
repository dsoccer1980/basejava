package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public void save(Resume resume) {
        if (resume == null) {
            System.out.println("Warning: Resume is null");
        } else if (size == STORAGE_LIMIT) {
            System.out.println("Warning: Resume was not inserted. Storage is full");
        } else {
            int index = getIndex(resume.getUuid());
            if (getIndex(resume.getUuid()) >= 0) {
                System.out.println("Warning: Resume '" + resume + "' already exists in storage");
            }
            else {
                insertResume(resume, index);
                size++;
            }
        }
    }

    protected abstract void insertResume(Resume resume, int index);

    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        } else {
            System.out.println("Warning: Resume '" + uuid + "' does not exist in storage");
            return null;
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Warning: Resume '" + uuid + "' does not exist in storage");
        }
        else {
            deleteResume(index);
            size--;
        }
    }

    protected abstract void deleteResume(int index);

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        if (resume == null) {
            System.out.println("Warning: Resume is null");
            return;
        }
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
        } else {
            System.out.println("Warning: Resume '" + resume + "' does not exist in storage");
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    protected abstract int getIndex(String uuid);

}
