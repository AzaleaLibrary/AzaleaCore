package com.azalealibrary.azaleacore.util;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class FileUtil {

    public static void copyDirectory(File from, File to) {
        if (!to.exists()) to.mkdir();

        for (String file : Objects.requireNonNull(from.list())) {
            File source = new File(from, file);
            File destination = new File(to, file);

            if (source.isDirectory()) {
                copyDirectory(source, destination);
            } else {
                copyFile(source, destination);
            }
        }
    }

    public static void copyFile(File from, File to) {
        try (InputStream input = new FileInputStream(from); OutputStream output = new FileOutputStream(to)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = input.read(buf)) > 0) {
                output.write(buf, 0, length);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    public static List<File> directories(File directory) {
        return Stream.of(Objects.requireNonNull(directory.listFiles())).filter(File::isDirectory).toList();
    }
}
