package pl.bochunator.experiment.generator;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import pl.bochunator.experiment.domain.Experiment;
import pl.bochunator.experiment.domain.ExperimentMetadata;
import pl.bochunator.experiment.graph.GraphGenerator;
import pl.bochunator.experiment.pattern.PatternGenerator;

public class ExperimentGenerator {

    private final PatternGenerator patternGenerator;
    private final GraphGenerator graphGenerator;

    public ExperimentGenerator(PatternGenerator patternGenerator, GraphGenerator graphGenerator) {
        this.patternGenerator = patternGenerator;
        this.graphGenerator = graphGenerator;
    }

    public Experiment generate(ExperimentMetadata experimentMetadata) {

        Graph<Integer, DefaultEdge> pattern =
                patternGenerator.generate(
                        experimentMetadata.getPatternVertices(),
                        experimentMetadata.getPatternEdges()
                );

        Graph<Integer, DefaultEdge> graph =
                graphGenerator.generate(
                        pattern,
                        experimentMetadata.getGraphVertices(),
                        experimentMetadata.getGraphEdges()
                );

        return new Experiment(experimentMetadata.getExperimentId(), pattern, graph, experimentMetadata);
    }



}
