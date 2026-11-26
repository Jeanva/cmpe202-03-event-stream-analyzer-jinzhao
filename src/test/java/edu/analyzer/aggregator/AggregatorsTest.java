package edu.analyzer.aggregator;

import edu.analyzer.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Timeout(5)
public class AggregatorsTest {

    @Test
    void sensorStatisMatchHandCalculation() {
        SensorAggregator agg = new SensorAggregator();
        agg.accept(new SensorRecord("temperature_celsius", 21.5));
        agg.accept(new SensorRecord("temperature_celsius", 25.0));
        agg.accept(new SensorRecord("humidity_percent", 45));

        @SuppressWarnings("unchecked")
        Map<String, Object> t= (Map<String, Object>) agg.result().get("temperature_celsius");
        assertEquals(2, t.get("count"));
        assertEquals(21.5, t.get("minimum"));
        assertEquals(23.3, t.get("median"));
        assertEquals(23.3, t.get("average"));
        assertEquals(25L, t.get("maximum"));
        assertEquals(1.8, t.get("std_dev"));
    }

        @Test
        void sensorSingleValueHasZeroStdDev() {
            SensorAggregator agg = new SensorAggregator();
            agg.accept(new SensorRecord("humidity_percent", 45));
            @SuppressWarnings("unchecked")

            Map<String, Object> h = (Map<String, Object>) agg.result().get("humidity_percent");
            assertEquals(1, h.get("count"));
            assertEquals(45L, h.get("median"));
            assertEquals(0L, h.get("std_dev"));
        }

        @Test
        void emptyAggregatorsReturnEmptyMaps(){
            assertTrue(new SensorAggregator().result().isEmpty());
            assertTrue(new DeviceAggregator().result().isEmpty());
            assertTrue(new AccessAggregator().result().isEmpty());
        }

        @Test
        void devicceTieGoesToHigherSeverity() {
        DeviceAggregator agg = new DeviceAggregator();
        agg.accept(new DeviceEvent(Severity.INFO, "thermostat-01"));
        agg.accept(new DeviceEvent(Severity.WARN, "thermostat-01"));
        agg.accept(new DeviceEvent(Severity.ERROR, "door-lock-07"));
        agg.accept(new DeviceEvent(Severity.CRITICAL, "door-lock-07"));

        Map<String, Object> result = agg.result();
        @SuppressWarnings("unchecked")
                Map<String, Object> byDevice = (Map<String, Object>) result.get("by_device");
        @SuppressWarnings("unchecked")
                Map<String, Object> t = (Map<String, Object>) byDevice.get("thermostat-01");

        @SuppressWarnings("unchecked")
                Map<String, Object> d = (Map<String, Object>) byDevice.get("door-lock-07");

        assertEquals("WARN", t.get("most_frequent_severity"));
        assertEquals("CRITICAL", d.get("most_frequent_severity"));
        assertEquals(2, t.get("total"));
        }

        @Test
        void deviceMajorityBeatsSeverity() {
            DeviceAggregator agg = new DeviceAggregator();
            agg.accept(new DeviceEvent(Severity.INFO, "d1"));
            agg.accept(new DeviceEvent(Severity.INFO, "d1"));
            agg.accept(new DeviceEvent(Severity.CRITICAL, "d1"));
            @SuppressWarnings("unchecked")
                    Map<String, Object> byDevice = (Map<String, Object>) agg.result().get("by_device");
            @SuppressWarnings("unchecked")
                    Map<String, Object> d1 = (Map<String, Object>) byDevice.get("d1");
            assertEquals("INFO", d1.get("most_frequent_severity"));
        }

        @Test
        void deviceCountsBySeverity() {
        DeviceAggregator agg = new DeviceAggregator();
        agg.accept(new DeviceEvent(Severity.ERROR, "d1"));
        agg.accept(new DeviceEvent(Severity.ERROR, "d2"));

        @SuppressWarnings("unchecked")
                Map<String, Object> bySev = (Map<String, Object>) agg.result().get("by_severity");
        assertEquals(2, bySev.get("ERROR"));
        assertEquals(0, bySev.get("INFO"));
        }

        @Test
        void accessStatsMatchHandCalculate() {
            AccessAggregator agg = new AccessAggregator();
            int[] times = {30, 28, 35};
            for (int t : times) agg.accept(new AccessRecord("user-42", "door-A", AccessResult.DENIED, t));
            agg.accept(new AccessRecord("user-08", "door-A", AccessResult.GRANTED, 18));

            @SuppressWarnings("unchecked")
                    Map<String, Object> door= (Map<String, Object>) agg.result().get("door-A");
            @SuppressWarnings("unchecked")
                    Map<String, Object> dt= (Map<String, Object>) door.get("decision_times");
            @SuppressWarnings("unchecked")
                    Map<String, Object> res = (Map<String, Object>) door.get("results");
            assertEquals(18, dt.get("min"));
            assertEquals(28, dt.get("50_percentile"));
            assertEquals(35, dt.get("90_percentile"));
            assertEquals(35, dt.get("99_percentile"));
            assertEquals(35, dt.get("max"));
            assertEquals(1, res.get("GRANTED"));
            assertEquals(3, res.get("DENIED"));
            assertEquals(List.of("user-42"), door.get("flagged_users"));
        }

        @Test
        void twoDeniedAttemptsAreNotFlagged() {
            AccessAggregator agg = new AccessAggregator();
            agg.accept(new AccessRecord("u1", "door-B", AccessResult.DENIED, 10));
            agg.accept(new AccessRecord("u1","door-B", AccessResult.DENIED, 11));
            @SuppressWarnings("unchecked")
                    Map<String, Object> door = (Map<String, Object>) agg.result().get("door-B");
            assertEquals(List.of(), door.get("flagged_users"));
        }

        @Test
        void deniedAttemptsAreCountedPerDoor() {
            AccessAggregator agg = new AccessAggregator();
            agg.accept(new AccessRecord("u1", "door-A", AccessResult.DENIED, 10));
            agg.accept(new AccessRecord("u1", "door-A", AccessResult.DENIED, 11));
            agg.accept(new AccessRecord("u1", "door-B", AccessResult.DENIED, 12));
            @SuppressWarnings("unchecked")
                    Map<String, Object> a= (Map<String, Object>) agg.result().get("door-A");
            assertEquals(List.of(), a.get("flagged_users"));
        }


}
