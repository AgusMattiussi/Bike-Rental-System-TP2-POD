package ar.edu.itba.pod.tp2.reducer;

import ar.edu.itba.pod.tp2.model.AffluenceInfo;
import ar.edu.itba.pod.tp2.model.Pair;
import ar.edu.itba.pod.tp2.model.Triple;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class AffluenceByStationReducerFactory implements ReducerFactory<Integer, Pair<LocalDateTime, Integer>, AffluenceInfo> {
    @Override
    public Reducer<Pair<LocalDateTime, Integer>, AffluenceInfo> newReducer(Integer integer) {
        return new AffluenceByStationReducer();
    }

    private static class AffluenceByStationReducer extends Reducer<Pair<LocalDateTime, Integer>, AffluenceInfo> {
        private Map<LocalDateTime, Integer> affluenceByDay = new HashMap<>();

        @Override
        public void beginReduce() {
            affluenceByDay.clear();
        }

        @Override
        public void reduce(Pair<LocalDateTime, Integer> pair) {
            LocalDateTime time = pair.first();
            Integer affluence = pair.second();

            affluenceByDay.compute(time, (key, currentValue) -> {
                if (currentValue == null) {
                    return affluence;
                } else {
                    return currentValue + affluence;
                }
            });
        }

        @Override
        public AffluenceInfo finalizeReduce(){
            int positiveDays = 0;
            int negativeDays = 0;
            int neutralDays = 0;

            for (int count : affluenceByDay.values()) {
                if (count > 0) {
                    positiveDays++;
                } else if (count < 0) {
                    negativeDays++;
                } else {
                    neutralDays++;
                }
            }

            return new AffluenceInfo(positiveDays, negativeDays, neutralDays);
        }
    }
}
