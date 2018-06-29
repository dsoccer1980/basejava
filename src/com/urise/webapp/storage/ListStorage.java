package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;


public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Resume doGet(Object searchKey) {
        return storage.get((Integer)searchKey);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        int index = getIndex(uuid);
        if (index == -1) {
            return null;
        }
        return getIndex(uuid);
    }

    @Override
    protected void doSave(Object searchKey, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected Object getSearchKeyForSaveOrException(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(resume.getUuid());
        }
        return index;
    }
    
    @Override
    protected void doDelete(Object searchKey) {
        storage.remove(((Integer)searchKey).intValue());
    }

    @Override
    protected void doUpdate(Object searchKey, Resume resume) {
        storage.set((Integer)searchKey, resume);
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

}
