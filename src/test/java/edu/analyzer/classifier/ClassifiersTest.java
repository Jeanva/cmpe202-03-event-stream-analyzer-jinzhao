package edu.analyzer.classifier;

import edu.KeyValueLineParser;
import edu.analyzer.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassifiersTest {
    private final KeyValueLineParser parser = new KeyValueLineParser();

    private ParsedLine p(String line) { return parser.parse(line).orElseThrow();}

    @Test
    void sensorValidLine() {
        var r = new SensorClassifier().classify(p("ts=1 sensor=temperature_celsius device=t1 reading=21.5"));
        assertEquals(new SensorRecord("temperature_celsius", 21.5), r.orElseThrow());
    }

    @Test
    void sensorNonNumericReadingRejected() {
        assertTrue(new SensorClassifier().classify(p("ts=1 sensor=temperature_celsius reading=abc")).isEmpty());
    }

    @Test
    void sensorMissingReadingRejected() {
        assertTrue(new SensorClassifier().classify(p("ts=1 sensor=humidity_percen")).isEmpty());

    }

    @Test
    void deviceValidLine() {
        var r = new DeviceClassifier().classify(p("ts=1 severity=WARN device=thermostat-01 msg=\"Temp driftingd\""));
                assertEquals(new DeviceEvent(Severity.WARN, "thermostat-01"), r.orElseThrow());
    }

    @Test
    void deviceInvalidServerityRejected() {
        assertTrue(new DeviceClassifier().classify(p("ts=1 severity=FATAL device=d1")).isEmpty());
    }

    @Test
    void deviceMissingDeviceRejected() {
        assertTrue(new DeviceClassifier().classify(p("ts=1 severity=INFO")).isEmpty());
    }

    @Test
    void accessValidLine() {
        var r = new AccessClassifier().classify(p("ts=1 user=user-42 door=door-A result=DENIED duration_ms=30"));
        assertEquals(new AccessRecord("user-42", "door-A", AccessResult.DENIED, 30), r.orElseThrow());
    }

    @Test
    void accessInvalidResultRejected() {
        assertTrue(new AccessClassifier().classify(p("ts=1 user=u door=d result=MAYBE duration_ms=30")).isEmpty());
    }

    @Test
    void accessNonIntegerDurationRejected() {
        assertTrue(new AccessClassifier().classify(p("ts=1 user=u door=d result=GRANTED duration_ms=3.5")).isEmpty());
    }

    @Test
    void accessMissingKeyRejected() {
        assertTrue(new AccessClassifier().classify(p("ts=1 user=u door=d result=GRANTED")).isEmpty());
    }

    @Test
    void sensorLineIsNotDeviceOrAccess() {
        ParsedLine line = p("ts=1 sensor=humidity_percent device=hygro-02 reading=45");
        assertTrue(new DeviceClassifier().classify(line).isEmpty());
        assertTrue(new AccessClassifier().classify(line).isEmpty());
    }
}
