package ar.edu.itba.pod.tp2.client.utils;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import com.hazelcast.core.IMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class BikeTripCSVBatchPopulator extends CSVBatchPopulator<Integer, BikeTrip> {
    // TODO: Considerar si no es mejor tener una lista
    private static int bikeTripId = 1;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public BikeTripCSVBatchPopulator(String path, IMap<Integer, BikeTrip> hazelcastMap) {
        super(path, hazelcastMap);
    }

    @Override
    protected Map.Entry<Integer, BikeTrip> consumeNextLine(String[] line) {
        //CSV Title: start_date;emplacement_pk_start;end_date;emplacement_pk_end;is_member
        LocalDateTime startDate = LocalDateTime.parse(line[0], formatter);
        int startStationId = Integer.parseInt(line[1]);
        LocalDateTime endDate = LocalDateTime.parse(line[2], formatter);
        int endStationId = Integer.parseInt(line[3]);
        boolean isMember = line[4].equals("1");

        return Map.entry(bikeTripId++, new BikeTrip(startStationId, startDate, endStationId, endDate, isMember));
    }
}
