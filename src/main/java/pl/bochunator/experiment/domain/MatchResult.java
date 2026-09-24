package pl.bochunator.experiment.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatchResult {
    private final int experimentId;
    private final String algorithm;
    private final List<Map<Integer,Integer>> matches;

    public MatchResult(int experimentId, String algorithm, List<Map<Integer, Integer>> matches) {
        this.experimentId = experimentId;
        this.algorithm = algorithm;
        this.matches = matches;
    }

    public String calculateHash() {
        String canonical = matches.stream()
                .map(this::canonicalMatch)
                .sorted()
                .collect(Collectors.joining("|"));

        return sha256(canonical);
    }

    private String canonicalMatch(Map<Integer,Integer> match) {

        return match.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e ->
                        e.getKey() + "->" + e.getValue()
                )
                .collect(Collectors.joining(","));
    }

    private String sha256(String input) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            input.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder hex =
                    new StringBuilder();

            for (byte b : hash) {
                hex.append(
                        String.format("%02x", b)
                );
            }

            return hex.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String toText() {

        StringBuilder sb = new StringBuilder();

        sb.append("Experiment: ")
                .append(experimentId)
                .append(System.lineSeparator());

        sb.append("Algorithm: ")
                .append(algorithm)
                .append(System.lineSeparator());

        sb.append("Matches: ")
                .append(matches.size())
                .append(System.lineSeparator());

        sb.append(System.lineSeparator());

        int counter = 1;

        for (Map<Integer,Integer> match : matches) {

            sb.append("MATCH ")
                    .append(counter++)
                    .append(System.lineSeparator());

            for (var entry : match.entrySet()) {

                sb.append(entry.getKey())
                        .append(" -> ")
                        .append(entry.getValue())
                        .append(System.lineSeparator());
            }

            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }

    public int getExperimentId() {
        return experimentId;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public List<Map<Integer, Integer>> getMatches() {
        return matches;
    }
}
