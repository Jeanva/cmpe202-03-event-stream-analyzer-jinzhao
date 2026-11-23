package edu.analyzer.service;

import edu.analyzer.aggregator.Aggregator;
import edu.analyzer.classifier.RecordClassifier;
import edu.analyzer.model.EventRecord;
import edu.analyzer.model.ParsedLine;

import java.util.Map;
import java.util.Optional;

public class RecordType {
    private final String name;
    private final RecordClassifier classifier;
    private final Aggregator aggregator;

    public RecordType (String name, RecordClassifier classifier, Aggregator aggregator) {
        this.name = name;
        this.classifier = classifier;
        this.aggregator = aggregator;
    }

    public boolean tryAccept(ParsedLine line){
        Optional<EventRecord> record = classifier.classify(line);
        if (record.isEmpty()) {
            return false;
        }
        aggregator.accept(record.get());
        return true;

    }

    public Map<String, Object> result() {
        return aggregator.result();
    }

    public String outputName() {
        return name;
    }
}
