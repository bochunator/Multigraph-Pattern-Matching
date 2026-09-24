package pl.bochunator.repository.metadata;

import pl.bochunator.experiment.domain.ExperimentMetadata;

public interface ExperimentMetadataRepository {
    void save(ExperimentMetadata experimentMetadata);
}
