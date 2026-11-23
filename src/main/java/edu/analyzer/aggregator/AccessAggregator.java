package edu.analyzer.aggregator;

import edu.analyzer.model.EventRecord;

import java.util.Map;

public class AccessAggregator implements Aggregator{
    @Override
    public void accept(EventRecord record) {

    }

    @Override
    public Map<String, Object> result() {
        return Map.of();
    }
}
