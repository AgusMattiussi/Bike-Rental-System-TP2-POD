package ar.edu.itba.pod.tp2.keypredicates;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import com.hazelcast.mapreduce.KeyPredicate;

import java.time.LocalDateTime;

@SuppressWarnings("deprecation")
public class DateRangePredicate implements KeyPredicate<BikeTrip> {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public DateRangePredicate(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public boolean evaluate(BikeTrip bikeTrip) {
        LocalDateTime tripStartDate = bikeTrip.startDate();
        LocalDateTime tripEndDate = bikeTrip.endDate();

        return (tripStartDate.isAfter(startDate) || tripStartDate.isEqual(startDate)) &&
                (tripEndDate.isBefore(endDate) || tripEndDate.isEqual(endDate));
    }
}
