package pl.bochunator.app;


import pl.bochunator.repository.benchmark.BenchmarkRepository;
import pl.bochunator.repository.benchmark.FileBenchmarkRepository;
import pl.bochunator.repository.experiment.ExperimentRepository;
import pl.bochunator.repository.experiment.FileExperimentRepository;
import pl.bochunator.repository.match.FileMatchRepository;
import pl.bochunator.repository.match.MatchRepository;
import pl.bochunator.repository.metadata.ExperimentMetadataRepository;
import pl.bochunator.repository.metadata.FileExperimentMetadataRepository;


public class Main {
    /**
     * Implementacja wykorzystuje model multigrafu jako najbardziej ogólną strukturę. Graf prosty traktowany jest jako szczególny przypadek multigrafu, w którym zabronione są pętle własne oraz wielokrotne krawędzie pomiędzy tą samą parą wierzchołków.
     */
    static void main(String[] args) {

        ExperimentRepository experimentRepository =
                new FileExperimentRepository();

        ExperimentMetadataRepository experimentMetadataRepository =
                new FileExperimentMetadataRepository();

        MatchRepository matchRepository =
                new FileMatchRepository();

        BenchmarkRepository benchmarkRepository =
                new FileBenchmarkRepository();

        CliApplication app =
                new CliApplication(
                        experimentRepository,
                        experimentMetadataRepository,
                        matchRepository,
                        benchmarkRepository
                );

        app.run(args);

    }
}
