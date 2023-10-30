package ar.edu.itba.pod.tp2.collators;

import ar.edu.itba.pod.tp2.model.DistanceJourney;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class AverageDistanceCollator implements Collator<Map.Entry<Integer, DistanceJourney>, List<Pair<String,Double>>> {

    private final IMap<Integer, Station> stations;

    public AverageDistanceCollator(IMap<Integer, Station> stations) {
        this.stations = stations;
    }

    @Override
    public List<Pair<String, Double>> collate(Iterable<Map.Entry<Integer, DistanceJourney>> iterable) {
        List<Pair<String, Double>> sortedValues = new ArrayList<>();

        // Resolvemos los nombres de las estaciones de origen y destino
        iterable.forEach(entry -> {
            Station startStation = stations.get(entry.getKey());
            sortedValues.add(new Pair<>(startStation.getName(), entry.getValue().getAverage()));
        });

        sortedValues.sort(new AvgDistanceAndNameComparator());
        return sortedValues;
    }

    private static class AvgDistanceAndNameComparator implements Comparator<Pair<String, Double>> {
        @Override
        public int compare(Pair<String, Double> o1, Pair<String, Double> o2) {
            // Descendente por promedio de distancia
            int durationComp = Double.compare(o2.second(), o1.second());
            if(durationComp != 0)
                return durationComp;
            // Alfabetico por nombre de startStation
            return o1.first().compareTo(o2.first());
        }
    }
}
