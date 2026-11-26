package edu.analyzer.edu.analyzer.util;

import java.util.List;

import edu.analyzer.util.StatisticsUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatisticsUtilTest {
    @Test
    void medianEventCountAveragesMiddleTwo() {

        assertEquals(23.25, StatisticsUtil.median(List.of(21.5, 25.0)), 1e-9);
    }

    @Test
    void medianOddCountTakesMiddle() {
        assertEquals(2.0, StatisticsUtil.median(List.of(1.0, 2.0,3.0)), 1e-9);
    }

    @Test
    void populationStdDevDividesByN () {
        assertEquals(1.75, StatisticsUtil.populationStdDev(List.of(21.5, 25.0)), 1e-9);
    }

    @Test
    void percentileNearestRankMatchesHandCalculation() {
        List<Integer> sorted = List.of(18, 28, 30, 35);
        assertEquals(28, StatisticsUtil.percentile(sorted, 50));
        assertEquals(35, StatisticsUtil.percentile(sorted, 90));
        assertEquals(35, StatisticsUtil.percentile(sorted, 99));
    }

    @Test
    void percentileOfSingleValue() {
        assertEquals(12, StatisticsUtil.percentile(List.of(12), 50));
    }
}
