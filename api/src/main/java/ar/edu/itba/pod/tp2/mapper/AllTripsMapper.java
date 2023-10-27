package ar.edu.itba.pod.tp2.mapper;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.model.Triple;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.Duration;
import java.time.LocalDateTime;

// Recibe un ID y viaje, emite las estaciones de inicio y salida
@SuppressWarnings("deprecation")
public class AllTripsMapper implements Mapper<Integer, BikeTrip, Pair<Integer, Integer>, Integer>, HazelcastInstanceAware {

    private IMap<Integer, Station> stations;
    private static final String STATIONS_MAP_NAME = "stations";

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        stations = hazelcastInstance.getMap(STATIONS_MAP_NAME);
    }

    @Override
    public void map(Integer integer, BikeTrip bikeTrip, Context<Pair<Integer, Integer>, Integer> context) {
        Integer startStationId = bikeTrip.getStartStationId();
        Integer endStationId = bikeTrip.getEndStationId();

        // No se contemplan viajes circulares => estacio de inicio y fin deben ser diferentes.
        if(startStationId.equals(endStationId) || !stations.containsKey(startStationId) || !stations.containsKey(endStationId)) {
            return;
        }
        // Emitimos un par <StartId, EndId>
        context.emit(new Pair<>(startStationId, endStationId), 1);
    }
}