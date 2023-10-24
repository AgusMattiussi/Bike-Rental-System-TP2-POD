package mapper;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import model.BikeTrip;
import model.Pair;
import model.Station;

import java.time.Duration;
import java.util.Map;

// Recibe un ID de viaje y un BikeTrip y emite como key el ID de la estación de inicio y como value un par
// con el ID de la estacion de destino y la duración en minutos del viaje.
public class LongestTripMapper implements Mapper<Integer, BikeTrip, Integer, Pair<Integer, Long>>, HazelcastInstanceAware {

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
    public void map(Integer integer, BikeTrip bikeTrip, Context<Integer, Pair<Integer, Long>> context) {
        Integer startStationId = bikeTrip.getStartStationId();
        Integer endStationId = bikeTrip.getEndStationId();


        // Solo tenemos en cuenta viajes entre distintas estaciones
        //TODO: Validar si son nulls?
        if(startStationId.equals(endStationId) || !stations.containsKey(startStationId) || !stations.containsKey(endStationId)) {
            return;
        }

        // Calculamos la duracion del viaje en minutos
        Duration tripDuration = Duration.between(bikeTrip.getStartDate(), bikeTrip.getEndDate());

        // Emitimos una tupla (startStationId, (endStationId, tripDuration))
        context.emit(startStationId, new Pair<>(endStationId, tripDuration.toMinutes()));
    }


}
