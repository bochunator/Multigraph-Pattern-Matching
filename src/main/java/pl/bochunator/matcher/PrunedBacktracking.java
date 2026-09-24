package pl.bochunator.matcher;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.*;

public class PrunedBacktracking<Vertex, Edge> implements Matcher<Vertex, Edge> {
    private final Graph<Vertex, Edge> patternGraph;
    private final Graph<Vertex, Edge> targetGraph;
    private final List<Vertex> patternVertices;
    private final List<Vertex> targetVertices;

    private final List<Map<Vertex, Vertex>> matches = new ArrayList<>();

    public PrunedBacktracking(Graph<Vertex, Edge> patternGraph, Graph<Vertex, Edge> targetGraph) {
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
            matches.add(new HashMap<>(mapping));
            return;
        }

        Vertex patternVertexToMatch = patternVertices.get(depth);

        for (Vertex targetVertex : targetVertices) {
            if (used.contains(targetVertex)) {
                continue;
            }

            mapping.put(patternVertexToMatch, targetVertex);
            used.add(targetVertex);
            if (isFeasible(mapping, patternVertexToMatch, targetVertex)) {
                backtrack(
                        mapping,
                        used,
                        depth + 1
                );
            }
            mapping.remove(patternVertexToMatch);
            used.remove(targetVertex);
        }
    }

    private boolean isFeasible(
            Map<Vertex, Vertex> mapping,
            Vertex patternVertexToMatch,
            Vertex targetVertex
    ) {
        for (Edge edge : patternGraph.edgesOf(patternVertexToMatch)) {
            Vertex neighbour =
                    Graphs.getOppositeVertex(patternGraph, edge, patternVertexToMatch);

            if (!mapping.containsKey(neighbour)) {
                continue;
            }
            Vertex mappedNeighbour = mapping.get(neighbour);

            int patternEdgeCount = patternGraph.getAllEdges(patternVertexToMatch, neighbour).size();
            int targetEdgeCount  = targetGraph.getAllEdges(targetVertex, mappedNeighbour).size();

            if (targetEdgeCount < patternEdgeCount) {
                return false;
            }
        }

        return true;
    }
}
