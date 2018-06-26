package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public interface Storage {

    void save(Resume resume);

    Resume get(String uuid);

    void delete(String uuid);

    void clear();

    void update(Resume resume);

    Resume[] getAll();

    int size();
}
