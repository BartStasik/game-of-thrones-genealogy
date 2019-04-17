package com.got.genealogy;

import com.got.genealogy.core.family.person.Relation;
import com.got.genealogy.core.processor.FileProcessor;

import java.io.*;
import java.nio.file.Files;


public class TestFileProcessor {
    public static void main(String[] args) {
        String filename = "InputFile.txt";

        ClassLoader classLoader = new TestFileProcessor()
                .getClass()
                .getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        FileProcessor processor = new FileProcessor();
        processor.loadFile(file.getAbsolutePath());

        processor.getFamily()
                .printGraph(new Relation("______"));
    }
}
