package edu.analyzer.model;

public record AccessRecord(String user, String door, AccessResult result, int durationMs) implements EventRecord{

}
