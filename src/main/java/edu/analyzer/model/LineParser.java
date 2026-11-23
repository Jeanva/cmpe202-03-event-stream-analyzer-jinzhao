package edu.analyzer.model;

import edu.analyzer.model.ParsedLine;
import java.util.Optional;

/*
Strategy: parse a raw line string into a key-value pair
 */
public interface LineParser {
    // Return Optional.empty() if meet invalid key-value pair
    Optional<ParsedLine> parse(String line);
}
