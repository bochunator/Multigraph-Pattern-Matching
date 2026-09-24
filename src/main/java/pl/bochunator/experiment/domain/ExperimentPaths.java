package pl.bochunator.experiment.domain;

import java.nio.file.Path;

import static pl.bochunator.repository.ExperimentStorageConfig.EXPERIMENT_DIR_FORMAT;

public final class ExperimentPaths {
    private ExperimentPaths() {}

    public static Path experimentDirectory(int id) {
        return Path.of(
                String.format(EXPERIMENT_DIR_FORMAT, id)
        );
    }
}
