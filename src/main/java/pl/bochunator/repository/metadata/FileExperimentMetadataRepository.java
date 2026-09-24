package pl.bochunator.repository.metadata;

import pl.bochunator.experiment.domain.ExperimentMetadata;
import pl.bochunator.experiment.domain.ExperimentPaths;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static pl.bochunator.repository.ExperimentStorageConfig.METADATA_FILE;

public class FileExperimentMetadataRepository implements ExperimentMetadataRepository {

    @Override
    public void save(ExperimentMetadata experimentMetadata) {

        Path experimentDir = ExperimentPaths.experimentDirectory(experimentMetadata.getExperimentId());
        Path metadataFile = experimentDir.resolve(METADATA_FILE);

        try(FileWriter writer = new FileWriter(metadataFile.toFile(), true)) {

            writer.write(
                    experimentMetadata.toCsv()
            );

            writer.write(System.lineSeparator());

        } catch (IOException e) {
            throw new RuntimeException("Cannot save experimentMetadata: " + experimentMetadata.getExperimentId(), e);
        }
    }
}
