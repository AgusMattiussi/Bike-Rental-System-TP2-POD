package ar.edu.itba.pod.tp2.collators;

import ar.edu.itba.pod.tp2.model.BikeTripCount;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class AllTripsCollator implements Collator<Map.Entry<Pair<Integer, Integer>, Integer>, List<BikeTripCount>> {
    private final IMap<Integer, Station> stations;

    public AllTripsCollator(IMap<Integer, Station> stations) {
        this.stations = stations;
    }


    @Override
    public List<BikeTripCount> collate(Iterable<Map.Entry<Pair<Integer, Integer>, Integer>> values) {
        List<BikeTripCount> sortedValues = new ArrayList<>();

        // Resolvemos los nombres de las estaciones de origen y destino
        List<BikeTripCount> finalSortedValues = sortedValues;
        values.forEach(entry -> {
            Station startStation = stations.get(entry.getKey().first());
            Station endStation = stations.get(entry.getKey().second());
            BikeTripCount toAdd = new BikeTripCount(startStation.getName(), endStation.getName(), entry.getValue());
            finalSortedValues.add(toAdd);
        });

        sortedValues = sortedValues.parallelStream()
                .sorted(new TripsAndNameComparator())
                .collect(Collectors.toList());

        return sortedValues;
    }

    private static class TripsAndNameComparator implements Comparator<BikeTripCount> {
        @Override
        public int compare(BikeTripCount o1, BikeTripCount o2) {
            // Descendente por cantidad de viajes
            int tripComp = Integer.compare(o2.getTripCount(), o1.getTripCount());
            if(tripComp != 0) {
                return tripComp;
            }
            // Alfabetico por nombre de startStation
            int startStationComp = o1.getStartStation().toLowerCase().compareTo(o2.getStartStation().toLowerCase());
            if(startStationComp != 0) {
                return startStationComp;
            }
            // Alfabetico por nombre de endStation
            return o1.getEndStation().toLowerCase().compareTo(o2.getEndStation().toLowerCase());
        }
    }

}
