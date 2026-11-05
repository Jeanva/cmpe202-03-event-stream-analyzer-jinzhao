# cmpe202-03-event-stream-analyzer-jinzhao

Command-line application that reads IoT/Smart-Building event records,
classifies each line, and writes aggregated results to JSON files.

## Requirements

- JDK 17 or later
- Maven 3.8+

## Build

```bash
mvn package
```

This runs the tests and produces `target/analyzer.jar`.

## Run

```bash
java -jar target/analyzer.jar --file events.txt --outdir results/
```

(Usage details, output format, and design notes will be added later.)
