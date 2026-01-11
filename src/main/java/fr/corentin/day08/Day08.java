package fr.corentin.day08;

import fr.corentin.utils.Day;

import java.util.*;
import java.util.stream.IntStream;

public class Day08 extends Day {

    private static final int DAY = 8;

    public Day08() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        return String.valueOf(calculateCircuitProduct());
    }

    @Override
    public String solvePart2() {
        return String.valueOf(findLastConnectionProduct());
    }

    private long findLastConnectionProduct() {
        int[][] points = parsePoints();
        UnionFind uf = new UnionFind(points.length);
        Edge[] edges = buildSortedEdges(points);

        int lastFrom = -1, lastTo = -1;
        for (Edge e : edges) {
            if (uf.union(e.from, e.to)) {
                lastFrom = e.from;
                lastTo = e.to;
                if (uf.components == 1) break;
            }
        }

        return (long) points[lastFrom][0] * points[lastTo][0];
    }

    private long calculateCircuitProduct() {
        int[][] points = parsePoints();
        UnionFind uf = new UnionFind(points.length);
        Edge[] edges = buildSortedEdges(points);

        int limit = Math.min(points.length == 20 ? 10 : 1000, edges.length);
        for (int i = 0; i < limit; i++) {
            uf.union(edges[i].from, edges[i].to);
        }

        int[] sizes = new int[points.length];
        for (int i = 0; i < points.length; i++) {
            sizes[uf.find(i)]++;
        }

        Arrays.sort(sizes);
        return (long) sizes[sizes.length - 1] * sizes[sizes.length - 2] * sizes[sizes.length - 3];
    }

    private int[][] parsePoints() {
        return Arrays.stream(getInputLines())
            .filter(line -> !line.trim().isEmpty())
            .map(line -> Arrays.stream(line.split(","))
                .mapToInt(s -> Integer.parseInt(s.trim()))
                .toArray())
            .toArray(int[][]::new);
    }

    private Edge[] buildSortedEdges(int[][] points) {
        int n = points.length;
        List<Edge> edges = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = (long) points[i][0] - points[j][0];
                long dy = (long) points[i][1] - points[j][1];
                long dz = (long) points[i][2] - points[j][2];
                edges.add(new Edge(i, j, Math.sqrt(dx * dx + dy * dy + dz * dz)));
            }
        }
        
        edges.sort(Comparator.comparingDouble(e -> e.dist));
        return edges.toArray(new Edge[0]);
    }

    private static class Edge {
        final int from, to;
        final double dist;
        Edge(int from, int to, double dist) {
            this.from = from;
            this.to = to;
            this.dist = dist;
        }
    }

    private static class UnionFind {
        int[] parent, rank;
        int components;

        UnionFind(int size) {
            parent = IntStream.range(0, size).toArray();
            rank = new int[size];
            components = size;
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return false;

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            components--;
            return true;
        }
    }

    public static void main(String[] args) {
        new Day08().solve();
    }
}
