package com.urise.webapp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileUtil {

    public static List<File> getFiles(File dir) {
        List<File> result = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result.addAll(getFiles(file));
                } else {
                    result.add(file);
                }
            }
        }
        return result;
    }

    public static void deleteFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFiles(file);
                } else {
                    file.delete();
                }
            }
        }
    }

    private FileUtil() {
    }
}
