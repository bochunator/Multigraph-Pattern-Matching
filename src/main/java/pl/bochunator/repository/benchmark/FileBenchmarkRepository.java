package pl.bochunator.repository.benchmark;

import pl.bochunator.experiment.domain.BenchmarkResult;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBenchmarkRepository implements BenchmarkRepository {

    private final Path file =
            Path.of("data", "benchmark", "results.csv");

    @Override
    public void save(BenchmarkResult result) {
        try {
            Files.createDirectories(file.getParent());

            boolean exists = Files.exists(file);

            try (FileWriter writer =
                         new FileWriter(file.toFile(), true)) {

                if (!exists) {
                    writer.write(
                            "experimentId,algorithm,timeNs,matchCount,resultHash, statesVisited, prunedCandidates"
                    );
                    writer.write(System.lineSeparator());
                }

                writer.write(
                        String.join(",",
                                String.valueOf(result.getExperimentId()),
                                result.getAlgorithm(),
                                String.valueOf(result.getTimeNs()),
                                String.valueOf(result.getMatchCount()),
                                result.getResultHash(),
                                String.valueOf(result.getBenchmark()),
                                String.valueOf(result.getPruned())
                        )
                );

                writer.write(System.lineSeparator());
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Cannot save benchmark result",
                    e
            );
        }
    }
}
