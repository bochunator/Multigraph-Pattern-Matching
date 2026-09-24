package pl.bochunator.matcher;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.*;

public class VF2<Vertex, Edge> implements Matcher<Vertex, Edge> {

    private final Graph<Vertex, Edge> patternGraph;
    private final Graph<Vertex, Edge> targetGraph;
    private final Map<Vertex, Integer> patternFrontier = new HashMap<>();
    private final Map<Vertex, Integer> targetFrontier = new HashMap<>();
    private final List<Map<Vertex, Vertex>> matches = new ArrayList<>();

    public VF2(Graph<Vertex, Edge> patternGraph, Graph<Vertex, Edge> targetGraph) {
        this.patternGraph = patternGraph;
        this.targetGraph = targetGraph;
        for (Vertex patternVertex : patternGraph.vertexSet()) {
            patternFrontier.put(patternVertex, 0);
        }
        for (Vertex targetVertex : targetGraph.vertexSet()) {
            targetFrontier.put(targetVertex, 0);
        }
    }

    private long statesVisited = 0;
    private long prunedCandidates = 0;

    @Override
    public long getBacktracks() {
        return statesVisited;
    }

    @Override
    public long getPruned() {
        return prunedCandidates;
    }

    @Override
    public List<Map<Vertex, Vertex>> findMatches() {
        matches.clear();
        backtrack(new HashMap<>(), 1);
        return matches;
    }

    private void backtrack(Map<Vertex, Vertex> mapping, int depth) {
        statesVisited++;
        if (mapping.size() == patternGraph.vertexSet().size()) {
            matches.add(new HashMap<>(mapping));
            return;
        }
        Vertex patternVertexToMatch = nextPatternVertex(mapping);

        for (Vertex targetVertex  : nextTargetCandidates(mapping)) {

            if (!feasible(patternVertexToMatch, targetVertex, mapping)) {
                prunedCandidates++;
                continue;
            }
            mapping.put(patternVertexToMatch, targetVertex);
            addToPatternFrontier(patternVertexToMatch, depth);
            addToTargetFrontier(targetVertex, depth);

            backtrack(mapping, depth + 1);

            restorePatternFrontier(depth);
            restoreTargetFrontier(depth);
            mapping.remove(patternVertexToMatch);
        }
    }

    private Vertex nextPatternVertex(Map<Vertex, Vertex> mapping) {

        for(Vertex v : patternFrontier.keySet()) {

            if(mapping.containsKey(v)) {
                continue;
            }

            if(patternFrontier.get(v) > 0) {
                return v;
            }
        }

        for (Vertex v : patternGraph.vertexSet()) {

            if (!mapping.containsKey(v)) {
                return v;
            }
        }
        return null;
    }

    private Set<Vertex> nextTargetCandidates(Map<Vertex, Vertex> mapping) {
        Set<Vertex> result = new HashSet<>();
        Set<Vertex> used = new HashSet<>(mapping.values());

        if (!isPatternFrontierEmpty(mapping)) {
            for (Map.Entry<Vertex, Integer> entry : targetFrontier.entrySet()) {
                if (entry.getValue() > 0 && !used.contains(entry.getKey())) {
                    result.add(entry.getKey());
                }
            }
            if (!result.isEmpty()) {
                return result;
            }
        }

        for (Vertex v : targetGraph.vertexSet()) {
            if (!used.contains(v)) {
                result.add(v);
            }
        }
        return result;
    }

    private boolean isPatternFrontierEmpty(Map<Vertex, Vertex> mapping) {
        for (Map.Entry<Vertex, Integer> entry : patternFrontier.entrySet()) {
            if (entry.getValue() > 0 && !mapping.containsKey(entry.getKey())) {
                return false;
            }
        }
        return true;
    }

    private boolean feasible(Vertex patternVertexToMatch,
                             Vertex targetVertex,
                             Map<Vertex, Vertex> mapping) {
        return checkSelfLoops(patternVertexToMatch, targetVertex, mapping)
                && checkDegree(patternVertexToMatch, targetVertex)
                && checkMappedNeighboursMultigraph(patternVertexToMatch, targetVertex, mapping)
                && checkEdgeMultiplicity(patternVertexToMatch, targetVertex, mapping);
    }

    private boolean checkSelfLoops(Vertex patternVertexToMatch, Vertex targetVertex, Map<Vertex, Vertex> mapping) {
        int patternLoops = patternGraph.getAllEdges(patternVertexToMatch, patternVertexToMatch).size();
        int targetLoops = targetGraph.getAllEdges(targetVertex, targetVertex).size();
        return targetLoops >= patternLoops;
    }

    private boolean checkDegree(Vertex patternVertexToMatch, Vertex targetVertex) {
        return patternGraph.degreeOf(patternVertexToMatch) <= targetGraph.degreeOf(targetVertex);
    }

    private boolean checkMappedNeighboursMultigraph(Vertex patternVertex, Vertex targetVertex, Map<Vertex, Vertex> mapping) {
        for (Edge edge : patternGraph.edgesOf(patternVertex)) {
            Vertex patternNeighbour = Graphs.getOppositeVertex(patternGraph, edge, patternVertex);

            if (patternNeighbour.equals(patternVertex)) {
                continue;
            }

            Vertex mappedNeighbour = mapping.get(patternNeighbour);
            if (mappedNeighbour == null)
                continue;
            int patternEdgeCount = patternGraph.getAllEdges(patternVertex, patternNeighbour).size();
            int targetEdgeCount = targetGraph.getAllEdges(targetVertex, mappedNeighbour).size();

            if (targetEdgeCount < patternEdgeCount) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEdgeMultiplicity(Vertex patternVertex, Vertex targetVertex, Map<Vertex, Vertex> mapping) {
        Set<Vertex> uniquePatternNeighbours = new HashSet<>();
        for (Edge edge : patternGraph.edgesOf(patternVertex)) {
            uniquePatternNeighbours.add(Graphs.getOppositeVertex(patternGraph, edge, patternVertex));
        }

        for (Vertex patternNeighbour : uniquePatternNeighbours) {
            if (patternNeighbour.equals(patternVertex)) continue;

            int requiredEdges = patternGraph.getAllEdges(patternVertex, patternNeighbour).size();
            Vertex mappedNeighbour = mapping.get(patternNeighbour);

            if (mappedNeighbour != null) {
                if (targetGraph.getAllEdges(targetVertex, mappedNeighbour).size() < requiredEdges) {
                    return false;
                }
            } else {
                boolean foundCandidateEdge = false;
                for (Edge targetEdge : targetGraph.edgesOf(targetVertex)) {
                    Vertex targetNeighbour = Graphs.getOppositeVertex(targetGraph, targetEdge, targetVertex);

                    if (mapping.containsValue(targetNeighbour)) continue;

                    if (targetGraph.getAllEdges(targetVertex, targetNeighbour).size() >= requiredEdges) {
                        foundCandidateEdge = true;
                        break;
                    }
                }
                if (!foundCandidateEdge) {
                    return false;
                }
            }
        }
        return true;
    }

    private void addToPatternFrontier(Vertex patternVertexToMatch, int depth) {
        for (Vertex neighbour : Graphs.neighborListOf(patternGraph, patternVertexToMatch)) {
            if (patternFrontier.get(neighbour) != 0) {
                continue;
            }
            patternFrontier.put(neighbour, depth);
        }
    }

    private void restorePatternFrontier(int depth) {
        for (Map.Entry<Vertex, Integer> entry : patternFrontier.entrySet()) {
            if (entry.getValue() == depth) {
                entry.setValue(0);
            }
        }
    }

    private void addToTargetFrontier(Vertex targetVertex, int depth) {
        for (Vertex neighbour : Graphs.neighborListOf(targetGraph, targetVertex)) {
            if (targetFrontier.get(neighbour) != 0) {
                continue;
            }
            targetFrontier.put(neighbour, depth);
        }
    }

    private void restoreTargetFrontier(int depth) {
        for (Map.Entry<Vertex, Integer> entry : targetFrontier.entrySet()) {
            if (entry.getValue() == depth) {
                entry.setValue(0);
            }
        }
    }
}
