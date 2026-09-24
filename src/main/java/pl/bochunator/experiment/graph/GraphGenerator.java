package pl.bochunator.experiment.graph;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

public interface GraphGenerator {

    Graph<Integer, DefaultEdge> generate(
            Graph<Integer, DefaultEdge> pattern,
            int vertices,
            int edges
    );

}
