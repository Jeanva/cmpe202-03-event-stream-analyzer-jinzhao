package edu.analyzer.cli;

import java.nio.file.Path;

public class CliArguments {
    private final Path file;
    private final Path outDir;

    private CliArguments(Path file, Path outDir) {
        this.file = file;
        this.outDir = outDir;
    }

    public Path getFile() { return file; };
    public Path getOutDir() {return outDir;};

    // throw IllegalArgumentException when fail parsing
    public static CliArguments parse(String [] args) {
        String file = null;
        String outDir = ".";

        for (int i =0; i< args.length; i++) {
            switch (args[i]){
                case "--file" -> {
                    if(i +1 >= args.length) {
                        throw new IllegalArgumentException("Missing value for --file");
                    }
                    file = args[++i];
                }

                case "--outdir" -> {
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("Missing value for --outdir");
                    }
                    outDir = args[++i];
                }
                default -> throw new IllegalArgumentException("Unknown argument:" + args[i]);
            }
        }

        if (file == null) {
            throw new IllegalArgumentException("Missing required argument:--file <filename.txt>");
        }

        return new CliArguments(Path.of(file), Path.of(outDir));

    }
}
