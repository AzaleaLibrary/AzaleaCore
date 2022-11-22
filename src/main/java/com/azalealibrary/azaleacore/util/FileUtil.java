package com.azalealibrary.azaleacore.util;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class FileUtil {

    public static final File ROOMS = new File(Bukkit.getWorldContainer(), "/azalea/rooms");
    public static final File MAPS = new File(Bukkit.getWorldContainer(), "/azalea/maps");

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

    public static List<File> maps() {
        return directories(MAPS);
    }

    public static List<File> rooms() {
        return directories(ROOMS);
    }

    public static @Nullable File map(String map) {
        return maps().stream().filter(file -> file.getName().equals(map)).findFirst().orElse(null);
    }

    public static @Nullable File room(String room) {
        return rooms().stream().filter(file -> file.getName().equals(room)).findFirst().orElse(null);
    }

    public static @Nonnull File insureExists(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) { }
        }
        return file;
    }
}
