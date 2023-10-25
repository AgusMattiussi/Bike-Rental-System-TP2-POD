package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AverageDistance {
    private static Logger logger = LoggerFactory.getLogger(AverageDistance.class);
    public static void averageClientSolver(HazelcastInstance hazelcastInstance, String n, IMap<Integer, BikeTrip> bikeIMap, IMap<Integer, Station> stationIMap) {
        // Example = ./query2 -Daddresses='10.6.0.1:5701' -DinPath=. -DoutPath=. -Dn=4
        logger.info("AverageDistance Client Starting...");
        try {
            String mapName = "testMap";
            IMap<Integer, String> testMapFromMember = hazelcastInstance.getMap(mapName);


        }catch (Exception e) {
            logger.error(e.toString());
        }

    }

}
