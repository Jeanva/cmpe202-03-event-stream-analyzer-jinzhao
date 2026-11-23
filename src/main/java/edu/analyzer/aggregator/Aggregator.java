package edu.analyzer.aggregator;

import edu.analyzer.model.EventRecord;

import java.util.Map;

public interface Aggregator {
    void accept(EventRecord record);

    Map<String, Object> result();
}
