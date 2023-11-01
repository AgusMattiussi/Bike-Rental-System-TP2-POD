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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query4 implements Runnable {
    private static final String OUT_CSV_HEADER = "station;pos_afflux;neutral_afflux;negative_afflux\n";
    private static final String QUERY_4_CSV_NAME = "query4.csv";
    private final String jobName;
    private final HazelcastInstance hazelcast;
    private final IMap<Integer, Station> stations;
    private final IMap<Integer, BikeTrip> trips;
    private final String startDate;
    private final String endDate;
    private final String outPath;

    public Query4(String jobName, HazelcastInstance hazelcast,
                  IMap<Integer, Station> stations, IMap<Integer, BikeTrip> trips, String startDate, String endDate, String outPath) {
        this.jobName = jobName;
        this.hazelcast = hazelcast;
        this.stations = stations;
        this.trips = trips;
        this.startDate = startDate;
        this.endDate = endDate;
        this.outPath = outPath;
    }

    @Override
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

        writeResultToFile(result);
    }

    private void writeResultToFile(List<Pair<String, AffluenceInfo>> result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outPath + QUERY_4_CSV_NAME))) {
            writer.write(OUT_CSV_HEADER);

            for (Pair<String, AffluenceInfo> pair : result)
                writer.write(nextLine(pair));
            writer.flush();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String nextLine(Pair<String, AffluenceInfo> pair) {
        StringBuilder sb = new StringBuilder()
                .append(pair.first()).append(';')
                .append(pair.second().getPositiveDays()).append(';')
                .append(pair.second().getNeutralDays()).append(';')
                .append(pair.second().getNegativeDays()).append('\n');
        return sb.toString();
    }
}
