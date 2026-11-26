package edu.analyzer.output;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class JsonOutputWriter implements OutputWriter{
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    @Override
    public void write(Map<String, Object> result, Path file) throws IOException {
        Files.writeString(file, gson.toJson(result) + System.lineSeparator(), StandardCharsets.UTF_8);
    }
}
