package ar.edu.itba.pod.tp2.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FinishedBikeTrip implements DataSerializable, Comparable<FinishedBikeTrip> {

    private int endStationId;
    private int durationInMinutes;
    private LocalDateTime startDate;
    private String endStationName = ""; // Este nombre se setea en el collator

    /* Para Hazelcast */
    public FinishedBikeTrip() {
    }

    public FinishedBikeTrip(int endStationId, int durationInMinutes, LocalDateTime startDate) {
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

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FinishedBikeTrip that = (FinishedBikeTrip) o;

        if (endStationId != that.endStationId) return false;
        if (durationInMinutes != that.durationInMinutes) return false;
        return startDate.equals(that.startDate);
    }

    @Override
    public int hashCode() {
        int result = endStationId;
        result = 31 * result + durationInMinutes;
        result = 31 * result + startDate.hashCode();
        result = 31 * result + (endStationName != null ? endStationName.hashCode() : 0);
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
        out.writeInt(durationInMinutes);
        out.writeLong(startDate.toInstant(ZoneOffset.UTC).toEpochMilli());

    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        endStationId = in.readInt();
        durationInMinutes = in.readInt();
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

    // TODO: Si este no es unico formato de csv, hacerlo del lado del cliente
    public StringBuilder toCSV(boolean lastSemicolon, boolean lineBreak) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        StringBuilder sb = new StringBuilder()
            .append(endStationName).append(';')
            .append(startDate.format(formatter)).append(';')
            .append(durationInMinutes);

        if(lastSemicolon)
            sb.append(';');
        if (lineBreak)
            sb.append('\n');
        return sb;
    }

    public boolean isLongerThan(FinishedBikeTrip other) {
        return this.compareTo(other) > 0;
    }
}
