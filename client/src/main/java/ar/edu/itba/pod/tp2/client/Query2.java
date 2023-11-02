package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.collators.AverageDistanceCollator;
import ar.edu.itba.pod.tp2.mapper.AverageDistanceMapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.reducer.AverageDistanceReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query2 implements Runnable{
    private static final String OUT_CSV_HEADER = "station;avg_distance";
    private static final String QUERY_2_CSV_NAME = "query2.csv";

    private final String jobName;
    private final HazelcastInstance hazelcast;
    private final int n;
    private final IMap<Integer,BikeTrip> trips;
    private final IMap<Integer, Station> stations;
    private final String outPath;

    public Query2(String jobName, HazelcastInstance hazelcastInstance, int n, IMap<Integer, BikeTrip> trips, IMap<Integer, Station> stations, String outPath){
        this.jobName = jobName;
        this.hazelcast = hazelcastInstance;
        this.n = n;
        this.trips = trips;
        this.stations = stations;
        this.outPath = outPath;
    }

    @Override
    public void run() {
        JobTracker jobTracker = hazelcast.getJobTracker(jobName);
        KeyValueSource<Integer, BikeTrip> source = KeyValueSource.fromMap(trips);

        JobCompletableFuture<List<Pair<String, Double>>> future = jobTracker.newJob(source)
                .mapper(new AverageDistanceMapper())
//                .combiner(new AverageDistanceCombinerFactory())
                .reducer(new AverageDistanceReducerFactory())
                .submit(new AverageDistanceCollator(stations));

        List<Pair<String, Double>> result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToFile(result, n);
    }

    private void writeResultToFile (List<Pair<String, Double>> result,int n){
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(outPath + QUERY_2_CSV_NAME))) {
            buffWriter.write(OUT_CSV_HEADER);
            List<Pair<String, Double>> croppedResults = result.stream().limit(n).toList();

            for (Pair<String, Double> res : croppedResults) {
                buffWriter.newLine();
                buffWriter.write(res.first() + ";" + new DecimalFormat("#.##").format(res.second()));
            }
            buffWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}