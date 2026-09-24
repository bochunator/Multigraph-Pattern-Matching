package pl.bochunator.experiment.runner;

import pl.bochunator.experiment.domain.BenchmarkResult;
import pl.bochunator.experiment.domain.Experiment;
import pl.bochunator.experiment.domain.MatchResult;
import pl.bochunator.matcher.Matcher;
import pl.bochunator.repository.benchmark.BenchmarkRepository;
import pl.bochunator.repository.match.MatchRepository;

import java.util.List;
import java.util.Map;

public class ExperimentRunner {

    private final MatchRepository matchRepository;
    private final BenchmarkRepository benchmarkRepository;

    public ExperimentRunner(MatchRepository matchRepository, BenchmarkRepository benchmarkRepository) {
        this.matchRepository = matchRepository;
        this.benchmarkRepository = benchmarkRepository;
    }

    public void run(Experiment experiment, Matcher<Integer, ?> matcher) {

        long start = System.nanoTime();
        List<Map<Integer, Integer>> matches = matcher.findMatches();
        long end = System.nanoTime();
        long timeNs = end - start;

        System.out.println(
                "Time [ms]: " +
                        timeNs / 1_000_000.0
        );

        System.out.println("Found " + matches.size() + " matches");
        System.out.println("Number of backtracks " + matcher.getBacktracks());

        MatchResult matchResult = new MatchResult(
                experiment.getId(),
                matcher.getName(),
                matches
        );

        BenchmarkResult benchmarkResult = new BenchmarkResult(
                experiment.getId(),
                matcher.getName(),
                timeNs,
                matchResult.getMatches().size(),
                matchResult.calculateHash(),
                matcher.getBacktracks(),
                matcher.getPruned()
        );

        matchRepository.save(matchResult);
        benchmarkRepository.save(benchmarkResult);
        System.out.println("Finished saving experiment " + experiment.getId());
    }
}
