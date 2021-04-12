package site.bitlab16.restservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.DayOfWeek;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TrackedPointStatistic {

    @JsonProperty("flow-average")
    EnumMap<DayOfWeek, Map<Integer, Integer>> metrics = new EnumMap<>(DayOfWeek.class);

    public Map<DayOfWeek, Map<Integer, Integer>> getMetrics() {
        return metrics;
    }

    public void addMetrics(DayOfWeek day, Map<Integer, Integer> metric) {
        var keys = metric.keySet();
        if(this.metrics.containsKey(day)) {
            var map = this.metrics.get(day);
            for (var key : keys) {
                var value = metric.get(key);
                if(map.containsKey(key)) {
                    map.put(key, (map.get(key) + value) / 2);
                } else {
                    map.put(key, value);
                }
            }
        } else {
            this.metrics.put(day, metric);
        }
    }

    public void addMetric(DayOfWeek day, Integer key, Integer value) {
        var hoursMap = metrics.get(day);
        if(hoursMap == null) {
            hoursMap = new HashMap<>();
        }
        hoursMap.put(key, value);
        metrics.put(day, hoursMap);
    }


    @Override
    public String toString() {
        return "TrackedPointStatistic{" +
                "metrics=" + metrics +
                '}';
    }
}
