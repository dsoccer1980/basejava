package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.util.Objects;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume resume) {
        checkForNonNull(resume);
        if (isStorageFull()) {
            throw new StorageException("Storage overflowed", resume.getUuid());
        } else {
            int index = getIndex(resume.getUuid());
            if (index >= 0) {
                throw new ExistStorageException(resume.getUuid());
            } else
                insertResume(resume, index);
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndexOrNotExistStorageException(uuid);
        return getResume(index);
    }

    @Override
    public void delete(String uuid) {
        int index = getIndexOrNotExistStorageException(uuid);
        deleteResume(index);
    }

    @Override
    public void update(Resume resume) {
        checkForNonNull(resume);

        int index = getIndexOrNotExistStorageException(resume.getUuid());
        updateResume(resume, index);
    }

    private int getIndexOrNotExistStorageException(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return index;
    }

    private void checkForNonNull(Resume resume) {
        Objects.requireNonNull(resume, "Warning: Resume is null");
    }

    protected abstract void updateResume(Resume resume, int index);

    protected abstract boolean isStorageFull();

    protected abstract int getIndex(String uuid);

    protected abstract void insertResume(Resume resume, int index);

    protected abstract Resume getResume(int index);

    protected abstract void deleteResume(int index);
}
