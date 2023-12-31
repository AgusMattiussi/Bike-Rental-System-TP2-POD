package ar.edu.itba.pod.tp2.model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.Objects;

public class AffluenceInfo implements DataSerializable, Comparable<AffluenceInfo> {
    private int positiveDays;
    private int negativeDays;
    private int neutralDays;
    private String stationName = "";

    public AffluenceInfo() {
    }

    public AffluenceInfo(int positiveDays, int negativeDays, int neutralDays) {
        this.positiveDays = positiveDays;
        this.negativeDays = negativeDays;
        this.neutralDays = neutralDays;
    }

    public int getPositiveDays() {
        return positiveDays;
    }

    public int getNegativeDays() {
        return negativeDays;
    }

    public int getNeutralDays() {
        return neutralDays;
    }

    public void addPositiveDays(int count) {
        positiveDays += count;
    }

    public void addNegativeDays(int count) {
        negativeDays += count;
    }

    public void addNeutralDays(int count) {
        neutralDays += count;
    }

    public String getStationName() {
        return stationName;
    }

    public void normalize(int days){
        int sum = positiveDays + negativeDays + neutralDays;
        if(sum != days){
            int diff = days - sum;
            addNeutralDays(diff);
        }
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffluenceInfo that = (AffluenceInfo) o;
        return positiveDays == that.positiveDays && negativeDays == that.negativeDays && neutralDays == that.neutralDays && Objects.equals(stationName, that.stationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positiveDays, negativeDays, neutralDays, stationName);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AffluenceInfo{");
        sb.append("positiveDays=").append(positiveDays);
        sb.append(", neutralDays=").append(neutralDays);
        sb.append(", negativeDays=").append(negativeDays);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeInt(positiveDays);
        objectDataOutput.writeInt(neutralDays);
        objectDataOutput.writeInt(negativeDays);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        positiveDays = objectDataInput.readInt();
        neutralDays = objectDataInput.readInt();
        negativeDays = objectDataInput.readInt();
    }

    @Override
    public int compareTo(AffluenceInfo o) {
        int aux = Integer.compare(this.positiveDays, o.positiveDays);
        if(aux != 0) return aux;

        aux = Integer.compare(this.negativeDays, o.negativeDays);
        if(aux != 0) return aux;

        aux = Integer.compare(this.neutralDays, o.neutralDays);
        if(aux != 0) return aux;

        return this.stationName.compareTo(o.stationName);
    }
}
