package fr.corentin.day02;

import fr.corentin.utils.Day;

public class Day02 extends Day {

    private static final int DAY = 2;

    public Day02() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        return String.valueOf(calculateInvalidIdsSum(this::isInvalidIdPart1));
    }

    @Override
    public String solvePart2() {
        return String.valueOf(calculateInvalidIdsSum(this::isInvalidIdPart2));
    }

    private long calculateInvalidIdsSum(InvalidIdChecker checker) {
        String[] ranges = getContent().split(",");
        long totalSum = 0;

        for (String range : ranges) {
            String[] parts = range.trim().split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);

            for (long id = start; id <= end; id++) {
                if (checker.isInvalid(id)) {
                    totalSum += id;
                }
            }
        }

        return totalSum;
    }

    private boolean isInvalidIdPart1(long id) {
        String idStr = String.valueOf(id);
        int len = idStr.length();

        if (len % 2 != 0) {
            return false;
        }

        int halfLen = len / 2;
        long divisor = 1;
        for (int i = 0; i < halfLen; i++) {
            divisor *= 10;
        }

        long firstHalf = id / divisor;
        long secondHalf = id % divisor;

        return firstHalf == secondHalf;
    }

    private boolean isInvalidIdPart2(long id) {
        String idStr = String.valueOf(id);
        int len = idStr.length();

        char[] chars = idStr.toCharArray();

        // Only test pattern lengths that divide the total length evenly
        for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
            if (len % patternLen != 0) {
                continue;
            }

            if (isRepeatedPattern(chars, len, patternLen)) {
                return true;
            }
        }

        return false;
    }

    private boolean isRepeatedPattern(char[] chars, int len, int patternLen) {
        // Compare characters directly using modulo to check pattern repetition
        for (int i = patternLen; i < len; i++) {
            if (chars[i] != chars[i % patternLen]) {
                return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    private interface InvalidIdChecker {
        boolean isInvalid(long id);
    }

    public static void main(String[] args) {
        new Day02().solve();
    }
}