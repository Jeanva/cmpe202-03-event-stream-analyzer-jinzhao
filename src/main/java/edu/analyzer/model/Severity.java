package edu.analyzer.model;

public enum Severity {
    CRITICAL, ERROR, WARN, INFO;

    public static java.util.Optional<Severity> parse(String s) {
        for (Severity v: values()) {
            if (v.name().equals(s)) { return java.util.Optional.of(v);}
        }
        return java.util.Optional.empty();
    }
}
