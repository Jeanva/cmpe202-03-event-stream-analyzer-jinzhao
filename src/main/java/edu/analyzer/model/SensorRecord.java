package edu.analyzer.model;

public record SensorRecord(String sensor, double reading) implements EventRecord{
}
