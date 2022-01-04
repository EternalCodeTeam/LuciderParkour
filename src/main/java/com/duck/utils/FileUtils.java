package com.duck.utils;

import com.duck.LuciderParkour;
import net.dzikoysk.cdn.source.Resource;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class FileUtils {

    public static void copyFile(InputStream inputStream, File file2) throws IOException {
        Files.copy(inputStream, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}
