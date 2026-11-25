package edu.analyzer.model;

public record DeviceEvent(Severity severity, String device) implements EventRecord{

}
