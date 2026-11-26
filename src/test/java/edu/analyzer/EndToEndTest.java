package edu.analyzer;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EndToEndTest {

    private Path resource(String name) throws Exception {
        return Path.of(getClass().getResource(name).toURI());
    }

    private JsonElement json(Path p) throws IOException {
        return JsonParser.parseString(Files.readString(p, StandardCharsets.UTF_8));
    }

    @Test
    void sampleInputProduceExpectedFiles(@TempDir Path out) throws Exception {

        int code = EventStreamAnalyzer.run(new String[] {
            "--file", resource("/sample.txt").toString(),"--outdir", out.toString()});

        assertEquals(0, code);
        for (String n : new String[]{"sensors", "devices", "access"}) {
            assertEquals(json(resource("/expected/" + n + ".json")), json(out.resolve(n + ".json")),n);
        }

    }

    @Test
    void emptyInputStillCreateEmptyObjects(@TempDir Path dir) throws Exception {
        Path in = dir.resolve("empty.txt");
        Files.writeString(in, "");
        Path out = dir.resolve("results");
        assertEquals(0, EventStreamAnalyzer.run(new String[]{"--file", in.toString(), "--outdir", out.toString()}));
        for (String n : new String[]{"sensors", "devices", "access"}) {
            assertEquals("{}", Files.readString(out.resolve(n + ".json")).trim());
        }
    }

    @Test
    void missingFileArgumentGivesNonZero() {
        assertNotEquals(0, EventStreamAnalyzer.run(new String[]{}));
    }

    @Test
    void nonexistentFileGivesNonZero() {
        assertNotEquals(0, EventStreamAnalyzer.run(new String[]{"--file", "no_such_file.txt"}));
    }


}
