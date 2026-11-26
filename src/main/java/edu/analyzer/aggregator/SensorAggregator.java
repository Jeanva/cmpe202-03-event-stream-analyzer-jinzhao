package edu.analyzer.aggregator;

import edu.analyzer.model.EventRecord;
import edu.analyzer.model.SensorRecord;
import edu.analyzer.util.NumberFormatter;
import edu.analyzer.util.StatisticsUtil;

import java.util.*;

public class SensorAggregator implements Aggregator{
    // final表示常量
    private final Map<String, List<Double>> readings = new LinkedHashMap<>();

    @Override
    public void accept(EventRecord record) {
        if (record instanceof SensorRecord s) {
            readings.computeIfAbsent(s.sensor(), k -> new ArrayList<>()).add(s.reading());
        }
    }

    @Override
    public Map<String, Object> result() {
        Map<String, Object> out = new LinkedHashMap<>();

        for (Map.Entry<String, List<Double>> e : readings.entrySet()) {
            List<Double> sorted = new ArrayList<>(e.getValue());
            Collections.sort(sorted);

            Map<String, Object> stats = new LinkedHashMap<>();
            stats.put("count", sorted.size());
            stats.put("minimum", NumberFormatter.format(sorted.get(0)));
            stats.put("median", NumberFormatter.format(StatisticsUtil.median(sorted)));
            stats.put("average", NumberFormatter.format(StatisticsUtil.average(sorted)));
            stats.put("maximum", NumberFormatter.format(sorted.get(sorted.size() -1)));
            stats.put("std_dev", NumberFormatter.format(StatisticsUtil.populationStdDev(sorted)));
            out.put(e.getKey(), stats);
        }

        return out;
    }


}
