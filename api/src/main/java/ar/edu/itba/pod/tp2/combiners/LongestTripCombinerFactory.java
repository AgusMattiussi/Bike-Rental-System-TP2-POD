package ar.edu.itba.pod.tp2.combiners;

import ar.edu.itba.pod.tp2.model.Triple;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.time.LocalDateTime;

@SuppressWarnings("deprecation")
public class LongestTripCombinerFactory implements CombinerFactory<Integer,
        Triple<Integer, Long, LocalDateTime>, Triple<Integer, Long, LocalDateTime>> {

    @Override
    public Combiner<Triple<Integer, Long, LocalDateTime>, Triple<Integer, Long, LocalDateTime>> newCombiner(Integer integer) {
        return new LongestTripCombiner();
    }

    private static class LongestTripCombiner extends Combiner<Triple<Integer, Long, LocalDateTime>, Triple<Integer, Long, LocalDateTime>> {

        private Triple<Integer, Long, LocalDateTime> longestTrip = null;

        @Override
        public void reset() {
            longestTrip = null;
        }

        @Override
        public void combine(Triple<Integer, Long, LocalDateTime> trip) {
            if(longestTrip == null || isTripLonger(trip))
                longestTrip = trip;
        }

        @Override
        public Triple<Integer, Long, LocalDateTime> finalizeChunk() {
            return longestTrip;
        }

        private boolean isTripLonger(Triple<Integer, Long, LocalDateTime> trip) {
            return trip.second() > longestTrip.second() &&       // La duracion es mayor
                    trip.third().isBefore(longestTrip.third());  // La fecha de inicio es mas reciente
        }
    }

}
