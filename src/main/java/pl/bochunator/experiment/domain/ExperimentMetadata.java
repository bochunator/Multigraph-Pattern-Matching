package pl.bochunator.experiment.domain;

public class ExperimentMetadata {
    private final int experimentId;

    private final int graphVertices;
    private final int graphEdges;

    private final int patternVertices;
    private final int patternEdges;

    public ExperimentMetadata(int experimentId, int graphVertices, int graphEdges, int patternVertices, int patternEdges) {
        this.experimentId = experimentId;
        this.graphVertices = graphVertices;
        this.graphEdges = graphEdges;
        this.patternVertices = patternVertices;
        this.patternEdges = patternEdges;
    }

    public String toCsv() {
        return experimentId + "," +
                graphVertices + "," +
                graphEdges + "," +
                patternVertices + "," +
                patternEdges;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public int getGraphVertices() {
        return graphVertices;
    }

    public int getGraphEdges() {
        return graphEdges;
    }

    public int getPatternVertices() {
        return patternVertices;
    }

    public int getPatternEdges() {
        return patternEdges;
    }
}
