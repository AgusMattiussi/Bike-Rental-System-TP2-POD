package ar.edu.itba.pod.tp2.mapper;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("deprecation")
public class AffluenceByStationMapper implements Mapper<Integer, BikeTrip, Integer, Pair<LocalDate, Integer>>, HazelcastInstanceAware {

    private IMap<Integer, Station> stations;
    private static final String STATIONS_MAP_NAME = "station-map";

    private LocalDate startDate;
    private LocalDate endDate;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public AffluenceByStationMapper(String startDate, String endDate) {
        this.startDate = LocalDate.parse(startDate, formatter);
        this.endDate = LocalDate.parse(endDate, formatter);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        stations = hazelcastInstance.getMap(STATIONS_MAP_NAME);
    }

    @Override
    public void map(Integer integer, BikeTrip bikeTrip, Context<Integer, Pair<LocalDate, Integer>> context) {
        System.out.println("Mapping...");

        Integer startStationId = bikeTrip.getStartStationId();
        Integer endStationId = bikeTrip.getEndStationId();

        LocalDate tripStartDate = LocalDate.from(bikeTrip.getStartDate());
        LocalDate tripEndDate = LocalDate.from(bikeTrip.getEndDate());

        /* Nos interesan viajes entre distintas estaciones,
           que esten en el rango de fechas y que las estaciones existan en el csv de estaciones */
        if(startStationId.equals(endStationId) || !stationsInStationsCsv(startStationId, endStationId) || !datesInRange(tripStartDate, tripEndDate)) {
            return;
        }

        context.emit(startStationId, new Pair<>(tripStartDate, 1));
        context.emit(endStationId, new Pair<>(tripEndDate, -1));
    }

    private boolean datesInRange(LocalDate tripStartDate, LocalDate tripEndDate) {
        return (tripStartDate.isAfter(startDate) || tripStartDate.isEqual(startDate)) &&
                (tripEndDate.isBefore(endDate) || tripEndDate.isEqual(endDate));
    }

    private boolean stationsInStationsCsv(Integer startStationId, Integer endStationId) {
        return stations.containsKey(startStationId) && stations.containsKey(endStationId);
    }
}