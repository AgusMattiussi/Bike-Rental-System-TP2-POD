package ar.edu.itba.pod.tp2.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

public class AverageDistanceCombinerFactory implements CombinerFactory<Integer, Double, Double> {

    @Override
    public Combiner<Double, Double> newCombiner(Integer i) {
        return new AverageDistanceCombiner();
    }

    private class AverageDistanceCombiner extends Combiner<Double, Double> {
        private double distanceSum = 0;
        private double totalTrips = 0;

        @Override
        public void combine( Double distance ) {
            distanceSum += distance;
            totalTrips += 1;
        }

        @Override
        public void reset() {
            distanceSum = 0;
            totalTrips = 0;
        }

        @Override
        public Double finalizeChunk() {
            return totalTrips == 0 ? 0 : distanceSum / totalTrips;
        }
    }
}
