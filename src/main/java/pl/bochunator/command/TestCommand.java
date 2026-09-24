package pl.bochunator.command;

import org.jgrapht.graph.DefaultEdge;
import pl.bochunator.experiment.runner.ExperimentRunner;
import pl.bochunator.experiment.domain.Experiment;
import pl.bochunator.matcher.Matcher;
import pl.bochunator.matcher.MatcherProvider;
import pl.bochunator.repository.benchmark.BenchmarkRepository;
import pl.bochunator.repository.experiment.ExperimentRepository;
import pl.bochunator.repository.match.MatchRepository;

public class TestCommand implements Command {

    private final ExperimentRepository experimentRepository;
    private final MatchRepository matchRepository;
    private final BenchmarkRepository benchmarkRepository;

    public TestCommand(ExperimentRepository experimentRepository, MatchRepository matchRepository, BenchmarkRepository benchmarkRepository) {
        this.experimentRepository = experimentRepository;
        this.matchRepository = matchRepository;
        this.benchmarkRepository = benchmarkRepository;
    }

    @Override
    public void execute(String[] args) {
        String algorithm = args[1];

        Integer id = null;
        if (args.length > 2) {
            id = Integer.parseInt(args[2]);
        }

        int resolvedId = experimentRepository.resolveId(id);

        Experiment experiment = experimentRepository.load(resolvedId);
        System.out.println("Algorithm below:");

        MatcherProvider matcherProvider = new MatcherProvider();
        Matcher<Integer, DefaultEdge> matcher =
                matcherProvider.create(
                        algorithm,
                        experiment.getPattern(),
                        experiment.getGraph()
                );

        ExperimentRunner experimentRunner = new ExperimentRunner(matchRepository, benchmarkRepository);
        experimentRunner.run(experiment, matcher);
    }
}
