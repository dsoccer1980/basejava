package com.urise.webapp;


import java.io.File;


public class MainFile {

    public static void printFiles(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    printFiles(file);
                } else {
                    System.out.println(file.getName());
                }
            }
        }
    }


    public static void main(String[] args) {
        printFiles(new File("."));
    }
}
