package pl.bochunator.app;

import pl.bochunator.command.GenerateCommand;
import pl.bochunator.command.TestCommand;
import pl.bochunator.experiment.generator.ExperimentGenerator;
import pl.bochunator.experiment.graph.EmbeddedGraphGenerator;
import pl.bochunator.experiment.graph.GraphGenerator;
import pl.bochunator.experiment.pattern.PatternGenerator;
import pl.bochunator.experiment.pattern.RandomPatternGenerator;
import pl.bochunator.repository.benchmark.BenchmarkRepository;
import pl.bochunator.repository.experiment.ExperimentRepository;
import pl.bochunator.repository.match.MatchRepository;
import pl.bochunator.repository.metadata.ExperimentMetadataRepository;

public class CliApplication {
    private final GenerateCommand generateCommand;
    private final TestCommand testCommand;

    public CliApplication(
            ExperimentRepository experimentRepository,
            ExperimentMetadataRepository experimentMetadataRepository,
            MatchRepository matchRepository,
            BenchmarkRepository benchmarkRepository
    ) {

        PatternGenerator patternGenerator = new RandomPatternGenerator();
        GraphGenerator graphGenerator = new EmbeddedGraphGenerator();
        ExperimentGenerator experimentGenerator = new ExperimentGenerator(patternGenerator, graphGenerator);

        this.generateCommand = new GenerateCommand(experimentRepository, experimentMetadataRepository, experimentGenerator);
        this.testCommand = new TestCommand(experimentRepository, matchRepository, benchmarkRepository);
    }

    public void run(String[] args) {

        if (args.length == 0) {
            System.out.println("Enter mode: generate | test");
            return;
        }

        switch (args[0]) {

            case "generate" -> generateCommand.execute(args);
            case "test" -> testCommand.execute(args);

            default -> System.out.println("Unknown mode");
        }
    }
}
