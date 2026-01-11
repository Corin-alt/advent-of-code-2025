package fr.corentin.day05;

import fr.corentin.utils.Day;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day05 extends Day {

    private static final int DAY = 5;

    public Day05() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        ParsedData data = parseInput();

        long count = data.values.stream()
                .filter(value -> isInAnyRange(value, data.ranges))
                .distinct()
                .count();

        return String.valueOf(count);
    }

    @Override
    public String solvePart2() {
        ParsedData data = parseInput();

        // Merge overlapping ranges
        List<Range<Long>> mergedRanges = mergeOverlappingRanges(data.ranges);

        // Count all unique IDs in the merged ranges
        long totalFreshIds = countIdsInRanges(mergedRanges);

        return String.valueOf(totalFreshIds);
    }

    private ParsedData parseInput() {
        List<Range<Long>> ranges = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (String line : getInputLines()) {
            if (line.trim().isEmpty()) continue;

            if (line.trim().contains("-")) {
                String[] parts = line.split("-");
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                ranges.add(new Range<>(start, end));
            } else {
                values.add(Long.parseLong(line));
            }
        }

        return new ParsedData(ranges, values);
    }


    private boolean isInAnyRange(long value, List<Range<Long>> ranges) {
        return ranges.stream().anyMatch(range -> range.contains(value));
    }

    List<Range<Long>> mergeOverlappingRanges(List<Range<Long>> ranges) {
        if (ranges.isEmpty()) return new ArrayList<>();

        // Sort ranges by their start value
        List<Range<Long>> sorted = new ArrayList<>(ranges);
        sorted.sort(Comparator.comparing(r -> r.min));

        List<Range<Long>> merged = new ArrayList<>();
        Range<Long> current = sorted.get(0);

        for (int i = 1; i < sorted.size(); i++) {
            Range<Long> next = sorted.get(i);

            // If ranges overlap or are adjacent
            if (current.max >= next.min - 1) {
                // Merge by extending current range
                current = new Range<>(
                        current.min,
                        Math.max(current.max, next.max)
                );
            } else {
                // No overlap, add the current range and move to the next
                merged.add(current);
                current = next;
            }
        }

        // Add the last range
        merged.add(current);

        return merged;
    }


    private long countIdsInRanges(List<Range<Long>> ranges) {
        return ranges.stream()
                .mapToLong(range -> range.max - range.min + 1)
                .sum();
    }

    private record ParsedData(List<Range<Long>> ranges, List<Long> values) { }

    public record Range<T extends Comparable<T>>(T min, T max) {
        public boolean contains(T value) {
            return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
        }
    }

    public static void main(String[] args) {
        new Day05().solve();
    }
}