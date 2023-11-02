package ar.edu.itba.pod.tp2.collators;

import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Ordena los resultados de mayor a menor duracion
@SuppressWarnings("deprecation")
public class LongestTripCollator implements Collator<Map.Entry<Integer, FinishedBikeTrip>, List<Pair<String, FinishedBikeTrip>>> {

    private final IMap<Integer, Station> stations;

    public LongestTripCollator(IMap<Integer, Station> stations) {
        this.stations = stations;
    }

    @Override
    public List<Pair<String, FinishedBikeTrip>> collate(Iterable<Map.Entry<Integer, FinishedBikeTrip>> values) {
        List<Pair<String, FinishedBikeTrip>> sortedValues = new ArrayList<>();

        // Resolvemos los nombres de las estaciones
        List<Pair<String, FinishedBikeTrip>> finalSortedValues = sortedValues;
        values.forEach(entry -> {
            FinishedBikeTrip trip = entry.getValue();
            Station endStation = stations.get(trip.getEndStationId());
            Station startStation = stations.get(entry.getKey());

            trip.setEndStationName(endStation.getName());
            finalSortedValues.add(new Pair<>(startStation.getName(), trip));
        });

        sortedValues = sortedValues.parallelStream()
                .sorted(new DurationAndNameComparator())
                .collect(Collectors.toList());

        return sortedValues;
    }

    private static class DurationAndNameComparator implements Comparator<Pair<String, FinishedBikeTrip>> {
        @Override
        public int compare(Pair<String, FinishedBikeTrip> o1, Pair<String, FinishedBikeTrip> o2) {

            // Descendente por duracion
            int durationComp = Double.compare(o2.second().getDurationInMinutes(), o1.second().getDurationInMinutes());
            if(durationComp != 0)
                return durationComp;
            // Alfabetico por nombre de startStation
            return o1.first().toLowerCase().compareTo(o2.first().toLowerCase());
        }
    }
}
