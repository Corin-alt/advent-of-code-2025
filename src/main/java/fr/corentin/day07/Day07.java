package fr.corentin.day07;

import fr.corentin.utils.Day;

import java.util.*;

public class Day07 extends Day {

    private static final int DAY = 7;

    public Day07() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        return String.valueOf(countBeamSplits());
    }

    @Override
    public String solvePart2() {
        return String.valueOf(countTimelines());
    }

    private long countTimelines() {
        char[][] grid = getInputAsGrid();
        int[] start = findStart(grid);
        return countPathsFrom(grid, start[0], start[1], grid[0].length, new HashMap<>());
    }

    private long countPathsFrom(char[][] grid, int row, int col, int maxCols, Map<Integer, Long> memo) {
        if (row >= grid.length) return 1L;

        int pos = row * maxCols + col;
        if (memo.containsKey(pos)) return memo.get(pos);

        int nextRow = row + 1;
        if (nextRow >= grid.length || col >= grid[nextRow].length) {
            memo.put(pos, 1L);
            return 1L;
        }

        long count = grid[nextRow][col] == '^' 
            ? (col > 0 ? countPathsFrom(grid, nextRow, col - 1, maxCols, memo) : 0)
            + (col + 1 < grid[nextRow].length ? countPathsFrom(grid, nextRow, col + 1, maxCols, memo) : 0)
            : countPathsFrom(grid, nextRow, col, maxCols, memo);

        memo.put(pos, count);
        return count;
    }

    private int countBeamSplits() {
        char[][] grid = getInputAsGrid();
        int[] start = findStart(grid);
        int maxCols = grid[0].length;
        
        Set<Integer> hitSplitters = new HashSet<>();
        Set<Integer> processed = new HashSet<>();
        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(start[0] * maxCols + start[1]);

        while (!queue.isEmpty()) {
            int pos = queue.poll();
            if (!processed.add(pos)) continue;

            int row = pos / maxCols;
            int col = pos % maxCols;

            while (++row < grid.length && col < grid[row].length) {
                if (grid[row][col] == '^') {
                    hitSplitters.add(row * maxCols + col);
                    if (col > 0) queue.add(row * maxCols + (col - 1));
                    if (col + 1 < grid[row].length) queue.add(row * maxCols + (col + 1));
                    break;
                }
            }
        }

        return hitSplitters.size();
    }

    private int[] findStart(char[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == 'S') return new int[]{row, col};
            }
        }
        throw new IllegalStateException("Start position 'S' not found");
    }

    public static void main(String[] args) {
        new Day07().solve();
    }
}
