package pl.bochunator.experiment.pattern;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public interface PatternGenerator {

    Graph<Integer, DefaultEdge> generate(int vertices, int edges);

}
