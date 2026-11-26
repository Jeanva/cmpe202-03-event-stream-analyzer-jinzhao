package edu.analyzer.util;

import edu.analyzer.util.NumberFormatter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberFormatterTest {
    @Test
    void roundsHalfUp() {
        assertEquals(23.3, NumberFormatter.format(23.25));
    }

    @Test void wholeNumberHasNoDecimalPoint() {
        assertEquals(25L, NumberFormatter.format(25.0));
    }

    @Test void zeroIsPlainZero() {
        assertEquals(0L, NumberFormatter.format(0.0));
    }

    @Test void keepsOneDecimal() {
        assertEquals(21.5, NumberFormatter.format(21.5));
    }

    @Test void valueRoundingToWholeBecomesInteger() {
        assertEquals(5L, NumberFormatter.format(4.96));
    }
}
