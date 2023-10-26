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
    private static final String STATIONS_MAP_NAME = "stations";

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        stations = hazelcastInstance.getMap(STATIONS_MAP_NAME);
    }

    @Override
    public void map(Integer integer, BikeTrip bikeTrip, Context<Integer, Pair<LocalDateTime, Integer>> context) {
        Integer startStationId = bikeTrip.getStartStationId();
        Integer endStationId = bikeTrip.getEndStationId();

        //TODO: ver si puedo pasar esto a un keypredicate
        if(!stations.containsKey(startStationId) || !stations.containsKey(endStationId)) {
            return;
        }

        context.emit(startStationId, new Pair<>(bikeTrip.getStartDate(), 1));
        context.emit(endStationId, new Pair<>(bikeTrip.getEndDate(), -1));
    }
}