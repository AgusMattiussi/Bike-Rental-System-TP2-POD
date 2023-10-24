package model;

import java.time.LocalDateTime;

public class BikeTrip {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final int startStationId;
    private final int endStationId;
    private final boolean isMember;

    public BikeTrip(LocalDateTime startDate, LocalDateTime endDate, int startStationId, int endStationId, boolean isMember) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
        this.isMember = isMember;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public int getStartStationId() {
        return startStationId;
    }

    public int getEndStationId() {
        return endStationId;
    }

    public boolean isMember() {
        return isMember;
    }

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
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + startStationId;
        result = 31 * result + endStationId;
        result = 31 * result + (isMember ? 1 : 0);
        return result;
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
