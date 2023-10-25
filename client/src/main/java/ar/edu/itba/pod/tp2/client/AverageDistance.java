package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.collators.AverageDistanceCollator;
import ar.edu.itba.pod.tp2.combiners.AverageDistanceCombinerFactory;
import ar.edu.itba.pod.tp2.mapper.AverageDistanceMapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.reducer.AverageDistanceReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AverageDistance {
    private static Logger logger = LoggerFactory.getLogger(AverageDistance.class);
    public static void averageClientSolver(HazelcastInstance hazelcastInstance, int n, IMap<String, BikeTrip> bikeIMap, List<Station> stations, String outPath) {
        // Example = ./query2 -Daddresses='10.6.0.1:5701' -DinPath=. -DoutPath=. -Dn=4
        logger.info("Average Distance Client Starting...");

        JobTracker jobTracker = hazelcastInstance.getJobTracker("averageDistance");
        Job<String, BikeTrip> job = jobTracker.newJob(KeyValueSource.fromMap(bikeIMap));


        try {
            List<Map.Entry<String, Double>> result = job
                    .mapper(new AverageDistanceMapper(stations))
                    .combiner(new AverageDistanceCombinerFactory())
                    .reducer(new AverageDistanceReducerFactory())
                    .submit(new AverageDistanceCollator()).get();


            writeResultToFile(outPath , result, n, outPath);

        }catch (Exception e) {
            logger.error(e.toString());
        }

    }

    static void writeResultToFile(String path, List<Map.Entry<String, Double>> result, int n, String outPath){
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter(path, false));
            buffer.write("station;avg_distance");

            List<Map.Entry<String, Double>> croppedResults = result.stream().limit(n).toList();

            for (Map.Entry<String, Double> res : croppedResults){
                buffer.newLine();
                buffer.write(res.getKey() + ";" + res.getValue().toString());
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
