package fr.corentin.day03;

import fr.corentin.utils.Day;

public class Day03 extends Day {

    private static final int DAY = 3;

    public Day03() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        long total = 0;

        for (String line : getInputLines()) {
            total += findMaxJoltage(line.trim(), 2);
        }

        return String.valueOf(total);
    }

    @Override
    public String solvePart2() {
        long total = 0;

        for (String line : getInputLines()) {
            total += findMaxJoltage(line.trim(), 12);
        }

        return String.valueOf(total);
    }

    private long findMaxJoltage(String batteries, int count) {
        int n = batteries.length();
        int toRemove = n - count;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char current = batteries.charAt(i);

            // Remove smaller digits from the end if we can still remove more and if the current digit is larger
            while (!result.isEmpty() && toRemove > 0 && result.charAt(result.length() - 1) < current) {
                result.deleteCharAt(result.length() - 1);
                toRemove--;
            }

            result.append(current);
        }

        // Remove remaining digits from the end if needed
        while (toRemove > 0) {
            result.deleteCharAt(result.length() - 1);
            toRemove--;
        }

        return Long.parseLong(result.toString());
    }

    public static void main(String[] args) {
        new Day03().solve();
    }
}