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
        return new Resume[0];
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    protected void updateResume(Resume resume, int index) {

    }

    @Override
    protected boolean isStorageFull() {
        return false;
    }

    @Override
    protected int getIndex(String uuid) {
        return 0;
    }

    @Override
    protected void insertResume(Resume resume, int index) {

    }

    @Override
    protected Resume getResume(int index) {
        return null;
    }

    @Override
    protected void deleteResume(int index) {

    }
}
