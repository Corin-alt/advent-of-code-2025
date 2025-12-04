package fr.corentin.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileReader {

    public static String readFile(String packagePath, String filename) {
        try {
            Path path = Paths.get("src/main/java/" + packagePath + "/" + filename);
            return String.join(" ", Files.readAllLines(path));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }
    }

    public static List<String> readLines(String packagePath, String filename) {
        try {
            Path path = Paths.get("src/main/java/" + packagePath + "/" + filename);
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }
    }

    public static String readDayInput(int day) {
        String dayFolder = String.format("fr/corentin/day%02d", day);
        return readFile(dayFolder, "input.txt");
    }

    public static List<String> readDayInputLines(int day) {
        String dayFolder = String.format("fr/corentin/day%02d", day);
        return readLines(dayFolder, "input.txt");
    }

    public static char[][] readDayInputAsGrid(int day) {
        List<String> lines = readDayInputLines(day);
        if (lines.isEmpty()) {
            return new char[0][0];
        }

        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }
}