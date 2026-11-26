package edu.analyzer.aggregator;

import edu.analyzer.model.AccessRecord;
import edu.analyzer.model.AccessResult;
import edu.analyzer.model.EventRecord;
import edu.analyzer.util.StatisticsUtil;

import java.util.*;

public class AccessAggregator implements Aggregator{
    private static final int FLAG_THRESHOLD = 3;

    private static class DoorStats {
        final List<Integer> durations = new ArrayList<>();
        int granted = 0;
        int denied = 0;
        final Map<String, Integer> deniedByUser = new TreeMap<>();
    }

    private final Map<String, DoorStats> doors = new LinkedHashMap<>();

    @Override
    public Map<String, Object> result() {
        Map<String, Object> out = new LinkedHashMap<>();
        for (Map.Entry<String, DoorStats> e: doors.entrySet()) {
            DoorStats d = e.getValue();
            List<Integer> sorted = new ArrayList<>(d.durations);
            Collections.sort(sorted);

            Map<String, Object> times =new LinkedHashMap<>();
            times.put("min", sorted.get(0));
            times.put("50_percentile", StatisticsUtil.percentile(sorted, 50));
            times.put("90_percentile", StatisticsUtil.percentile(sorted, 90));
            times.put("99_percentile", StatisticsUtil.percentile(sorted, 99));
            times.put("max", sorted.get(sorted.size() - 1));

            Map<String, Object> results = new LinkedHashMap<>();
            results.put("GRANTED", d.granted);
            results.put("DENIED", d.denied);

            List<String> flagged = new ArrayList<>();
            for (Map.Entry<String, Integer> u : d.deniedByUser.entrySet()) {
                if (u.getValue() >= FLAG_THRESHOLD) flagged.add(u.getKey());
            }

            Map<String, Object> door = new LinkedHashMap<>();
            door.put("decision_times", times);
            door.put("results", results);
            door.put("flagged_users", flagged);
            out.put(e.getKey(), door);
        }
        return out;
    }

    @Override
    public void accept(EventRecord record) {
        if (record instanceof AccessRecord a) {
            DoorStats d = doors.computeIfAbsent(a.door(), k-> new DoorStats());
            d.durations.add(a.durationMs());
            if(a.result() == AccessResult.GRANTED) {
                d.granted++;
            } else {
                d.denied++;
                d.deniedByUser.merge(a.user(), 1, Integer::sum);
            }
        }
    }

}
