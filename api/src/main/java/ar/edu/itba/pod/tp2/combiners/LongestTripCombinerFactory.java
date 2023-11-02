package ar.edu.itba.pod.tp2.combiners;

import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;


@SuppressWarnings("deprecation")
public class LongestTripCombinerFactory implements CombinerFactory<Integer, FinishedBikeTrip, FinishedBikeTrip> {

    @Override
    public Combiner<FinishedBikeTrip, FinishedBikeTrip> newCombiner(Integer integer) {
        return new LongestTripCombiner();
    }

    private static class LongestTripCombiner extends Combiner<FinishedBikeTrip, FinishedBikeTrip> {

        private FinishedBikeTrip longestTrip = null;

        @Override
        public void reset() {
            longestTrip = null;
        }

        @Override
        public void combine(FinishedBikeTrip trip) {
            if(longestTrip == null || trip.isLongerThan(longestTrip))
                longestTrip = trip;
        }

        @Override
        public FinishedBikeTrip finalizeChunk() {
            return longestTrip;
        }
    }

}
