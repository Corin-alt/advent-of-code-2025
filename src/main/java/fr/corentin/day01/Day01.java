package fr.corentin.day01;

import fr.corentin.utils.Day;

public class Day01 extends Day {
    private static final int DAY = 1;

    private static final int START_POSITION = 50;
    private static final int MAX_VALUE = 100;

    public Day01() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        return String.valueOf(solve(false));
    }

    @Override
    public String solvePart2() {
        return String.valueOf(solve( true));
    }

    /**
     * Generic solver for both parts.
     * @param countClicks If true, count every intermediate step. Otherwise, only final positions.
     */
    private int solve(boolean countClicks) {
        int position = START_POSITION;
        int countZeros = 0;

        for (String line : getInputLines()) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // 'L' or 'R'
            char direction = line.charAt(0);
            int distance = Integer.parseInt(line.substring(1));

            if (countClicks) {
                countZeros += rotateStepByStep(position, direction, distance);
                position = rotateDirect(position, direction, distance);
            } else {
                position = rotateDirect(position, direction, distance);
                if (position == 0) countZeros++;
            }
        }
        return countZeros;
    }

    /**
     * Direct rotation without counting intermediate clicks.
     */
    private int rotateDirect(int position, char direction, int distance) {
        if (direction == 'L') {
            position = (position - distance) % MAX_VALUE;
            if (position < 0) position += MAX_VALUE;
        } else if (direction == 'R') {
            position = (position + distance) % MAX_VALUE;
        }
        return position;
    }

    /**
     * Rotation step-by-step, allowing counting each intermediate position.
     */
    private int rotateStepByStep(int position, char direction, int distance) {
        int step = (direction == 'L') ? -1 : 1;
        int countZeros = 0;

        for (int i = 0; i < distance; i++) {
            position = (position + step) % MAX_VALUE;
            if (position < 0) position += MAX_VALUE;

            if (position == 0) {
                countZeros++;
            }
        }
        return countZeros;
    }

    public static void main(String[] args) {
        new Day01().solve();
    }
}
