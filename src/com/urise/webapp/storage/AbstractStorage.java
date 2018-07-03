package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.Comparator;
import java.util.Objects;

public abstract class AbstractStorage<SK> implements Storage {

    protected static final Comparator<Resume> COMPARATOR_RESUME = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    @Override
    public void save(Resume resume) {
        checkForNonNull(resume);
        SK searchKey = getIfNotExist(resume);
        doSave(searchKey, resume);
    }

    @Override
    public Resume get(String uuid) {
        SK searchKey = getIfExist(uuid);
        return doGet(searchKey);
    }

    @Override
    public void delete(String uuid) {
        SK searchKey = getIfExist(uuid);
        doDelete(searchKey);
    }

    @Override
    public void update(Resume resume) {
        checkForNonNull(resume);
        SK searchKey = getIfExist(resume.getUuid());
        doUpdate(searchKey, resume);
    }

    private SK getIfNotExist(Resume resume) {
        SK searchKey = getSearchKey(resume.getUuid());
        if (isExist(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        }
        return searchKey;
    }

    private void checkForNonNull(Resume resume) {
        Objects.requireNonNull(resume, "Warning: Resume is null");
    }

    private SK getIfExist(String uuid) {
        SK searchKey = getSearchKey(uuid);

        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract boolean isExist(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract void doSave(SK searchKey, Resume resume);

    protected abstract Resume doGet(SK searchKey);

    protected abstract void doUpdate(SK searchKey, Resume resume);

    protected abstract void doDelete(SK searchKey);

}
