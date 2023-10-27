package ar.edu.itba.pod.tp2.client.utils;

import ar.edu.itba.pod.tp2.model.Station;
import com.hazelcast.core.IMap;

import java.util.Map;

public class StationsCSVBatchPopulator extends CSVBatchPopulator<Integer, Station> {

    public StationsCSVBatchPopulator(String path, IMap<Integer, Station> hazelcastMap) {
        super(path, hazelcastMap);
    }

    @Override
    protected Map.Entry<Integer, Station> consumeNextLine(String[] line) {
        //Csv Title: pk;name;latitude;longitude
        int id = Integer.parseInt(line[0]);
        String name = line[1];
        double latitude = Double.parseDouble(line[2]);
        double longitude = Double.parseDouble(line[3]);

        return Map.entry(id, new Station(id, name, latitude, longitude));
    }
}
