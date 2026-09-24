package pl.bochunator.matcher;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class MatcherProvider {

    public Matcher<Integer, DefaultEdge> create(
            String algorithm,
            Graph<Integer, DefaultEdge> pattern,
            Graph<Integer, DefaultEdge> graph) {

        return switch (algorithm) {
            case "BruteForce" -> new BruteForce<>(pattern, graph);
            case "PrunedBacktracking" -> new PrunedBacktracking<>(pattern, graph);
            case "VF2" -> new VF2<>(pattern, graph);
            case "Ullmann" -> new Ullmann<>(pattern, graph);
            default -> throw new IllegalArgumentException("Not found parameter name for algoritm " + algorithm);
        };
    }
}