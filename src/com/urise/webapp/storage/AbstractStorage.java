package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.Objects;

public abstract class AbstractStorage implements Storage {

    protected static final Comparator<Resume> COMPARATOR_RESUME = (o1, o2) -> {
        int index = o1.getFullName().compareTo(o2.getFullName());
        if (index == 0) {
            return o1.getUuid().compareTo(o2.getUuid());
        }
        return index;
    };

    @Override
    public void save(Resume resume) {
        checkForNonNull(resume);
        Object searchKey = getIfNotExist(resume);
        doSave(searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        Object searchKey = getIfExist(uuid);
        return doGet(searchKey);
    }

    @Override
    public void delete(String uuid) {
        Object searchKey = getIfExist(uuid);
        doDelete(searchKey);
    }

    @Override
    public void update(Resume resume) {
        checkForNonNull(resume);
        Object searchKey = getIfExist(resume.getUuid());
        doUpdate(searchKey, resume);
    }

    protected Object getIfNotExist(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        }
        return searchKey;
    }

    private void checkForNonNull(Resume resume) {
        Objects.requireNonNull(resume, "Warning: Resume is null");
    }

    private Object getIfExist(String uuid) {
        Object searchKey = getSearchKey(uuid);

        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract boolean isExist(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void doSave(Object searchKey, Resume resume);

    protected abstract Resume doGet(Object searchKey);

    protected abstract void doUpdate(Object searchKey, Resume resume);

    protected abstract void doDelete(Object searchKey);

}
