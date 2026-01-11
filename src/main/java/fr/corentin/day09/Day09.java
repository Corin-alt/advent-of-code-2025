package fr.corentin.day09;

import fr.corentin.utils.Day;

import java.util.*;

public class Day09 extends Day {

    private static final int DAY = 9;
    private Map<Long, Boolean> insideCache;

    public Day09() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        return String.valueOf(findLargestRectangle());
    }

    @Override
    public String solvePart2() {
        return String.valueOf(findLargestRectangleWithGreen());
    }

    private long findLargestRectangle() {
        int[][] points = parsePoints();
        long maxArea = 0;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                long area = ((long) Math.abs(points[j][0] - points[i][0]) + 1) *
                           ((long) Math.abs(points[j][1] - points[i][1]) + 1);
                maxArea = Math.max(maxArea, area);
            }
        }

        return maxArea;
    }

    private long findLargestRectangleWithGreen() {
        int[][] points = parsePoints();
        insideCache = new HashMap<>();
        
        Set<Long> boundary = new HashSet<>();
        for (int i = 0; i < points.length; i++) {
            boundary.add(encode(points[i][0], points[i][1]));
            int next = (i + 1) % points.length;
            addLine(points[i][0], points[i][1], points[next][0], points[next][1], boundary);
        }

        long maxArea = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                long area = ((long) Math.abs(points[j][0] - points[i][0]) + 1) *
                           ((long) Math.abs(points[j][1] - points[i][1]) + 1);
                if (area <= maxArea) continue;

                int x1 = Math.min(points[i][0], points[j][0]);
                int x2 = Math.max(points[i][0], points[j][0]);
                int y1 = Math.min(points[i][1], points[j][1]);
                int y2 = Math.max(points[i][1], points[j][1]);

                if (isValid(x1, y1, x2, y2, boundary, points)) {
                    maxArea = area;
                }
            }
        }

        return maxArea;
    }

    private boolean isValid(int x1, int y1, int x2, int y2, Set<Long> boundary, int[][] points) {
        long totalPoints = ((long)(x2 - x1 + 1)) * (y2 - y1 + 1);
        
        if (totalPoints <= 1000) {
            for (int x = x1; x <= x2; x++) {
                for (int y = y1; y <= y2; y++) {
                    if (!isPointValid(x, y, boundary, points)) return false;
                }
            }
        } else {
            int step = (int) Math.max(1, Math.min(x2 - x1, y2 - y1) / 50);
            for (int x = x1; x <= x2; x += step) {
                if (!isPointValid(x, y1, boundary, points) || !isPointValid(x, y2, boundary, points)) return false;
            }
            for (int y = y1; y <= y2; y += step) {
                if (!isPointValid(x1, y, boundary, points) || !isPointValid(x2, y, boundary, points)) return false;
            }
            if (!isPointValid((x1 + x2) / 2, (y1 + y2) / 2, boundary, points)) return false;
        }
        
        return true;
    }

    private boolean isPointValid(int x, int y, Set<Long> boundary, int[][] points) {
        long key = encode(x, y);
        if (boundary.contains(key)) return true;
        return insideCache.computeIfAbsent(key, k -> isInside(x, y, points));
    }

    private boolean isInside(int x, int y, int[][] points) {
        int crossings = 0;
        for (int i = 0; i < points.length; i++) {
            int next = (i + 1) % points.length;
            int x1 = points[i][0], y1 = points[i][1];
            int x2 = points[next][0], y2 = points[next][1];

            if (((y1 <= y && y < y2) || (y2 <= y && y < y1)) &&
                x < x1 + (y - y1) * (x2 - x1) / (double)(y2 - y1)) {
                crossings++;
            }
        }
        return (crossings % 2) == 1;
    }

    private void addLine(int x1, int y1, int x2, int y2, Set<Long> tiles) {
        if (x1 == x2) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                tiles.add(encode(x1, y));
            }
        } else if (y1 == y2) {
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                tiles.add(encode(x, y1));
            }
        }
    }

    private int[][] parsePoints() {
        return Arrays.stream(getInputLines())
            .filter(line -> !line.trim().isEmpty())
            .map(line -> Arrays.stream(line.split(","))
                .mapToInt(s -> Integer.parseInt(s.trim()))
                .toArray())
            .toArray(int[][]::new);
    }

    private long encode(int x, int y) {
        return ((long) x << 32) | (y & 0xFFFFFFFFL);
    }

    public static void main(String[] args) {
        new Day09().solve();
    }
}
