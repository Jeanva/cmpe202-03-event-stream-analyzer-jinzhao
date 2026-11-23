package edu.analyzer.output;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface OutputWriter {
    void write(Map<String, Object> result, Path file) throws IOException;
}
