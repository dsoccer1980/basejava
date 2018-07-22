package com.urise.webapp.storage;


import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class PathStorage extends AbstractStorage<Path> {
    private Path directory;
    private StreamStorage streamStorage;

    protected PathStorage(String dir, StreamStorage streamStorage) {
        Objects.requireNonNull(dir, "directory must not be null");
        Objects.requireNonNull(streamStorage, "streamStorage must not be null");
        directory = Paths.get(dir);
        this.streamStorage = streamStorage;
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException("Path delete error", null, e);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return Paths.get(directory.toString(), uuid);
    }

    @Override
    protected void doUpdate(Path file, Resume r) {
        try {
            doWrite(new BufferedOutputStream(Files.newOutputStream(file)), r);
        } catch (IOException e) {
            throw new StorageException("Path write error", file.getFileName().toString(), e);
        }
    }

    @Override
    protected boolean isExist(Path file) {
        return Files.exists(file);
    }

    @Override
    protected void doSave(Path file, Resume r) {
        try {
            Files.createFile(file);
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + file.toAbsolutePath(), file.getFileName().toString(), e);
        }
        doUpdate(file, r);
    }

    @Override
    protected Resume doGet(Path file) {
        try {
            return doRead(new BufferedInputStream(Files.newInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Path read error", file.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path file) {
        try {
            if (!Files.deleteIfExists(file)) {
                throw new StorageException("Path delete error", file.getFileName().toString());
            }
        } catch (IOException e) {
            throw new StorageException("Path delete error", file.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> getCopyStorage() {
        try {
            return Files.list(directory).map(this::doGet).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Directory read error", null, e);
        }
    }

    protected Resume doRead(InputStream is) throws IOException {
        return streamStorage.doRead(is);
    }

    protected void doWrite(OutputStream os, Resume r) throws IOException {
        streamStorage.doWrite(os, r);
    }
}
