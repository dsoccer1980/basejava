package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.List;

public interface Storage {

    void save(Resume resume);

    Resume get(String uuid);

    void delete(String uuid);

    void clear();

    void update(Resume resume);

    List<Resume> getAllSorted();

    int size();
}
