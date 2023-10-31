package ar.edu.itba.pod.tp2.combiners;

import ar.edu.itba.pod.tp2.model.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class AllTripsCombinerFactory implements CombinerFactory<Pair<Integer,Integer>,Integer, Integer> {
    @Override
    public Combiner<Integer, Integer> newCombiner(Pair<Integer, Integer> integerIntegerPair) {
        return new AllTripsCombiner();
    }

    private static class AllTripsCombiner extends Combiner<Integer, Integer> {
        private int sum;

        @Override
        public void combine(Integer integer) {
            sum += integer;
        }

        @Override
        public Integer finalizeChunk() {
            return sum;
        }

        @Override
        public void reset() {
            sum = 0;
        }
    }
}
