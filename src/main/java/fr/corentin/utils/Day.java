package fr.corentin.utils;

public abstract class Day {

    protected final int dayNumber;
    protected final String input;

    public Day(int dayNumber) {
        this.dayNumber = dayNumber;
        this.input = FileReader.readDayInput(dayNumber);
    }

    public abstract String solvePart1();

    public abstract String solvePart2();

    public void solve() {
        System.out.println("=== Day " + String.format("%02d", dayNumber) + " ===");

        long startPart1 = System.currentTimeMillis();
        String resultPart1 = solvePart1();
        long timePart1 = System.currentTimeMillis() - startPart1;

        long startPart2 = System.currentTimeMillis();
        String resultPart2 = solvePart2();
        long timePart2 = System.currentTimeMillis() - startPart2;

        System.out.println("Part 1: " + resultPart1 + " (" + timePart1 + "ms)");
        System.out.println("Part 2: " + resultPart2 + " (" + timePart2 + "ms)");
        System.out.println();
    }

    protected String[] getInputLines() {
        return input.trim().split("\n");
    }
}