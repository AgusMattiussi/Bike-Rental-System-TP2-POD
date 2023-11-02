package ar.edu.itba.pod.tp2.reducer;

import ar.edu.itba.pod.tp2.model.AffluenceInfo;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class AffluenceByStationReducerFactory implements ReducerFactory<Integer, Map<LocalDate, Integer>, AffluenceInfo> {
    @Override
    public Reducer<Map<LocalDate, Integer>, AffluenceInfo> newReducer(Integer integer) {
        return new AffluenceByStationReducer();
    }

    private static class AffluenceByStationReducer extends Reducer<Map<LocalDate, Integer>, AffluenceInfo> {
        private Map<LocalDate, Integer> affluenceByDay = new HashMap<>();

        @Override
        public void beginReduce() {
            affluenceByDay.clear();
        }

        @Override
        public void reduce(Map<LocalDate, Integer> partialAffluenceByDay) {
            partialAffluenceByDay.forEach((key, value) -> affluenceByDay.merge(key, value, Integer::sum));
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
