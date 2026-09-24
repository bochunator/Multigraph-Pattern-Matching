package pl.bochunator.repository.match;

import pl.bochunator.experiment.domain.MatchResult;

public interface MatchRepository {

    void save(MatchResult result);
}
