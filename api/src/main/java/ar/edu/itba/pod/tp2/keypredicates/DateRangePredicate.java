package ar.edu.itba.pod.tp2.keypredicates;

import ar.edu.itba.pod.tp2.model.BikeTrip;
import com.hazelcast.mapreduce.KeyPredicate;

import java.time.LocalDateTime;

@SuppressWarnings("deprecation")
public class DateRangePredicate implements KeyPredicate<BikeTrip> {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public DateRangePredicate(String startDate, String endDate) {
        this.startDate = LocalDateTime.parse(startDate);
        this.endDate = LocalDateTime.parse(endDate);
    }

    @Override
    public boolean evaluate(BikeTrip bikeTrip) {
        LocalDateTime tripStartDate = bikeTrip.getStartDate();
        LocalDateTime tripEndDate = bikeTrip.getEndDate();

        return (tripStartDate.isAfter(startDate) || tripStartDate.isEqual(startDate)) &&
                (tripEndDate.isBefore(endDate) || tripEndDate.isEqual(endDate));
    }
}
