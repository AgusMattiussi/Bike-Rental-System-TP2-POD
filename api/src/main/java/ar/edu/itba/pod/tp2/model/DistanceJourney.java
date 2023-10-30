package ar.edu.itba.pod.tp2.model;

import java.io.Serializable;
import java.util.Objects;

public class DistanceJourney implements Serializable {
    private Integer startStationId;
    private Integer journeysAmount;
    private Double sumDistances;

    public DistanceJourney(Integer startStationId){
        this.startStationId = startStationId;
        this.journeysAmount = 0;
        this.sumDistances = 0.0;
    }

    public Double getSumDistances() {
        return sumDistances;
    }

    public Integer getJourneysAmount() {
        return journeysAmount;
    }

    public void addDistanceAndJourneys(Double sumDistances, Integer journeysAmount){
        this.journeysAmount+=journeysAmount;
        this.sumDistances+=sumDistances;
    }

    public void addDistance(Double distance){
        journeysAmount++;
        sumDistances+=distance;
    }

    public Double getAverage(){
        return journeysAmount == 0? 0 : sumDistances/journeysAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DistanceJourney distanceJourney = (DistanceJourney) o;

        if (!Objects.equals(startStationId, distanceJourney.startStationId)) return false;
        if (Double.compare(sumDistances, distanceJourney.sumDistances) != 0) return false;
        if (Double.compare(journeysAmount, distanceJourney.journeysAmount) != 0) return false;
        return true;
    }
}
