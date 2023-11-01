package ar.edu.itba.pod.tp2.mapper;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.DistanceJourney;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.HashMap;
import java.util.Map;

// Recibe un nombre de estacion y un viaje y emite el nombre de la estacion y al distancia del viaje
@SuppressWarnings("deprecation")
public class AverageDistanceMapper implements Mapper<Integer, BikeTrip, Integer, DistanceJourney>, HazelcastInstanceAware {
    private transient Map<Integer, Station> stations = new HashMap<>();
    private static final String STATIONS_MAP_NAME = "station-map";

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        stations = hazelcastInstance.getMap(STATIONS_MAP_NAME);
    }

    @Override
    public void map(Integer stationId, BikeTrip bikeTrip, Context<Integer, DistanceJourney> context) {
        Integer startId = bikeTrip.getStartStationId();
        Integer endId = bikeTrip.getEndStationId();
        // Solo tenemos en cuenta viajes entre distintas estaciones
        if(startId.equals(endId) || !stations.containsKey(startId) || !stations.containsKey(endId))
            return;

        Station startStation = stations.get(startId);
        Station endStation = stations.get(endId);
        DistanceJourney distanceJourney = new DistanceJourney(startId);
        Double distance = haversine(startStation.getLatitude(), startStation.getLongitude(), endStation.getLatitude(), endStation.getLongitude());
        distanceJourney.addDistance(distance);
        context.emit(startId, distanceJourney);
    }

    static Double haversine(double lat1, double lon1,double lat2, double lon2) {
        // distance between latitudes
        // and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.asin(Math.sqrt(a));
        double EARTH_RADIUS = 6371;

        return EARTH_RADIUS * c;
    }
}
