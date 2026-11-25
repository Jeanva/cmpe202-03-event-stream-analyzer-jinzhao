package edu.analyzer.classifier;

import edu.analyzer.model.DeviceEvent;
import edu.analyzer.model.EventRecord;
import edu.analyzer.model.ParsedLine;
import edu.analyzer.model.Severity;

import java.util.Optional;

public class DeviceClassifier implements RecordClassifier{
    @Override
    public Optional<EventRecord> classify(ParsedLine line) {

        Optional<Severity> severity = line.get("severity").flatMap(Severity:: parse);

        Optional<String> device = line.get("device");

        if (severity.isEmpty() ||
            device.isEmpty() ||
                device.get().isEmpty()
        ) {
            return Optional.empty();
        }

        return Optional.of(new DeviceEvent(severity.get(), device.get()));
    }
}
