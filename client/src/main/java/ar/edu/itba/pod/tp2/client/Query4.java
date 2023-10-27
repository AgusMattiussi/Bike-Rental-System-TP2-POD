package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.collators.AffluenceByStationCollator;
import ar.edu.itba.pod.tp2.mapper.AffluenceByStationMapper;
import ar.edu.itba.pod.tp2.model.*;
import ar.edu.itba.pod.tp2.reducer.AffluenceByStationReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.util.List;
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

        JobCompletableFuture<List<Pair<String, AffluenceInfo>>> future = jobTracker.newJob(source)
                .mapper(new AffluenceByStationMapper(startDate, endDate))
                .reducer(new AffluenceByStationReducerFactory())
                .submit(new AffluenceByStationCollator(stations));
        // Attach a callback listenerfuture .andThen(buildCallback());

        // Esperamos el resultado de forma sincrónica
        List<Pair<String, AffluenceInfo>> result;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        //TODO: generar CSV


    }
}
