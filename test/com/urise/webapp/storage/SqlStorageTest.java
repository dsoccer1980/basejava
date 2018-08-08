package com.urise.webapp.storage;


import com.urise.webapp.Config;

public class SqlStorageTest extends AbstractStorageTest {
    private static final Config config = Config.get();

    public SqlStorageTest() {
        super(new SqlStorage(config.getDbUrl(), config.getDbUser(), config.getDbPassword()));
    }
}