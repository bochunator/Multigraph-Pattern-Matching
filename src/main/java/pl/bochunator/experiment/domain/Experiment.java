package pl.bochunator.experiment.domain;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public class Experiment {

    private final Integer id;
    private final Graph<Integer, DefaultEdge> pattern;
    private final Graph<Integer, DefaultEdge> graph;
    private final ExperimentMetadata experimentMetadata;

    public Experiment(Integer id, Graph<Integer, DefaultEdge> pattern, Graph<Integer, DefaultEdge> graph, ExperimentMetadata experimentMetadata) {
        this.id = id;
        this.pattern = pattern;
        this.graph = graph;
        this.experimentMetadata = experimentMetadata;
    }

    public Integer getId() {
        return id;
    }

    public Graph<Integer, DefaultEdge> getPattern() {
        return pattern;
    }

    public Graph<Integer, DefaultEdge> getGraph() {
        return graph;
    }

    public ExperimentMetadata getExperimentMetadata() {
        return experimentMetadata;
    }
}
