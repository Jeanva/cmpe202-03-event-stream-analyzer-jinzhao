package edu.analyzer.classifier;

import edu.analyzer.model.AccessRecord;
import edu.analyzer.model.AccessResult;
import edu.analyzer.model.EventRecord;
import edu.analyzer.model.ParsedLine;

import java.util.Optional;

public class AccessClassifier implements RecordClassifier{
    @Override
    public Optional<EventRecord> classify(ParsedLine line) {

        Optional<String> user = line.get("user");
        Optional<String> door = line.get("door");
        Optional<String> result = line.get("result");
        Optional<String> duration = line.get("duration_ms");

        if(user.isEmpty() || door.isEmpty() || result.isEmpty() || duration.isEmpty()) {
            return Optional.empty();
        }

        AccessResult r;
        if (result.get().equals("GRANTED")) r = AccessResult.GRANTED;
        else if (result.get().equals("DENIED")) r = AccessResult.DENIED;
        else return Optional.empty();

        try {
            int ms = Integer.parseInt(duration.get());
            return Optional.of(new AccessRecord(user.get(), door.get(), r, ms));
        } catch(NumberFormatException e) {
            return Optional.empty();
        }
    }
}
