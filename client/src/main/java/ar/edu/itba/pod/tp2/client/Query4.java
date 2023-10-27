package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.mapper.AffluenceByStationMapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.model.Triple;
import ar.edu.itba.pod.tp2.reducer.AffluenceByStationReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query4 {
    private final String jobName;
    private final HazelcastInstance hazelcast;
    private final IMap<Integer, Station> stations;
    private final IMap<Integer, BikeTrip> trips;
    private final String startDate;
    private final String endDate;

    public Query4(String jobName, HazelcastInstance hazelcast,
                  IMap<Integer, Station> stations, IMap<Integer, BikeTrip> trips, String startDate, String endDate) {
        this.jobName = jobName;
        this.hazelcast = hazelcast;
        this.stations = stations;
        this.trips = trips;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void run() {
        JobTracker jobTracker = hazelcast.getJobTracker(jobName);
        KeyValueSource<Integer, BikeTrip> source = KeyValueSource.fromMap(trips);

        JobCompletableFuture<Map<Integer, Triple<Integer, Integer, Integer>>> future = jobTracker.newJob(source)
                .mapper(new AffluenceByStationMapper(startDate, endDate))
                .reducer(new AffluenceByStationReducerFactory())
                .submit();
        // Attach a callback listenerfuture .andThen(buildCallback());

        // Esperamos el resultado de forma sincrónica
        Map<Integer, Triple<Integer, Integer, Integer>> result;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        //TODO: generar CSV


    }
}
