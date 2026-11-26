package edu.analyzer.util;

import java.util.List;

public class StatisticsUtil {
    private StatisticsUtil() {}

    /* values must be ordered. Take two medians in even numbers to split.*/
    public static double median(List<Double> sorted) {
        int n = sorted.size();
        if (n % 2 == 1) return sorted.get(n /2);
        return (sorted.get(n / 2 -1) + sorted.get(n /2)) / 2.0;
    }

    public static double average (List<Double> values) {
        double sum = 0;
        for (double v : values) sum += v;
        return sum / values.size();
    }

    /*总体标准差： 除以N.*/
    public static double populationStdDev(List<Double> values) {
        double mean = average(values);
        double sumSq = 0;
        for (double v:values) sumSq += (v - mean) * (v - mean);
        return Math.sqrt(sumSq / values.size());
    }

    /* nearest-rank: sorted 已排序，索引 = ceil(p/100 * N) -1 */
    public static <T> T percentile(List<T> sorted, int p) {
        int index = (int) Math.ceil(p / 100.0 * sorted.size()) -1;
        return sorted.get(Math.max(index, 0));
    }

}
