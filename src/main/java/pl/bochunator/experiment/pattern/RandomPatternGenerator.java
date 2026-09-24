package pl.bochunator.experiment.pattern;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;

import java.util.Random;

public class RandomPatternGenerator implements PatternGenerator {

    @Override
    public Graph<Integer, DefaultEdge> generate(int vertices, int edges) {
        Graph<Integer, DefaultEdge> pattern = new Pseudograph<>(DefaultEdge.class);

        Random random = new Random();

        for (int i = 0; i < vertices; i++) {
            pattern.addVertex(i);
        }

        while (pattern.edgeSet().size() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);

            pattern.addEdge(a, b);
        }

        return pattern;
    }
}
