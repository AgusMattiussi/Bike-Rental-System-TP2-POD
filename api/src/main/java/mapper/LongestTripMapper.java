package mapper;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import model.BikeTrip;
import model.Pair;
import model.Station;
import model.Triple;

import java.time.Duration;
import java.time.LocalDateTime;

// Recibe un ID de viaje y un BikeTrip y emite como key el ID de la estación de inicio y como value una terna Triple
// con el ID de la estacion de destino, la duración en minutos del viaje y la fecha de inicio del viaje (para desempatar
// viajes de igual duración)
@SuppressWarnings("deprecation")
public class LongestTripMapper implements Mapper<Integer, BikeTrip, Integer, Triple<Integer, Long, LocalDateTime>>, HazelcastInstanceAware {

    private IMap<Integer, Station> stations;
    private final String stationsMapName;

    public LongestTripMapper(String stationsMapName) {
        this.stationsMapName = stationsMapName;
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        stations = hazelcastInstance.getMap(stationsMapName);
    }

    @Override
    public void map(Integer integer, BikeTrip bikeTrip, Context<Integer, Triple<Integer, Long, LocalDateTime>> context) {
        Integer startStationId = bikeTrip.startStationId();
        Integer endStationId = bikeTrip.endStationId();

        // Solo tenemos en cuenta viajes entre distintas estaciones
        //TODO: Validar si son nulls?
        if(startStationId.equals(endStationId) || !stations.containsKey(startStationId) || !stations.containsKey(endStationId)) {
            return;
        }

        // Calculamos la duracion del viaje en minutos
        Duration tripDuration = Duration.between(bikeTrip.startDate(), bikeTrip.endDate());

        // Emitimos una tupla (startStationId, (endStationId, tripDuration, startDateTime))
        context.emit(startStationId, new Triple<>(endStationId, tripDuration.toMinutes(), bikeTrip.startDate()));
    }


}
