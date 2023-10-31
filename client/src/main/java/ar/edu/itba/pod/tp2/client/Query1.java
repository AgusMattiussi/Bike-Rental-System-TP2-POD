package ar.edu.itba.pod.tp2.client;
import ar.edu.itba.pod.tp2.collators.AllTripsCollator;
import ar.edu.itba.pod.tp2.combiners.AllTripsCombinerFactory;
import ar.edu.itba.pod.tp2.mapper.AllTripsMapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.BikeTripCount;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.reducer.AllTripsReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query1 {
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

        System.out.println("Done!");
        System.out.println("Result size: " + result.size());

    }


}



