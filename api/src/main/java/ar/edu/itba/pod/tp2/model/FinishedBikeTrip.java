package ar.edu.itba.pod.tp2.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FinishedBikeTrip implements DataSerializable, Comparable<FinishedBikeTrip> {

    private int endStationId;
    private double durationInMinutes;
    private LocalDateTime startDate;

    public FinishedBikeTrip(int endStationId, double durationInMinutes, LocalDateTime startDate) {
        this.endStationId = endStationId;
        this.durationInMinutes = durationInMinutes;
        this.startDate = startDate;
    }

    public int getEndStationId() {
        return endStationId;
    }

    public double getDurationInMinutes() {
        return durationInMinutes;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FinishedBikeTrip that = (FinishedBikeTrip) o;

        if (endStationId != that.endStationId) return false;
        if (Double.compare(durationInMinutes, that.durationInMinutes) != 0) return false;
        return startDate.equals(that.startDate);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = endStationId;
        temp = Double.doubleToLongBits(durationInMinutes);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + startDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FinishedBikeTrip{");
        sb.append("endStationId=").append(endStationId);
        sb.append(", durationInMinutes=").append(durationInMinutes);
        sb.append(", startDate=").append(startDate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(endStationId);
        out.writeDouble(durationInMinutes);
        out.writeLong(startDate.toInstant(ZoneOffset.UTC).toEpochMilli());

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        endStationId = in.readInt();
        durationInMinutes = in.readDouble();
        startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(in.readLong()), ZoneOffset.UTC);
    }

    @Override
    //TODO: Chequear si se esta quedando con el correcto
    public int compareTo(FinishedBikeTrip other) {
        int aux = Double.compare(this.durationInMinutes, other.durationInMinutes);
        // Si las duraciones son iguales, comparar por startDate
        if (aux == 0)
            return other.startDate.compareTo(this.startDate);
        return aux;
    }

    public boolean isLongerThan(FinishedBikeTrip other) {
        return this.compareTo(other) > 0;
    }
}
