package pl.bochunator.repository.match;

import pl.bochunator.experiment.domain.ExperimentPaths;
import pl.bochunator.experiment.domain.MatchResult;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class FileMatchRepository implements MatchRepository {

    @Override
    public void save(MatchResult result) {
        Path experimentDir =
                ExperimentPaths.experimentDirectory(
                        result.getExperimentId()
                );

        Path matchDir =
                experimentDir.resolve("matches");

        Path file =
                matchDir.resolve(
                        result.getAlgorithm() + ".txt"
                );

        try {
            matchDir.toFile().mkdirs();

            try (FileWriter writer =
                         new FileWriter(file.toFile())) {

                writer.write(result.toText());

            }

        } catch (IOException e) {
            throw new RuntimeException("Cannot save matches: " + result.getExperimentId(), e);
        }
    }
}
