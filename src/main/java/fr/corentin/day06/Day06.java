package fr.corentin.day06;

import fr.corentin.utils.Day;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class Day06 extends Day {

    private static final int DAY = 6;

    public Day06() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        return String.valueOf(solve(this::parseVertical));
    }

    @Override
    public String solvePart2() {
        return String.valueOf(solve(this::parseHorizontal));
    }

    private long solve(BiFunction<char[][], int[], List<Long>> parser) {
        char[][] grid = getInputAsGrid();
        List<MathProblem> problems = new ArrayList<>();
        int maxCols = Arrays.stream(grid).mapToInt(row -> row.length).max().orElse(0);

        for (int col = 0; col < maxCols; ) {
            if (isBlank(grid, col)) {
                col++;
                continue;
            }
            int start = col;
            while (col < maxCols && !isBlank(grid, col)) col++;
            problems.add(parseProblem(grid, parser.apply(grid, new int[]{start, col})));
        }

        return problems.stream().mapToLong(MathProblem::eval).sum();
    }

    private List<Long> parseVertical(char[][] grid, int[] range) {
        List<Long> numbers = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            StringBuilder num = new StringBuilder();
            for (int col = range[0]; col < range[1]; col++) {
                char ch = getChar(grid, row, col);
                if (Character.isDigit(ch)) num.append(ch);
            }
            if (!num.isEmpty()) numbers.add(Long.parseLong(num.toString()));
        }
        return numbers;
    }

    private List<Long> parseHorizontal(char[][] grid, int[] range) {
        List<Long> numbers = new ArrayList<>();
        for (int col = range[1] - 1; col >= range[0]; col--) {
            StringBuilder num = new StringBuilder();
            for (int row = 0; row < grid.length; row++) {
                char ch = getChar(grid, row, col);
                if (Character.isDigit(ch)) num.append(ch);
            }
            if (!num.isEmpty()) numbers.add(Long.parseLong(num.toString()));
        }
        return numbers;
    }

    private MathProblem parseProblem(char[][] grid, List<Long> numbers) {
        char op = ' ';
        for (char[] row : grid) {
            for (char ch : row) {
                if (ch == '*' || ch == '+') {
                    op = ch;
                    break;
                }
            }
            if (op != ' ') break;
        }
        return new MathProblem(numbers, op);
    }

    private char getChar(char[][] grid, int row, int col) {
        return col < grid[row].length ? grid[row][col] : ' ';
    }

    private boolean isBlank(char[][] grid, int col) {
        return IntStream.range(0, grid.length).allMatch(row -> getChar(grid, row, col) == ' ');
    }

    private record MathProblem(List<Long> numbers, char op) {

        long eval() {
                return numbers.stream().reduce((a, b) -> op == '*' ? a * b : a + b).orElse(0L);
            }
        }

    public static void main(String[] args) {
        new Day06().solve();
    }
}
