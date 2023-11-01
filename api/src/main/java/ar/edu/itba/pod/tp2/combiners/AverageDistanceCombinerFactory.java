package ar.edu.itba.pod.tp2.combiners;

import ar.edu.itba.pod.tp2.model.DistanceJourney;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;
@SuppressWarnings("deprecation")
public class AverageDistanceCombinerFactory implements CombinerFactory<Integer, DistanceJourney, DistanceJourney> {

    @Override
    public Combiner<DistanceJourney, DistanceJourney> newCombiner(Integer i) {
        return new AverageDistanceCombiner();
    }

    private class AverageDistanceCombiner extends Combiner<DistanceJourney, DistanceJourney> {
        private DistanceJourney distanceJourney = null;

        @Override
        public void combine( DistanceJourney distance ) {
            if (distanceJourney == null){
                distanceJourney = distance;
            }else{
                distanceJourney.addDistanceAndJourneys(distance.getSumDistances(), distance.getJourneysAmount());
            }
        }

        @Override
        public void reset() {
            distanceJourney = null;
        }

        @Override
        public DistanceJourney finalizeChunk() {
            return distanceJourney;
        }
    }
}
