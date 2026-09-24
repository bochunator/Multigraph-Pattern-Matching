package pl.bochunator.command;

import pl.bochunator.experiment.domain.Experiment;
import pl.bochunator.experiment.domain.ExperimentMetadata;
import pl.bochunator.experiment.generator.ExperimentGenerator;
import pl.bochunator.repository.experiment.ExperimentRepository;
import pl.bochunator.repository.metadata.ExperimentMetadataRepository;

public class GenerateCommand implements Command {

    private final ExperimentRepository experimentRepository;
    private final ExperimentMetadataRepository experimentMetadataRepository;
    private final ExperimentGenerator experimentGenerator;

    public GenerateCommand(ExperimentRepository experimentRepository, ExperimentMetadataRepository experimentMetadataRepository, ExperimentGenerator experimentGenerator) {
        this.experimentRepository = experimentRepository;
        this.experimentMetadataRepository = experimentMetadataRepository;
        this.experimentGenerator = experimentGenerator;
    }

    @Override
    public void execute(String[] args) {
        int id = experimentRepository.nextId();

        System.out.println("Experiment ID = " + id);

        int numberOfVertices = Integer.parseInt(args[1]);
        int numberOfEdges = Integer.parseInt(args[2]);
        int numberOfPatternVertices = Integer.parseInt(args[3]);
        int numberOfPatternEdges = Integer.parseInt(args[4]);

        ExperimentMetadata experimentMetadata = new ExperimentMetadata(
                id,
                numberOfVertices,
                numberOfEdges,
                numberOfPatternVertices,
                numberOfPatternEdges
        );

        Experiment experiment = experimentGenerator.generate(experimentMetadata);
        experimentRepository.save(experiment);
        experimentMetadataRepository.save(experimentMetadata);
    }
}
