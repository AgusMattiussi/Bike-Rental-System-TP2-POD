package ar.edu.itba.pod.tp2.reducer;

import ar.edu.itba.pod.tp2.model.DistanceJourney;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;


public class AverageDistanceReducerFactory implements ReducerFactory<Integer, DistanceJourney, DistanceJourney> {

    @Override
    public Reducer<DistanceJourney, DistanceJourney> newReducer(Integer i) {
        return new AverageDistanceReducer();
    }

    private class AverageDistanceReducer extends Reducer<DistanceJourney, DistanceJourney> {
        private DistanceJourney distanceJourney;

        @Override
        public void beginReduce () {
            distanceJourney = null;
        }

        @Override
        public void reduce( DistanceJourney distance ) {
            if (distanceJourney == null){
                distanceJourney = distance;
            }else{
                distanceJourney.addDistanceAndJourneys(distance.getSumDistances(), distance.getJourneysAmount());
            }
        }

        @Override
        public DistanceJourney finalizeReduce() {
            return distanceJourney;
        }
    }
}
