package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public record BikeTrip(LocalDateTime startDate, LocalDateTime endDate, int startStationId, int endStationId,
                       boolean isMember) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BikeTrip bikeTrip = (BikeTrip) o;

        if (startStationId != bikeTrip.startStationId) return false;
        if (endStationId != bikeTrip.endStationId) return false;
        if (isMember != bikeTrip.isMember) return false;
        if (!startDate.equals(bikeTrip.startDate)) return false;
        return endDate.equals(bikeTrip.endDate);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BikeTrip{");
        sb.append("startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", startStationId=").append(startStationId);
        sb.append(", endStationId=").append(endStationId);
        sb.append(", isMember=").append(isMember);
        sb.append('}');
        return sb.toString();
    }
}
