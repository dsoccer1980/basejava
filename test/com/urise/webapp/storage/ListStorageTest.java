package com.urise.webapp.storage;


import org.junit.Assert;

public class ListStorageTest extends AbstractStorageTest {

    public ListStorageTest() {
        super(new ListStorage());
    }

    @Override
    public void saveOverFlow() throws Exception {
        Assert.assertTrue(true);
    }
}