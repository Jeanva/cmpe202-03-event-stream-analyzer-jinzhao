package edu.analyzer.classifier;

import edu.analyzer.model.EventRecord;
import edu.analyzer.model.ParsedLine;
import edu.analyzer.model.SensorRecord;

import java.util.Optional;

public class SensorClassifier implements RecordClassifier{

    @Override
    public Optional<EventRecord> classify(ParsedLine line) {
        Optional<String> sensor = line.get("sensor");
        Optional<String> reading = line.get("reading");

        if(sensor.isEmpty() || reading.isEmpty() || sensor.get().isEmpty()) {
            return Optional.empty();
        }

        try {
            double value = Double.parseDouble(reading.get());
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                return Optional.empty();
            }
            return Optional.of(new SensorRecord(sensor.get(), value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

    }
}
