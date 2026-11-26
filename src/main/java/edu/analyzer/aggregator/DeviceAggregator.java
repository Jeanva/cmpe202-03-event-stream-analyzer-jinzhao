package edu.analyzer.aggregator;

import edu.analyzer.model.DeviceEvent;
import edu.analyzer.model.EventRecord;
import edu.analyzer.model.Severity;

import java.util.*;

public class DeviceAggregator implements Aggregator{
    private final Map<String, EnumMap<Severity, Integer>> perDevice = new LinkedHashMap<>();
    private final EnumMap<Severity, Integer> perSeverity = new EnumMap<>(Severity.class);

    @Override
    public void accept(EventRecord record) {
        if (record instanceof DeviceEvent d) {
            perSeverity.merge(d.severity(), 1, Integer::sum);
            perDevice.computeIfAbsent(d.device(), k-> new EnumMap<>(Severity.class))
                    .merge(d.severity(), 1, Integer::sum);
        }
    }

    @Override
    public Map<String, Object> result() {
        Map<String, Object> out = new LinkedHashMap<>();
        if (perDevice.isEmpty()) return out;  // 无数据时返回{}

        Map<String, Object> bySeverity = new LinkedHashMap<>();
        for (Severity s : Severity.values()) {
            bySeverity.put(s.name(), perSeverity.getOrDefault(s, 0));
        }

        Map<String, Object> byDevice = new LinkedHashMap<>();
        for (Map.Entry<String, EnumMap<Severity, Integer>> e : perDevice.entrySet()) {
            int total = 0;
            Severity best = null;
            int bestCount = 0;
            for (Severity s: Severity.values()) {       // From high to low
                int c = e.getValue().getOrDefault(s, 0);
                total +=c;
                if (c > bestCount) {    // 严格大于，平级保留最高级别
                    best = s;
                    bestCount = c;
                }
            }

            Map<String, Object> info = new LinkedHashMap<>();
            info.put("total", total);
            info.put("most_frequent_severity", best.name());
            byDevice.put(e.getKey(), info);
        }

        out.put("by_severity", bySeverity);
        out.put("by_device", byDevice);
        return out;
    }
}
