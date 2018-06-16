package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

public interface Storage {

    void update(Resume r);

    void clear();

    void save(Resume r);

    Resume get(String uuid);

    void delete(String uuid);

    Resume[] getAll();

    int size();
}
