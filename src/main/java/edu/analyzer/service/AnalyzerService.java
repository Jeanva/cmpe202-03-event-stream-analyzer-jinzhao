package edu.analyzer.service;

import edu.analyzer.parser.LineParser;
import edu.analyzer.model.ParsedLine;
import edu.analyzer.output.OutputWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

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
        // Use InputStreamReader: when there is an invalid char, replace it instead of throwing exception.
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(input), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Optional<ParsedLine> parsed = parser.parse(line);
                if (parsed.isEmpty()) continue;
                for (RecordType type: recordTypes) {
                    if (type.tryAccept(parsed.get())) break;
                }
            }
        }

        Files.createDirectories(outDir);
        for(RecordType type: recordTypes) {
            writer.write(type.result(), outDir.resolve(type.outputName() + ".json"));
        }
    }


}
