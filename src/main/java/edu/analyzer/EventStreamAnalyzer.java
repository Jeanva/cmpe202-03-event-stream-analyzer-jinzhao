package edu.analyzer;

import edu.analyzer.aggregator.*;
import edu.analyzer.classifier.*;
import edu.analyzer.cli.CliArguments;
import edu.analyzer.output.JsonOutputWriter;
import edu.analyzer.parser.KeyValueLineParser;
import edu.analyzer.service.AnalyzerService;
import edu.analyzer.service.RecordType;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class EventStreamAnalyzer {

    public static void main(String [] args) {
        int exitCode = run(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run (String[] args) {
        CliArguments cli;

        try {
            cli = CliArguments.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error:" + e.getMessage());
            System.err.println("Usage: java -jar<jar> --file <filename.txt> [--outdir <directory]");
            return 1;
        }

        if (!Files.isRegularRile(cli.getFile())) {
            System.err.println("Error: input file not found:" + cli.getFile());
            return 2;
        }

        // ---- Add new recordType ----
        List<RecordType> recordType = List.of(
                new RecordType("sensors", new SensorClassifier(), new SensorAggregator()),
                new RecordType("devices", new DeviceClassifier(), new DeviceAggregator()),
                new RecordType("access", new AccessClassifier(), new AccessAggregator())
        );

        AnalyzerService service = new AnalyzerService(
                new KeyValueLineParser(),
                recordType,
                new JsonOutputWriter()
        );

        try {
            service.run(cli.getFile(), cli.getOutDir());
        } catch(IOException e) {
            System.err.println("Error" + e.getMessage());
            return 3;
        }

        return 0;
    }
}
