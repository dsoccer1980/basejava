package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    public void clear() {

    }

    @Override
    public Resume[] getAll() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return null;
    }

    @Override
    protected void doSave(Object indexOrKey, Resume resume) {

    }

    @Override
    protected Resume doGet(Object indexOrKey) {
        return null;
    }

    @Override
    protected Object getSearchKeyForSaveOrException(Resume resume) {
        return null;
    }

    @Override
    protected void doDelete(Object indexOrKey) {

    }

    @Override
    protected void doUpdate(Object indexOrKey, Resume resume) {

    }

}
