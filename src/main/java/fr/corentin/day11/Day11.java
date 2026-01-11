package fr.corentin.day11;

import fr.corentin.utils.Day;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;

public class Day11 extends Day {

    private static final int DAY = 11;

    public Day11() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        Graph<String, DefaultEdge> graph = buildJGraphT();
        return String.valueOf(countAllPaths(graph, "you", "out", null));
    }

    @Override
    public String solvePart2() {
        Graph<String, DefaultEdge> graph = buildJGraphT();
        Set<String> required = new HashSet<>(Arrays.asList("dac", "fft"));
        return String.valueOf(countAllPaths(graph, "svr", "out", required));
    }

    private Graph<String, DefaultEdge> buildJGraphT() {
        Graph<String, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        // Parse input and build graph
        Arrays.stream(getInputLines())
            .filter(line -> !line.trim().isEmpty() && line.contains(":"))
            .forEach(line -> {
                String[] parts = line.split(":");
                String source = parts[0].trim();
                
                // Add source vertex
                graph.addVertex(source);
                
                // Add all target vertices and edges
                Arrays.stream(parts[1].trim().split("\\s+"))
                    .forEach(target -> {
                        graph.addVertex(target);
                        graph.addEdge(source, target);
                    });
            });

        return graph;
    }

    private int countAllPaths(Graph<String, DefaultEdge> graph, String start, String end, 
                               Set<String> requiredNodes) {
        // Use JGraphT's AllDirectedPaths to find all paths
        AllDirectedPaths<String, DefaultEdge> pathFinder = new AllDirectedPaths<>(graph);
        
        // Find all paths from start to end
        int maxPathLength = graph.vertexSet().size();
        List<GraphPath<String, DefaultEdge>> allPaths = pathFinder.getAllPaths(
            start, end, true, maxPathLength
        );

        // Filter paths that visit all required nodes if specified
        if (requiredNodes == null || requiredNodes.isEmpty()) {
            return allPaths.size();
        }

        return (int) allPaths.stream()
            .filter(path -> {
                Set<String> visitedNodes = new HashSet<>(path.getVertexList());
                return visitedNodes.containsAll(requiredNodes);
            })
            .count();
    }

    public static void main(String[] args) {
        new Day11().solve();
    }
}
