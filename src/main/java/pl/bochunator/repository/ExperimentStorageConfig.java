package pl.bochunator.repository;

public class ExperimentStorageConfig {

    public static final String LAST_ID_FILE =
            "data/experiments/last_id.txt";

    public static final String EXPERIMENT_DIR_FORMAT =
            "data/experiments/exp_%06d";

    public static final String PATTERN_FILE =
            "pattern.dot";

    public static final String GRAPH_FILE =
            "graph.dot";

    public static final String METADATA_FILE =
            "metadata.csv";

    private ExperimentStorageConfig() {}
}