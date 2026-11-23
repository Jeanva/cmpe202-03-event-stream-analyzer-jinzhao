package edu.analyzer.classifier;

import edu.analyzer.model.EventRecord;
import edu.analyzer.model.ParsedLine;

import java.util.Optional;

public class DeviceClassifier implements RecordClassifier{
    @Override
    public Optional<EventRecord> classify(ParsedLine line) {
        return Optional.empty();
    }
}
