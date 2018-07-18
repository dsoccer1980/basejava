package com.urise.webapp;

import com.urise.webapp.util.FileUtil;

import java.io.File;
import java.util.List;

public class MainFile {

    public static void main(String[] args) {
        File dir = new File(".");

        List<File> files = FileUtil.getFiles(dir);
        files.forEach(System.out::println);

    }
}
