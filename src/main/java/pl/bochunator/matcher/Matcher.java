package pl.bochunator.matcher;

import java.util.List;
import java.util.Map;

public interface Matcher<Vertex, Edge> {

    List<Map<Vertex, Vertex>> findMatches();

    default String getName() {
        return getClass().getSimpleName();
    }

    default long getBacktracks() {
        return 0;
    }

    default long getPruned() {
        return 0;
    }
}
