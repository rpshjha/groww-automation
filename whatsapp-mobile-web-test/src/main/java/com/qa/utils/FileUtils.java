package com.qa.utils;

import io.cucumber.java8.Fi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public void createCSV(StringBuilder builder, String fileName) {
        String filename = fileName + System.currentTimeMillis() + ".csv";
        File file = new File("./" + filename);

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(builder.toString());

            logger.info("file created at location -> " + "./" + filename);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
