package model;

public class Station {

    private final long pk;
    private final String name;
    private final double latitude;
    private final double longitude;

    public Station(long pk, String name, double latitude, double longitude) {
        this.pk = pk;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getPk() {
        return pk;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        if (pk != station.pk) return false;
        if (Double.compare(latitude, station.latitude) != 0) return false;
        if (Double.compare(longitude, station.longitude) != 0) return false;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (pk ^ (pk >>> 32));
        result = 31 * result + name.hashCode();
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Station{");
        sb.append("pk=").append(pk);
        sb.append(", name='").append(name).append('\'');
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append('}');
        return sb.toString();
    }
}
