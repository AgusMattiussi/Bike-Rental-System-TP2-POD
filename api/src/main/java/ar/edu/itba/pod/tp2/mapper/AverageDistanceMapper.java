package ar.edu.itba.pod.tp2.mapper;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Recibe un nombre de estacion y un viaje y emite el nombre de la estacion y al distancia del viaje
public class AverageDistanceMapper implements Mapper<Integer, BikeTrip, String, Double> {
    private Map<Integer, Station> stations = new HashMap<>();


    public AverageDistanceMapper(List<Station> stationList) {
        for (Station station: stationList) {
            stations.put(station.id(), station);
        }
    }

    @Override
    public void map(Integer stationId, BikeTrip bikeTrip, Context<String, Double> context) {
        double distances_total = 0;
        if (stations.containsKey(stationId)){
            Station startStation = stations.get(bikeTrip.getStartStationId());
            if (startStation.id() == stationId){
                Station endStation = stations.get(bikeTrip.getEndStationId());
                distances_total = haversine(startStation.latitude(), startStation.longitude(), endStation.latitude(), endStation.longitude());
            }
            context.emit(startStation.name(), distances_total);
        }

    }

    static double haversine(double lat1, double lon1,
                            double lat2, double lon2)
    {
        // distance between latitudes
        // and longitudes
        double dLat = (lat2 - lat1) *
                Math.PI / 180.0;
        double dLon = (lon2 - lon1) *
                Math.PI / 180.0;

        // convert to radians
        lat1 = (lat1) * Math.PI / 180.0;
        lat2 = (lat2) * Math.PI / 180.0;

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) * Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }
}
