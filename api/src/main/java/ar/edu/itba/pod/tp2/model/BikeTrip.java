package ar.edu.itba.pod.tp2.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public final class BikeTrip implements DataSerializable {
    private int startStationId;
    private LocalDateTime startDate;
    private int endStationId;
    private LocalDateTime endDate;
    private boolean isMember;

    /* Para Hazelcast */
    public BikeTrip() {
    }

    public BikeTrip(int startStationId, LocalDateTime startDate, int endStationId, LocalDateTime endDate, boolean isMember) {
        this.startStationId = startStationId;
        this.startDate = startDate;
        this.endStationId = endStationId;
        this.endDate = endDate;
        this.isMember = isMember;
    }

    public int getStartStationId() {
        return startStationId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public int getEndStationId() {
        return endStationId;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public boolean isMember() {
        return isMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BikeTrip that = (BikeTrip) o;

        if (startStationId != that.startStationId) return false;
        if (endStationId != that.endStationId) return false;
        if (isMember != that.isMember) return false;
        if (!startDate.equals(that.startDate)) return false;
        return endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        int result = startStationId;
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endStationId;
        result = 31 * result + endDate.hashCode();
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

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeInt(startStationId);
        out.writeLong(startDate.toInstant(ZoneOffset.UTC).toEpochMilli());
        out.writeInt(endStationId);
        out.writeLong(endDate.toInstant(ZoneOffset.UTC).toEpochMilli());
        out.writeBoolean(isMember);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        startStationId = in.readInt();
        startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(in.readLong()), ZoneOffset.UTC);
        endStationId = in.readInt();
        endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(in.readLong()), ZoneOffset.UTC);
        isMember = in.readBoolean();
    }
}
