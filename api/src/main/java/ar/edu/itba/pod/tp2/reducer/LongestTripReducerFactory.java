package ar.edu.itba.pod.tp2.reducer;

import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Triple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDateTime;

// (startStationId, (endStationId, tripDuration, startDateTime)) -> (endStationId, longestTripDuration)
@SuppressWarnings("deprecation")
public class LongestTripReducerFactory implements ReducerFactory<Integer, Triple<Integer, Long, LocalDateTime>, Pair<Integer, Long>> {

    @Override
    public Reducer<Triple<Integer, Long, LocalDateTime>, Pair<Integer, Long>> newReducer(Integer startStationId) {
        return new LongestTripReducer();
    }

    private static class LongestTripReducer extends Reducer<Triple<Integer, Long, LocalDateTime>, Pair<Integer, Long>> {

        private Triple<Integer, Long, LocalDateTime> longestTrip;
        @Override
        public void beginReduce() {
            longestTrip = null;
        }

        @Override
        public void reduce(Triple<Integer, Long, LocalDateTime> trip) {
            if (longestTrip == null || isTripLonger(trip))
                // Guardamos una copia del trip TODO: Preguntar si es necesario
                longestTrip = trip;
        }

        @Override
        public Pair<Integer, Long> finalizeReduce() {
            return new Pair<>(longestTrip.first(), longestTrip.second());
        }

        private boolean isTripLonger(Triple<Integer, Long, LocalDateTime> trip) {
            return trip.second() > longestTrip.second() &&       // La duracion es mayor
                    trip.third().isBefore(longestTrip.third());  // La fecha de inicio es mas reciente
        }
    }

}
