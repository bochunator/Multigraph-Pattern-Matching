package pl.bochunator.repository.experiment;

import pl.bochunator.experiment.domain.Experiment;

public interface ExperimentRepository {

    void save(Experiment experiment);

    Experiment load(int id);

    int resolveId(Integer requestedId);

    int nextId();

    int loadLastId();
}
