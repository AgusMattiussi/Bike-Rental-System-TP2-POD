package ar.edu.itba.pod.tp2.model;

public class BikeTripCount {
    private final String startStation;
    private final String endStation;
    private final Integer tripCount;

    public BikeTripCount(String startStation, String endStation, Integer tripCount) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.tripCount = tripCount;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public Integer getTripCount() {
        return tripCount;
    }
}
