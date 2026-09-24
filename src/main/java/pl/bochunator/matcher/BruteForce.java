package pl.bochunator.matcher;

import org.jgrapht.Graph;

import java.util.*;

public class BruteForce<Vertex, Edge> implements Matcher<Vertex, Edge> {
    private final Graph<Vertex, Edge> patternGraph;
    private final Graph<Vertex, Edge> targetGraph;
    private final List<Vertex> patternVertices;
    private final List<Vertex> targetVertices;
    private final List<Map<Vertex, Vertex>> matches = new ArrayList<>();

    public BruteForce(Graph<Vertex, Edge> patternGraph, Graph<Vertex, Edge> targetGraph) {
        this.patternGraph = patternGraph;
        this.targetGraph = targetGraph;

        this.patternVertices = new ArrayList<>(patternGraph.vertexSet());
        this.targetVertices = new ArrayList<>(targetGraph.vertexSet());
    }

    @Override
    public List<Map<Vertex, Vertex>> findMatches() {
        matches.clear();

        backtrack(new HashMap<>(), new HashSet<>(), 0);

        return matches;
    }

    private void backtrack(
            Map<Vertex, Vertex> mapping,
            Set<Vertex> used,
            int depth
    ) {
        if (depth == patternVertices.size()) {
            if (isMatch(mapping)) {
                matches.add(new HashMap<>(mapping));
            }
            return;
        }
        Vertex patternVertexToMatch = patternVertices.get(depth);

        for (Vertex targetVertex : targetVertices) {
            if (used.contains(targetVertex)) {
                continue;
            }
            mapping.put(patternVertexToMatch, targetVertex);
            used.add(targetVertex);

            backtrack(
                    mapping,
                    used,
                    depth + 1
            );
            mapping.remove(patternVertexToMatch);
            used.remove(targetVertex);
        }
    }

    private boolean isMatch(Map<Vertex, Vertex> mapping) {

        Set<String> checkedPairs = new HashSet<>();
        for (Edge e : patternGraph.edgeSet()) {

            Vertex p1 = patternGraph.getEdgeSource(e);
            Vertex p2 = patternGraph.getEdgeTarget(e);

            String key = p1.toString() + "#" + p2.toString();
            String keyReverse = p2.toString() + "#" + p1.toString();
            if (checkedPairs.contains(key) || checkedPairs.contains(keyReverse)) {
                continue;
            }
            checkedPairs.add(key);

            Vertex t1 = mapping.get(p1);
            Vertex t2 = mapping.get(p2);

            int patternEdgeCount = patternGraph.getAllEdges(p1, p2).size();
            int targetEdgeCount  = targetGraph.getAllEdges(t1, t2).size();

            if (targetEdgeCount < patternEdgeCount) {
                return false;
            }
        }

        return true;
    }
}
