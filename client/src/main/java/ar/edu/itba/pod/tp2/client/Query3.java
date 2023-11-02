package ar.edu.itba.pod.tp2.client;

import ar.edu.itba.pod.tp2.collators.LongestTripCollator;
import ar.edu.itba.pod.tp2.mapper.LongestTripMapper;
import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Station;
import ar.edu.itba.pod.tp2.reducer.LongestTripReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query3 implements Runnable {
    private static final String OUT_CSV_HEADER = "start_station;end_station;start_date;minutes\n";
    private static final String QUERY_3_CSV_NAME = "query3.csv";

    private final String jobName;
    private final HazelcastInstance hazelcast;
    private final IMap<Integer, Station> stations;
    private final IMap<Integer, BikeTrip> trips;
    private final String outPath;

    public Query3(String jobName, HazelcastInstance hazelcast,
                  IMap<Integer, Station> stations, IMap<Integer, BikeTrip> trips,
                  String outPath) {
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

        JobCompletableFuture<List<Pair<String, FinishedBikeTrip>>> future = jobTracker.newJob(source)
                .mapper(new LongestTripMapper())
                .reducer( new LongestTripReducerFactory())
                .submit(new LongestTripCollator(stations));
                // Attach a callback listenerfuture .andThen(buildCallback());

        // Esperamos el resultado de forma sincrónica
        List<Pair<String, FinishedBikeTrip>> result;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToFile(result);
    }

    private void writeResultToFile(List<Pair<String, FinishedBikeTrip>> result) {
        try (BufferedWriter buffWriter = new BufferedWriter(new FileWriter(outPath + QUERY_3_CSV_NAME))) {
            buffWriter.write(OUT_CSV_HEADER);

            for (Pair<String, FinishedBikeTrip> pair : result)
                buffWriter.write(nextLine(pair));
            buffWriter.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextLine(Pair<String, FinishedBikeTrip> pair) {
        StringBuilder sb = new StringBuilder()
                .append(pair.first()).append(';')
                .append(pair.second().toCSV(false, true));
        return sb.toString();
    }
}
