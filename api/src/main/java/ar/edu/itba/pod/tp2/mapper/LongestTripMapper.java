package ar.edu.itba.pod.tp2.mapper;

import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.model.Triple;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;

import java.time.Duration;
import java.time.LocalDateTime;

// Recibe un ID de viaje y un BikeTrip y emite como key el ID de la estación de inicio y como value una terna Triple
// con el ID de la estacion de destino, la duración en minutos del viaje y la fecha de inicio del viaje (para desempatar
// viajes de igual duración)
@SuppressWarnings("deprecation")
public class LongestTripMapper implements Mapper<Integer, BikeTrip, Integer, FinishedBikeTrip>, HazelcastInstanceAware {

    private IMap<Integer, Station> stations;
    private static final String STATIONS_MAP_NAME = "stations"; //TODO: Deberia ser parametro?

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        stations = hazelcastInstance.getMap(STATIONS_MAP_NAME);
    }

    @Override
    public void map(Integer integer, BikeTrip bikeTrip, Context<Integer, FinishedBikeTrip> context) {
        Integer startStationId = bikeTrip.getStartStationId();
        Integer endStationId = bikeTrip.getEndStationId();

        // Solo tenemos en cuenta viajes entre distintas estaciones
        //TODO: Validar si son nulls?
        if(startStationId.equals(endStationId) || !stations.containsKey(startStationId) || !stations.containsKey(endStationId)) {
            return;
        }

        // Calculamos la duracion del viaje en minutos
        Duration tripDuration = Duration.between(bikeTrip.getStartDate(), bikeTrip.getEndDate());

        // Emitimos una tupla (startStationId, (endStationId, tripDuration, startDateTime))
        context.emit(startStationId, new FinishedBikeTrip(endStationId, tripDuration.toMinutes(), bikeTrip.getStartDate()));
    }


}
