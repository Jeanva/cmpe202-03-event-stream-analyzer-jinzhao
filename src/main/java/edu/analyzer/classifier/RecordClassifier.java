package edu.analyzer.classifier;

import edu.analyzer.model.EventRecord;
import edu.analyzer.model.ParsedLine;

import java.util.Optional;

public interface RecordClassifier {
    Optional<EventRecord> classify(ParsedLine line);
}
