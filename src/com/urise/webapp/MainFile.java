package com.urise.webapp;


import java.io.File;


public class MainFile {

    public static void printFiles(File dir, String offset) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println(offset + "D:" + file.getName());
                    printFiles(file, offset + "  ");

                } else {
                    System.out.println(offset + "   " + "F:" + file.getName());
                }
            }
        }
    }

    public static void main(String[] args) {
        printFiles(new File("."), "");
    }
}
