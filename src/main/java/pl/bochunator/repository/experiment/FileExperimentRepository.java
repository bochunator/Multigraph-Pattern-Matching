package pl.bochunator.repository.experiment;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.dot.DOTImporter;
import pl.bochunator.experiment.domain.Experiment;
import pl.bochunator.experiment.domain.ExperimentPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static pl.bochunator.repository.ExperimentStorageConfig.*;

public class FileExperimentRepository implements ExperimentRepository {

    @Override
    public void save(Experiment experiment) {
        Path experimentDir = ExperimentPaths.experimentDirectory(experiment.getId());

        try {
            Files.createDirectories(experimentDir);

            saveGraph(experimentDir.resolve(PATTERN_FILE) , experiment.getPattern());
            saveGraph(experimentDir.resolve(GRAPH_FILE) , experiment.getGraph());

            saveId(experiment.getId());
        } catch (IOException e) {
            throw new RuntimeException("Cannot save experiment" + experiment.getId(), e);
        }
    }

    private void saveGraph(Path filePath, Graph<Integer, DefaultEdge> graph) {
        DOTExporter<Integer, DefaultEdge> exporter = new DOTExporter<>(Object::toString);
        exporter.setVertexAttributeProvider(v -> Map.of("label", DefaultAttribute.createAttribute(v)));

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            exporter.exportGraph(graph, writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot save graph: " + filePath, e);
        }
    }

    private void saveId(int experimentId) {
        try {
            Files.writeString(Path.of(LAST_ID_FILE), String.valueOf(experimentId));
        } catch (IOException e) {
            throw new RuntimeException("Cannot save last_id.txt file", e);
        }
    }

    @Override
    public Experiment load(int id) {
        Path experimentDir = ExperimentPaths.experimentDirectory(id);

        Graph<Integer, DefaultEdge> pattern =
                loadGraph(experimentDir.resolve(PATTERN_FILE));

        Graph<Integer, DefaultEdge> graph =
                loadGraph(experimentDir.resolve(GRAPH_FILE));

        return new Experiment(id, pattern, graph, null);
    }

    private Graph<Integer, DefaultEdge> loadGraph(Path path) {
        DOTImporter<Integer, DefaultEdge> importer = new DOTImporter<>();
        importer.setVertexFactory(Integer::parseInt);

        Graph<Integer, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);

        try (FileReader reader = new FileReader(path.toFile())) {
            importer.importGraph(graph, reader);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load graph: " + path, e);
        }
        return graph;
    }

    @Override
    public int resolveId(Integer requestedId) {
        if (requestedId != null) {
            return requestedId;
        }
        return loadLastId();
    }

    @Override
    public int nextId() {
        return loadLastId() + 1;
    }

    @Override
    public int loadLastId() {
        Path path = Path.of(LAST_ID_FILE);

        try {
            if (!Files.exists(path)) {
                return 0;
            }

            String content = Files.readString(path).trim();

            if (content.isEmpty()) {
                return 0;
            }

            return Integer.parseInt(content);

        } catch (IOException e) {
            throw new RuntimeException("Cannot read last experiment id", e);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Corrupted last_id.txt", e);
        }
    }
}
