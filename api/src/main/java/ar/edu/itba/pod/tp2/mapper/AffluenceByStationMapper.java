package ar.edu.itba.pod.tp2.mapper;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDateTime;

@SuppressWarnings("deprecation")
public class AffluenceByStationMapper implements Mapper<Integer, BikeTrip, Integer, Pair<LocalDateTime, Integer>>, HazelcastInstanceAware {

    private IMap<Integer, Station> stations;
    private static final String STATIONS_MAP_NAME = "station-map";

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public AffluenceByStationMapper(String startDate, String endDate) {
        this.startDate = LocalDateTime.parse(startDate);
        this.endDate = LocalDateTime.parse(endDate);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        stations = hazelcastInstance.getMap(STATIONS_MAP_NAME);
    }

    @Override
    public void map(Integer integer, BikeTrip bikeTrip, Context<Integer, Pair<LocalDateTime, Integer>> context) {
        Integer startStationId = bikeTrip.getStartStationId();
        Integer endStationId = bikeTrip.getEndStationId();

        LocalDateTime tripStartDate = bikeTrip.getStartDate();
        LocalDateTime tripEndDate = bikeTrip.getEndDate();

        /* Nos interesan viajes entre distintas estaciones,
           que esten en el rango de fechas y que las estaciones existan en el csv de estaciones */
        if(startStationId.equals(endStationId) || !stationsInStationsCsv(startStationId, endStationId) || !datesInRange(tripStartDate, tripEndDate)) {
            return;
        }

        context.emit(startStationId, new Pair<>(tripStartDate, 1));
        context.emit(endStationId, new Pair<>(tripEndDate, -1));
    }

    private boolean datesInRange(LocalDateTime tripStartDate, LocalDateTime tripEndDate) {
        return (tripStartDate.isAfter(startDate) || tripStartDate.isEqual(startDate)) &&
                (tripEndDate.isBefore(endDate) || tripEndDate.isEqual(endDate));
    }

    private boolean stationsInStationsCsv(Integer startStationId, Integer endStationId) {
        return stations.containsKey(startStationId) && stations.containsKey(endStationId);
    }
}