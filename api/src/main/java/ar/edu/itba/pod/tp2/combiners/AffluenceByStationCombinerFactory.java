package ar.edu.itba.pod.tp2.combiners;

import ar.edu.itba.pod.tp2.model.AffluenceInfo;
import ar.edu.itba.pod.tp2.model.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class AffluenceByStationCombinerFactory implements CombinerFactory<Integer, Pair<LocalDate, Integer>,  Map<LocalDate, Integer>> {

    @Override
    public Combiner<Pair<LocalDate, Integer>,  Map<LocalDate, Integer>> newCombiner(Integer integer) {
        return new AffluenceByStationCombiner();
    }

    private static class AffluenceByStationCombiner extends Combiner<Pair<LocalDate, Integer>,  Map<LocalDate, Integer>> {

        private Map<LocalDate, Integer> affluenceByDay;

        @Override
        public void beginCombine() {
            affluenceByDay = new HashMap<>();
        }

        @Override
        public void reset() {
            affluenceByDay = new HashMap<>();
        }

        @Override
        public void combine(Pair<LocalDate, Integer> pair) {
            System.out.println("Combining...");
            LocalDate time = pair.first();
            Integer affluence = pair.second();

            affluenceByDay.compute(time, (key, currentValue)
                    -> currentValue == null ? affluence : currentValue + affluence);
        }

        @Override
        public  Map<LocalDate, Integer> finalizeChunk() {
            return affluenceByDay;
        }
    }
}
