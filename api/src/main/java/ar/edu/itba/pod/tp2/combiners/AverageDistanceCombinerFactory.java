package ar.edu.itba.pod.tp2.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


public class AverageDistanceCombinerFactory implements CombinerFactory<String, Double, Double> {

    @Override
    public Combiner<Double, Double> newCombiner(String s) {
        return new AverageDistanceCombiner();
    }

    private class AverageDistanceCombiner extends Combiner<Double, Double> {
        private double distanceSum = 0;
        private double journeysAmount = 0;


        @Override
        public void combine( Double distance ) {
            distanceSum += distance;
            journeysAmount++;
        }

        @Override
        public void reset() {
            distanceSum = 0;
            journeysAmount = 0;
        }

        @Override
        public Double finalizeChunk() {
            return distanceSum/journeysAmount;
        }
    }
}
