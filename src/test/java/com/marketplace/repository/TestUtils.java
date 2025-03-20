package com.marketplace.repository;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {
    public static Path createTempDirectory() throws Exception {
        return Files.createTempDirectory("marketplace-test");
    }

    public static void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
