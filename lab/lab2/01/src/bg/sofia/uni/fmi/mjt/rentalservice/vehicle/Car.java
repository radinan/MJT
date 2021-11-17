package bg.sofia.uni.fmi.mjt.rentalservice.vehicle;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;

import java.time.LocalDateTime;

public class Car implements Vehicle {

    final String id;
    Location location;
    LocalDateTime until;

    public Car (String id, Location location) {
        this.id = id;
        this.location = location;
        this.until = LocalDateTime.now();
    }

    @Override
    public double getPricePerMinute() {
        return 0.50;
    }

    @Override
    public String getType() {
        return "CAR";
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
        return until.isBefore(LocalDateTime.now()) ? until : LocalDateTime.now();
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
