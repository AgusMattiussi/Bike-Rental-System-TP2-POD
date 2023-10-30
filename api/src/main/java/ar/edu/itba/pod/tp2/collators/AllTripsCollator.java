package ar.edu.itba.pod.tp2.collators;

import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.IMap;
import com.hazelcast.map.impl.MapEntrySimple;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class AllTripsCollator implements Collator<Map.Entry<Pair<Integer, Integer>, Integer>, List<Map.Entry<Pair<String, String>, Integer>>> {
    private final IMap<Integer, Station> stations;

    public AllTripsCollator(IMap<Integer, Station> stations) {
        this.stations = stations;
    }


    @Override
    public List<Map.Entry<Pair<String, String>, Integer>> collate(Iterable<Map.Entry<Pair<Integer, Integer>, Integer>> values) {
        List<Map.Entry<Pair<String, String>, Integer>> sortedValues = new ArrayList<>();

        values.forEach(entry -> {
            Station startStation = stations.get(entry.getKey().first());
            Station endStation = stations.get(entry.getKey().second());
            Map.Entry<Pair<String, String>, Integer> tripCount = new MapEntrySimple<>(new Pair<>(startStation.getName(), endStation.getName()), entry.getValue());
            sortedValues.add(tripCount);
        });
        sortedValues.sort(new TripsAndNameComparator());
        return sortedValues;
    }

    private static class TripsAndNameComparator implements Comparator<Map.Entry<Pair<String, String>, Integer>> {
        @Override
        public int compare(Map.Entry<Pair<String, String>, Integer> o1, Map.Entry<Pair<String, String>, Integer> o2) {
            // Descendente por cantidad de viajes
            int tripComp = Integer.compare(o2.getValue(), o1.getValue());
            if(tripComp != 0) {
                return tripComp;
            }
            // Alfabetico por nombre de startStation
            int startStationComp = o1.getKey().first().compareTo(o2.getKey().first());
            if(startStationComp != 0) {
                return startStationComp;
            }
            // Alfabetico por nombre de endStation
            return o1.getKey().second().compareTo(o2.getKey().second());
        }
    }

}
