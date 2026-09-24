package pl.bochunator.experiment.domain;

public class BenchmarkResult {
    private int experimentId;
    private String algorithm;
    private long timeNs;
    private int matchCount;
    private String resultHash;
    private long benchmark;
    private long pruned;

    public BenchmarkResult(int experimentId, String algorithm, long timeNs, int matchCount, String resultHash, long benchmark, long pruned) {
        this.experimentId = experimentId;
        this.algorithm = algorithm;
        this.timeNs = timeNs;
        this.matchCount = matchCount;
        this.resultHash = resultHash;
        this.benchmark = benchmark;
        this.pruned = pruned;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public long getTimeNs() {
        return timeNs;
    }

    public int getMatchCount() {
        return matchCount;
    }

    public String getResultHash() {
        return resultHash;
    }

    public long getBenchmark() {
        return benchmark;
    }

    public long getPruned() {
        return pruned;
    }
}
