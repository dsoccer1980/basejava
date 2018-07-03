package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;


public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    protected Resume doGet(Integer index) {
        return storage[index];
    }

    @Override
    protected void doDelete(Integer index) {
        fillDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        Resume[] copyStorage = Arrays.copyOf(storage, size);
        Arrays.sort(copyStorage, COMPARATOR_RESUME);
        return Arrays.asList(copyStorage);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    protected void doUpdate(Integer index, Resume resume) {
        storage[index] = resume;
    }

    @Override
    protected void doSave(Integer index, Resume resume) {
        if (isStorageFull()) {
            throw new StorageException("Storage overflowed", resume.getUuid());
        }
        insertElement(index, resume);
        size++;
    }

    @Override
    protected boolean isExist(Integer index) {
        return index >= 0;
    }

    private boolean isStorageFull() {
        return size == STORAGE_LIMIT;
    }

    protected abstract void insertElement(int index, Resume resume);

    protected abstract void fillDeletedElement(int index);

}
