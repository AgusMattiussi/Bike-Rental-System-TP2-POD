package ar.edu.itba.pod.tp2.reducer;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


public class AverageDistanceReducerFactory implements ReducerFactory<String, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(String s) {
        return new AverageDistanceReducer();
    }

    private class AverageDistanceReducer extends Reducer<Double, Double> {
        private double distanceSum;
        private double journeysAmount;

        @Override
        public void beginReduce () {
            distanceSum = 0;
            journeysAmount = 0;
        }

        @Override
        public void reduce( Double distance ) {
            distanceSum += distance;
            journeysAmount++;
        }

        @Override
        public Double finalizeReduce() {
            return distanceSum/journeysAmount;
        }
    }
}
