package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.collators.LongestTripCollator;
import ar.edu.itba.pod.tp2.combiners.LongestTripCombinerFactory;
import ar.edu.itba.pod.tp2.mapper.LongestTripMapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.reducer.LongestTripReducerFactory;
import com.hazelcast.config.JobTrackerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query3 implements Runnable {
    private final String jobName;
    private final HazelcastInstance hazelcast;
    private final IMap<Integer, Station> stations;
    private final IMap<Integer, BikeTrip> trips;

    public Query3(String jobName, HazelcastInstance hazelcast,
                  IMap<Integer, Station> stations, IMap<Integer, BikeTrip> trips) {
        this.jobName = jobName;
        this.hazelcast = hazelcast;
        this.stations = stations;
        this.trips = trips;
    }

    @Override
    public void run() {
        System.out.println("Running query3");
        JobTracker jobTracker = hazelcast.getJobTracker(jobName);
        KeyValueSource<Integer, BikeTrip> source = KeyValueSource.fromMap(trips);

        JobCompletableFuture<List<Pair<String, FinishedBikeTrip>>> future = jobTracker.newJob(source)
                .mapper(new LongestTripMapper())
                .combiner(new LongestTripCombinerFactory())
                .reducer( new LongestTripReducerFactory())
                .submit(new LongestTripCollator(stations));
                // Attach a callback listenerfuture .andThen(buildCallback());

        // Esperamos el resultado de forma sincrónica
        List<Pair<String, FinishedBikeTrip>> result;

        try {
            System.out.println("Waiting for result...");
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Done!");
        System.out.println("Result size: " + result.size());

        //TODO: Cambiar stationIds por stationNames y generar CSV
        result.forEach(pair -> System.out.println(pair.first() + " " + pair.second().toString()));

    }



}
