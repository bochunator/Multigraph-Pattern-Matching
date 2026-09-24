package pl.bochunator.experiment.graph;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;

import java.util.Random;

public class EmbeddedGraphGenerator implements GraphGenerator {

    @Override
    public Graph<Integer, DefaultEdge> generate(Graph<Integer, DefaultEdge> pattern, int vertices, int edges) {
        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);
        Graphs.addGraph(graph, pattern);

        Random random = new Random();

        int start = pattern.vertexSet().size();

        for (int i = start; i < vertices; i++) {
            graph.addVertex(i);
        }

        while (graph.edgeSet().size() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);

            graph.addEdge(a, b);
        }

        return graph;
    }
}
