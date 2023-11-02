package ar.edu.itba.pod.tp2.reducer;

import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


// (startStationId, (endStationId, tripDuration, startDateTime)) -> (endStationId, longestTripDuration)
@SuppressWarnings("deprecation")
public class LongestTripReducerFactory implements ReducerFactory<Integer, FinishedBikeTrip, FinishedBikeTrip> {

    @Override
    public Reducer<FinishedBikeTrip, FinishedBikeTrip> newReducer(Integer startStationId) {
        return new LongestTripReducer();
    }

    private static class LongestTripReducer extends Reducer<FinishedBikeTrip, FinishedBikeTrip> {

        private FinishedBikeTrip longestTrip = null;

        @Override
        public void beginReduce() {
            longestTrip = null;
        }

        @Override
        public void reduce(FinishedBikeTrip trip) {
            if (longestTrip == null || trip.isLongerThan(longestTrip))
                longestTrip = trip;
        }

        @Override
        public FinishedBikeTrip finalizeReduce() {
            return longestTrip;
        }
    }

}
