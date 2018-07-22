package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamStorage {

    void doWrite(OutputStream os, Resume r) throws IOException;

    Resume doRead(InputStream is) throws IOException;
}
