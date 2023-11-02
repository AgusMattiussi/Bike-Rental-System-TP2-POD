package ar.edu.itba.pod.tp2.client;
import ar.edu.itba.pod.tp2.collators.AllTripsCollator;
import ar.edu.itba.pod.tp2.combiners.AllTripsCombinerFactory;
import ar.edu.itba.pod.tp2.mapper.AllTripsMapper;
import ar.edu.itba.pod.tp2.model.*;
import ar.edu.itba.pod.tp2.reducer.AllTripsReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query1 implements Runnable {

    private static final String OUT_CSV_HEADER = "station_a;station_b;trips_between_a_b\n";
    private static final String QUERY_1_CSV_NAME = "query1.csv";

    private final String jobName;
    private final HazelcastInstance hazelcast;
    private final IMap<Integer, Station> stations;
    private final IMap<Integer, BikeTrip> trips;
    private final String outPath;

    public Query1(String jobName, HazelcastInstance hazelcast,
                  IMap<Integer, Station> stations, IMap<Integer, BikeTrip> trips, String outPath) {
        this.jobName = jobName;
        this.hazelcast = hazelcast;
        this.stations = stations;
        this.trips = trips;
        this.outPath = outPath;
    }

    @Override
    public void run() {
        JobTracker jobTracker = hazelcast.getJobTracker(jobName);
        KeyValueSource<Integer, BikeTrip> source = KeyValueSource.fromMap(trips);

        JobCompletableFuture<List<BikeTripCount>> future = jobTracker.newJob(source)
                .mapper(new AllTripsMapper())
                .combiner(new AllTripsCombinerFactory())
                .reducer(new AllTripsReducerFactory())
                .submit(new AllTripsCollator(stations)); // Attach a callback listenerfuture .andThen(buildCallback());

        // Esperamos el resultado de forma sincrónica
        List<BikeTripCount> result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToFile(result);
    }

    private void writeResultToFile(List<BikeTripCount> result) {

        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(outPath + QUERY_1_CSV_NAME))) {
            buffWriter.write(OUT_CSV_HEADER);

            for (BikeTripCount bikeTripCount : result)
                buffWriter.write(nextLine(bikeTripCount));
            buffWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextLine(BikeTripCount bikeTripCount) {
        StringBuilder sb = new StringBuilder()
                .append(bikeTripCount.getStartStation()).append(';')
                .append(bikeTripCount.getEndStation()).append(';')
                .append(bikeTripCount.getTripCount()).append('\n');
        return sb.toString();
    }


}



