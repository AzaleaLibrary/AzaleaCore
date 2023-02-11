package net.azalealibrary.azaleacore.util;

import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public final class FileUtil {

    private static final File PLAYGROUNDS = new File(Bukkit.getWorldContainer(), "/azalea/playgrounds");
    private static final File MAPS = new File(Bukkit.getWorldContainer(), "/azalea/maps");

    public static void copyDirectory(File from, File to) {
        if (!to.exists()) {
            to.mkdirs();
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

    public static @Nonnull File insureExists(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) { }
        }
        return file;
    }

    public static List<File> directories(File directory) {
        return Stream.of(Objects.requireNonNull(directory.listFiles())).filter(File::isDirectory).toList();
    }

    public static List<File> getMaps() {
        return directories(MAPS);
    }

    public static List<File> getPlaygrounds() {
        return directories(PLAYGROUNDS);
    }

    public static File getMap(String name) {
        return new File(MAPS, name);
    }

    public static File getPlayground(String name) {
        return new File(PLAYGROUNDS, name);
    }
}
