package edu.analyzer.service;

import edu.analyzer.model.LineParser;
import edu.analyzer.output.OutputWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class AnalyzerService {
    private final LineParser parser;
    private final List<RecordType> recordTypes;
    private final OutputWriter writer;

    public AnalyzerService(LineParser parser, List<RecordType> recordTypes, OutputWriter writer) {
        this.parser = parser;
        this.recordTypes = recordTypes;
        this.writer = writer;
    }

    public void run(Path input, Path outDir) throws IOException{

    }


}
