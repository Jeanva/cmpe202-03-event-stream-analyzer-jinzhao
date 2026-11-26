package edu.analyzer.parser;

import edu.analyzer.model.ParsedLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
@Timeout(5)

public class KeyValueLineParserTest {
    private KeyValueLineParser parser;

    @BeforeEach
    void setUp() {
        parser = new KeyValueLineParser();
    }

    @Test
    void ParsesSimpleKeyValuePairs() {
        ParsedLine p = parser.parse("ts=2024-05-10T08:00:00Z sensor=temperature_celsius device=thermostat-01 reading=21.5").orElseThrow();

        assertEquals("temperature_celsius", p.get("sensor").orElseThrow());
        assertEquals("thermostat-01", p.get("device").orElseThrow());
        assertEquals("21.5", p.get("reading").orElseThrow());
        assertEquals("2024-05-10T08:00:00Z", p.get("ts").orElseThrow());
    }

    @Test
    void quotedValueKeepsSpaces() {
        ParsedLine p = parser.parse("ts=2024-05-10T08:00:05Z severity=INFO device=thermostat-01 msg=\"Heartbeat OK\"").orElseThrow();
        assertEquals("Heartbeat OK", p.get("msg").orElseThrow());
    }

    @Test
    void fieldsAfterQuotedValueAreStillParsed() {
        ParsedLine p = parser.parse("ts=1 severity=ERROR device=door-lock-07 msg=\"Latch jammed\" code=E41").orElseThrow();

        assertEquals("Latch jammed", p.get("msg").orElseThrow());
        assertEquals("E41", p.get("code").orElseThrow());
        assertEquals("door-lock-07", p.get("device").orElseThrow());
    }

    @Test
    void tokensWithoutEqualsAreIgnored() {
        ParsedLine p = parser.parse("ts=2024-05-10T08:00:50Z ###CORRUPTED LINE### !!!").orElseThrow();
        assertEquals(1, p.toMap().size());
        assertTrue(p.contains("ts"));
    }

    @Test
    void lineWithNoKeyValuePairsReturnEmpty(){
        assertTrue(parser.parse("###CORRUPTED LINE### !!!").isEmpty());
    }

    @Test
    void emptyBlankAndNullLinesReturnEmpty() {
        assertTrue(parser.parse("").isEmpty());
        assertTrue(parser.parse("    \t").isEmpty());
        assertTrue(parser.parse(null).isEmpty());
    }

    @Test
    void nonNumericValueAreKeptAsStrings() {
        ParsedLine p = parser.parse("ts=1 sensor=temperature_celsius reading=abc").orElseThrow();
        assertEquals("abc", p.get("reading").orElseThrow());
    }

    @Test
    void multipleSpacesAndTabsBetweenFieldsAreHandled() {
        ParsedLine p = parser.parse("ts=1   user=user-42\tdoor=door-A    result=DENIED").orElseThrow();
        assertEquals("user-42", p.get("user").orElseThrow());
        assertEquals("door-A", p.get("door").orElseThrow());
        assertEquals("DENIED", p.get("result").orElseThrow());
    }

    @Test
    void unterminatedQuoteDropsThatFieldButKeepsEarlierOnes() {
        ParsedLine p = parser.parse("ts=1 device=d1 msg=\"never closed severity=INFO").orElseThrow();
        assertEquals("d1", p.get("device").orElseThrow());
        assertFalse(p.contains("msg"));
    }

    @Test
    void emptyValueIsKeptAsEmptyString() {
        ParsedLine p = parser.parse("ts=1 sensor=humidity reading=").orElseThrow();
        assertEquals("", p.get("reading").orElseThrow());
    }

    @Test
    void duplicateKeyLastValueWins() {
        ParsedLine p = parser.parse("ts=1 reading=1 reading=2").orElseThrow();
        assertEquals("2", p.get("reading").orElseThrow());
    }

    @Test
    void missingKeyReturnsEmptyOptional() {
        ParsedLine p = parser.parse("ts=1 sensor=x").orElseThrow();
        assertTrue(p.get("reading").isEmpty());
        assertFalse(p.contains("reading"));
    }
}
