package ar.edu.itba.pod.tp2.client;
import ar.edu.itba.pod.tp2.combiners.LongestTripCombinerFactory;
import ar.edu.itba.pod.tp2.mapper.AllTripsMapper;
import ar.edu.itba.pod.tp2.mapper.LongestTripMapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.reducer.LongestTripReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import java.util.Map;
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
        System.out.println("here");
//
//        JobCompletableFuture<Map<Integer, Pair<Integer, Long>>> future = jobTracker.newJob(source)
//                .mapper(new AllTripsMapper())
//                .reducer( new LongestTripReducerFactory())
//                .submit(); // Attach a callback listenerfuture .andThen(buildCallback());
//
//        // Esperamos el resultado de forma sincrónica
//        Map<Integer, Pair<Integer, Long>> result;
//        try {
//            result = future.get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//
//        result.entrySet().stream()
//                .sorted(/* TODO: Sort */)
//                .map(entry -> "From: " + entry.getKey() + " - To: " + entry.getValue().first())
//                .forEach(System.out::println);
        }
    }



