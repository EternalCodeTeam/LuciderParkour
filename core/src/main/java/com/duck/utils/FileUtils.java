package com.duck.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class FileUtils {

    public static void copyFile(InputStream inputStream, File file2) throws IOException {
        Files.copy(inputStream, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
