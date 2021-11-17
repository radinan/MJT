package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;

public class Bicycle implements Vehicle {

    final String id;
    Location location;
    LocalDateTime until;

    public Bicycle (String id, Location location) {
        this.id = id;
        this.location = location;
        this.until = LocalDateTime.now();
    }

    @Override
    public double getPricePerMinute() {
        return 0.20;
    }

    @Override
    public String getType() {
        return "BICYCLE";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Location getLocation() {
        return location == null ? new Location(0, 0) : location;
    }

    @Override
    public LocalDateTime getEndOfReservationPeriod() {
        return this.until;
    }

    @Override
    public void setEndOfReservationPeriod(LocalDateTime until) {
        if (until != null && !until.isBefore(this.until)) {
            this.until = until;
        }
    }

    public boolean equals(Vehicle vehicle) {
//        if (vehicle == null) return false;
        return vehicle.getId().equals(this.id);
    }

}
