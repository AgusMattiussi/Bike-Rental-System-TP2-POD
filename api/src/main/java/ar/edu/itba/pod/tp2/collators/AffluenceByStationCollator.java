package ar.edu.itba.pod.tp2.collators;

import ar.edu.itba.pod.tp2.model.AffluenceInfo;
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
public class AffluenceByStationCollator implements Collator<Map.Entry<Integer, AffluenceInfo>, List<Pair<String, AffluenceInfo>>> {
    private final IMap<Integer, Station> stations;

    public AffluenceByStationCollator(IMap<Integer, Station> stations) {
        this.stations = stations;
    }

    @Override
    public List<Pair<String, AffluenceInfo>> collate(Iterable<Map.Entry<Integer, AffluenceInfo>> values) {
        List<Pair<String, AffluenceInfo>> sortedValues = new ArrayList<>();

        // Resolvemos los nombres de las estaciones
        List<Pair<String, AffluenceInfo>> finalSortedValues = sortedValues;
        values.forEach(entry -> {
            Station station = stations.get(entry.getKey());
            AffluenceInfo affluenceInfo = entry.getValue();

            String stationName = station.getName();
            affluenceInfo.setStationName(stationName);

            finalSortedValues.add(new Pair<>(stationName, affluenceInfo));
        });

        sortedValues = sortedValues.parallelStream()
                .sorted(new AffluenceComparator())
                .collect(Collectors.toList());

        return sortedValues;
    }

    private static class AffluenceComparator implements Comparator<Pair<String, AffluenceInfo>> {
        @Override
        public int compare(Pair<String, AffluenceInfo> o1, Pair<String, AffluenceInfo> o2) {
            // Descendente por cantidad de dias positivos
            int aux = Double.compare(o2.second().getPositiveDays(), o1.second().getPositiveDays());
            // Alfabetico por nombre de station
            if(aux == 0)
                aux = o1.first().toLowerCase().compareTo(o2.first().toLowerCase());
            return aux;
        }
    }
}
