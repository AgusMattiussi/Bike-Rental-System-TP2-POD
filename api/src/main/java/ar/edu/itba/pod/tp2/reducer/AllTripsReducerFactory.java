package ar.edu.itba.pod.tp2.reducer;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import ar.edu.itba.pod.tp2.model.FinishedBikeTrip;
import ar.edu.itba.pod.tp2.model.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class AllTripsReducerFactory implements ReducerFactory<Pair<Integer, Integer>, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(Pair<Integer, Integer> integerIntegerPair) {
        return new AllTripsReducer();
    }

    private static class AllTripsReducer extends Reducer<Integer, Integer> {
        private int sum;

        @Override
        public void beginReduce() {
            System.out.println("Started mapping");
            sum = 0;
        }

        @Override
        public void reduce(Integer integer) {
            sum += integer;
        }

        @Override
        public Integer finalizeReduce() {
            return sum;
        }

    }
}