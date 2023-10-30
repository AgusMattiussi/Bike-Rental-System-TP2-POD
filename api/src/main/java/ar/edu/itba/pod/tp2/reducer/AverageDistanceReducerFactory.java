package ar.edu.itba.pod.tp2.reducer;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


public class AverageDistanceReducerFactory implements ReducerFactory<Integer, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(Integer i) {
        return new AverageDistanceReducer();
    }

    private class AverageDistanceReducer extends Reducer<Double, Double> {
        private double distanceSum;
        private double totalTrips;

        @Override
        public void beginReduce () {
            distanceSum = 0;
            totalTrips = 0;
        }

        @Override
        public void reduce( Double distance ) {
            distanceSum += distance;
            totalTrips += 1;
        }

        @Override
        public Double finalizeReduce() {
            return totalTrips == 0 ? 0 : distanceSum / totalTrips;
        }
    }
}
