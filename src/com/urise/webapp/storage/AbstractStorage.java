package com.urise.webapp.storage;

import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Objects;

public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume resume) {
        checkForNonNull(resume);
        Object searchKey = getSearchKeyForSaveOrException(resume);
        doSave(searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getSearchKeyOrNotExistStorageException(uuid);
        return doGet(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getSearchKeyOrNotExistStorageException(uuid);
        doDelete(searchKey);
    }

    @Override
    public void update(Resume resume) {
        checkForNonNull(resume);
        Object searchKey = getSearchKeyOrNotExistStorageException(resume.getUuid());
        doUpdate(searchKey, resume);
    }

    private void checkForNonNull(Resume resume) {
        Objects.requireNonNull(resume, "Warning: Resume is null");
    }

    private Object getSearchKeyOrNotExistStorageException(String uuid) {
        Object searchKey = getSearchKey(uuid);

        if (searchKey == null) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract Object getSearchKey(String uuid);

    protected abstract void doSave(Object searchKey, Resume resume);

    protected abstract Resume doGet(Object searchKey);

    protected abstract Object getSearchKeyForSaveOrException(Resume resume);

    protected abstract void doUpdate(Object searchKey, Resume resume);

    protected abstract void doDelete(Object searchKey);

}
