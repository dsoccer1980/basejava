package com.urise.webapp.exception;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException(String uuid) {
        super("Resume with uuid = " + uuid + " does not exist", uuid);
    }
}
