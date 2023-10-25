package ar.edu.itba.pod.tp2.collators;

import com.hazelcast.mapreduce.Collator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AverageDistanceCollator implements Collator<Map.Entry<String, Double>, List<Map.Entry<String,Double>>> {
//    TODO: check si ordena descendentemente y luego alfabeticamente
    @Override
    public List<Map.Entry<String, Double>> collate(Iterable<Map.Entry<String, Double>> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).sorted((o1, o2) -> {
            int numberComparison = o2.getValue().compareTo(o1.getValue());
            return numberComparison == 0 ? o1.getKey().compareTo(o2.getKey()) : numberComparison;
        }).collect(Collectors.toList());
    }
}
