package pl.bochunator.matcher;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.*;
import java.util.stream.Collectors;

public class Ullmann<Vertex, Edge> implements Matcher<Vertex, Edge> {

    private final Graph<Vertex, Edge> patternGraph;
    private final Graph<Vertex, Edge> targetGraph;

    private final List<Vertex> patternVertices;
    private final List<Vertex> targetVertices;

    public Ullmann(Graph<Vertex, Edge> patternGraph, Graph<Vertex, Edge> targetGraph) {
        this.patternGraph = patternGraph;
        this.targetGraph = targetGraph;

        this.patternVertices =
                patternGraph.vertexSet()
                        .stream()
                        .sorted(Comparator.comparingInt(
                                patternGraph::degreeOf
                        ).reversed())
                        .collect(Collectors.toList());

        this.targetVertices =
                new ArrayList<>(targetGraph.vertexSet());
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
        boolean[][] M = buildInitialCompatibilityMatrix();

        while(refineMatrix(M)) {}
        if (hasEmptyRow(M)) {
            return new ArrayList<>();
        }
        List<Map<Vertex, Vertex>> matches = new ArrayList<>();
        search(M, 0, matches);
        return matches;
    }

    private boolean[][] buildInitialCompatibilityMatrix() {

        int patternSize = patternVertices.size();
        int targetSize = targetVertices.size();

        boolean[][] M = new boolean[patternSize][targetSize];

        for (int i = 0; i < patternSize; i++) {

            Vertex patternVertex = patternVertices.get(i);

            int patternDegree = patternGraph.degreeOf(patternVertex);
            int patternLoops = patternGraph.getAllEdges(patternVertex, patternVertex).size();

            for (int j = 0; j < targetSize; j++) {

                Vertex targetVertex = targetVertices.get(j);

                int targetDegree = targetGraph.degreeOf(targetVertex);
                int targetLoops = targetGraph.getAllEdges(targetVertex, targetVertex).size();

                M[i][j] = (patternDegree <= targetDegree) && (patternLoops <= targetLoops);
            }
        }

        return M;
    }

    private void search(boolean[][] M, int patternIndex, List<Map<Vertex, Vertex>> matches) {
        statesVisited++;
        if (patternIndex == patternVertices.size()) {
            matches.add(matrixToMapping(M));
            return;
        }


        for (int targetIndex = 0; targetIndex < targetVertices.size(); targetIndex++) {
            if (!M[patternIndex][targetIndex]) {
                prunedCandidates++;
                continue;
            }

            boolean[][] next = new boolean[M.length][];
            for (int i = 0; i < M.length; i++) {
                next[i] = M[i].clone();
            }

            for (int makeZerosInRow = 0; makeZerosInRow < targetVertices.size(); makeZerosInRow++) {
                next[patternIndex][makeZerosInRow] = makeZerosInRow == targetIndex;
            }

            for (int makeZerosInColumn = 0; makeZerosInColumn < patternVertices.size(); makeZerosInColumn++) {
                next[makeZerosInColumn][targetIndex] = makeZerosInColumn == patternIndex;
            }

            boolean changed;
            do {
                changed = refineMatrix(next);

                if (hasEmptyRow(next)) {
                    prunedCandidates++;
                    break;
                }
            } while (changed);
            search(next, patternIndex + 1, matches);
        }
    }

    private boolean refineMatrix(boolean[][] next) {
        boolean changed = false;
        int patternSize = patternVertices.size();
        int targetSize = targetVertices.size();

        for (int patternIndex = 0; patternIndex < patternSize; patternIndex++) {
            for (int targetIndex = 0; targetIndex < targetSize; targetIndex++) {
                if (!next[patternIndex][targetIndex]) {
                    continue;
                }

                Vertex patternVertex = patternVertices.get(patternIndex);
                Vertex targetVertex = targetVertices.get(targetIndex);

                Set<Vertex> uniquePatternNeighbours = new HashSet<>();
                for (Edge edge : patternGraph.edgesOf(patternVertex)) {
                    Vertex opp = Graphs.getOppositeVertex(patternGraph, edge, patternVertex);
                    if (!opp.equals(patternVertex)) {
                        uniquePatternNeighbours.add(opp);
                    }
                }

                for (Vertex patternNeighbour: uniquePatternNeighbours) {
                    int requiredEdges = patternGraph.getAllEdges(patternVertex, patternNeighbour).size();
                    boolean found = false;

                    for (Edge targetEdge: targetGraph.edgesOf(targetVertex)) {
                        Vertex targetNeighbour = Graphs.getOppositeVertex(targetGraph, targetEdge, targetVertex);

                        if (targetNeighbour.equals(targetVertex)) {
                            continue;
                        }

                        int pIndex = patternVertices.indexOf(patternNeighbour);
                        int tIndex = targetVertices.size() > 0 ? targetVertices.indexOf(targetNeighbour) : -1;
                        if (tIndex == -1) continue;

                        if (next[pIndex][tIndex]) {
                            int availableEdges = targetGraph.getAllEdges(targetVertex, targetNeighbour).size();
                            if (availableEdges >= requiredEdges) {
                                found = true;
                                break;
                            }
                        }

                    }

                    if (!found) {
                        prunedCandidates++;
                        next[patternIndex][targetIndex] = false;
                        changed = true;
                        break;
                    }
                }
            }
        }
        return changed;
    }

    private Map<Vertex, Vertex> matrixToMapping(boolean[][] M) {
        Map<Vertex, Vertex> result = new HashMap<>();

        for (int i = 0; i < patternVertices.size(); i++) {
            for (int j = 0; j < targetVertices.size(); j++) {
                if (M[i][j]) {
                    result.put(
                            patternVertices.get(i),
                            targetVertices.get(j)
                    );
                    break;
                }
            }
        }

        return result;
    }

    private boolean hasEmptyRow(boolean[][] M) {
        for (int patternIndex = 0; patternIndex < patternVertices.size(); patternIndex++) {
            boolean hasCandidate = false;
            for (int targetIndex = 0; targetIndex < targetVertices.size(); targetIndex++) {
                if (M[patternIndex][targetIndex]) {
                    hasCandidate = true;
                    break;
                }
            }
            if (!hasCandidate) {
                return true;
            }
        }
        return false;
    }
}
