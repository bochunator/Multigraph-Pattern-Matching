package pl.bochunator.repository.benchmark;

import pl.bochunator.experiment.domain.BenchmarkResult;

public interface BenchmarkRepository {
    void save(BenchmarkResult result);
}
