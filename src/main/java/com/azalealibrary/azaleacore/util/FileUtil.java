package com.azalealibrary.azaleacore.util;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class FileUtil {

    public static final File ROOMS = new File(Bukkit.getWorldContainer(), "/rooms");
    public static final File TEMPLATES = new File(Bukkit.getWorldContainer(), "/templates");

    public static void copyDirectory(File from, File to) {
        if (!to.exists()) {
            to.mkdir();
        }

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

    public static void delete(File source) {
        if (source.isDirectory()) {
            File[] allContents = source.listFiles();

            if (allContents != null) {
                for (File file : allContents) {
                    delete(file);
                }
            }
        }

        source.delete();
    }

    public static List<File> directories(File directory) {
        return Stream.of(Objects.requireNonNull(directory.listFiles())).filter(File::isDirectory).toList();
    }

    public static List<File> templates() {
        return directories(TEMPLATES);
    }

    public static List<File> rooms() {
        return directories(ROOMS);
    }

    public static File template(String template) {
        return templates().stream().filter(file -> file.getName().equals(template)).findFirst().orElse(null);
    }

    public static File room(String room) {
        return rooms().stream().filter(file -> file.getName().equals(room)).findFirst().orElse(null);
    }

    public static File insureExists(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) { }
        }
        return file;
    }
}
