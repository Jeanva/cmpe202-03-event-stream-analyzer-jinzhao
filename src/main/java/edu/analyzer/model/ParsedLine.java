package edu.analyzer.model;

import java.util.Map;
import java.util.Optional;

public class ParsedLine {
    private final Map<String, String> fields;

    public ParsedLine(Map<String, String> fields){
        this.fields = Map.copyOf(fields);
    }

    public Optional<String> get(String key){
        return Optional.ofNullable(fields.get(key));
    }

    public boolean contains(String key){
        return fields.containsKey(key);
    }

    public Map<String, String> toMap(){
        return fields;
    }
}
