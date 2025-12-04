package fr.corentin.day04;

import fr.corentin.utils.Day;

import java.util.ArrayList;
import java.util.List;

public class Day04 extends Day {

    private static final int DAY = 4;

    // 8 directions: top, bottom, left, right, and 4 diagonals
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0},  {1, 1}
    };

    public Day04() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        char[][] grid = getInputAsGrid();
        return String.valueOf(findAccessibleRolls(grid).size());
    }

    @Override
    public String solvePart2() {
        char[][] grid = getInputAsGrid();
        return String.valueOf(removeAllAccessibleRolls(grid));
    }

    // Remove all accessible rolls iteratively until none remain
    private int removeAllAccessibleRolls(char[][] grid) {
        int totalRemoved = 0;
        List<Position> accessible;

        while (!(accessible = findAccessibleRolls(grid)).isEmpty()) {
            for (Position pos : accessible) {
                grid[pos.row][pos.col] = '.';
            }
            totalRemoved += accessible.size();
        }

        return totalRemoved;
    }

    // Find all paper rolls accessible by a forklift (less than 4 neighbors)
    private List<Position> findAccessibleRolls(char[][] grid) {
        List<Position> accessible = new ArrayList<>();

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '@' && countNeighbors(grid, row, col) < 4) {
                    accessible.add(new Position(row, col));
                }
            }
        }

        return accessible;
    }

    // Count '@' neighbors in the 8 adjacent positions
    private int countNeighbors(char[][] grid, int row, int col) {
        int count = 0;

        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isInBounds(grid, newRow, newCol) && grid[newRow][newCol] == '@') {
                count++;
            }
        }

        return count;
    }

    // Check if position is within grid bounds
    private boolean isInBounds(char[][] grid, int row, int col) {
        return row >= 0 && row < grid.length &&
                col >= 0 && col < grid[row].length;
    }

    private record Position(int row, int col) {
    }

    public static void main(String[] args) {
        new Day04().solve();
    }
}