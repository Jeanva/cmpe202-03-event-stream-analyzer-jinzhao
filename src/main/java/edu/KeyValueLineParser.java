package edu;

import edu.analyzer.model.LineParser;
import edu.analyzer.model.ParsedLine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//parse key=value format, enclose the value when there is a space.
public class KeyValueLineParser implements LineParser {

    @Override
    public Optional<ParsedLine> parse(String line){
        if (line == null) {
            return Optional.empty();
        }

        Map<String, String> fields = new HashMap<>();
        int n = line.length();
        int i = 0;

        while (i < n) {
            while (i< n && Character.isWhitespace(line.charAt(i))) i++;
            if (i >= n) break;

            //read key until‘=’ or space
            int keyStart = i;
            while (i < n && line.charAt(i) != '=' && !Character.isWhitespace(line.charAt(i))) i++;


                // if there is no '=' : ignore and go to next line
                if (i >= n || line.charAt(i) != '=') continue;

            String key = line.substring(keyStart, i);
            i++;

            String value;
            if (i <n && line.charAt(i) == '"') {
                int close = line.indexOf('"', i + 1);
                if (close < 0) break;
                value = line.substring(i + 1, close);
                i = close + 1;
            } else {
                int valueStart = i;
                while (i <n && !Character.isWhitespace(line.charAt(i))) i++;
                value = line.substring(valueStart, i);
            }

            if (!key.isEmpty()) {
                fields.put(key, value);
            }
        }
        return fields.isEmpty() ? Optional.empty() : Optional.of(new ParsedLine(fields));
    }
}
