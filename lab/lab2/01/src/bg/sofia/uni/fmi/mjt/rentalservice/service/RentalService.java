package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

public class RentalService implements RentalServiceAPI {
    private Vehicle[] vehicles;

    public RentalService(Vehicle[] vehicles) {
        this.vehicles = Arrays.copyOf(vehicles, vehicles.length);
    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {
        if (vehicle == null || until == null ||
                until.isBefore(vehicle.getEndOfReservationPeriod()) ||
                !containsVehicle(vehicle)) {
            return -1;
        }

        vehicle.setEndOfReservationPeriod(until);

        return vehicle.getPricePerMinute() * Duration.between(LocalDateTime.now(), until).toMinutes();
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        if (type == null || location == null || vehicles.length == 0 || maxDistance < 0) {
            return null;
        }

        Vehicle[] vehiclesWithType = getVehiclesWithType(type);

        if (vehiclesWithType.length == 0) {
            return null;
        }

        return getClosestVehicleInRadius(vehiclesWithType, maxDistance, location);
    }

    private boolean containsVehicle(Vehicle vehicle) {
        for (Vehicle v : vehicles) {
            if (!isBooked(v) && v.equals(vehicle)) {
                return true;
            }
        }

        return false;
    }

    private boolean isBooked(Vehicle vehicle) {
        return vehicle.getEndOfReservationPeriod().isAfter(LocalDateTime.now());
    }

    private Vehicle[] getVehiclesWithType(String type) {
        Vehicle[] filteredVehicles = new Vehicle[vehicles.length];
        int i = 0;

        for (Vehicle v : vehicles) {
            if (v.getType().equals(type)) {
                filteredVehicles[i] = v;
                ++i;
            }
        }

        return removeNulls(filteredVehicles);
    }

    private Vehicle getClosestVehicleInRadius(Vehicle[] vehicles, double maxDistance, Location location) {
        int i = 0;
        while(i < vehicles.length && vehicles[i] != null) {
            if (getDistance(location, vehicles[i]) > maxDistance) {
                vehicles[i] = null;
            }

            ++i;
        }

        Vehicle[] vehiclesWithoutNull = removeNulls(vehicles);
        if (vehiclesWithoutNull.length == 0) {
            return null;
        }
        
        Arrays.sort(vehiclesWithoutNull);

        return vehiclesWithoutNull[0];
    }


    private double getDistance(Location location, Vehicle vehicle) {
        double xDif = location.getX() - vehicle.getLocation().getX();
        double yDif = location.getY() - vehicle.getLocation().getY();
        return Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
    }

    private Vehicle[] removeNulls(Vehicle[] vehicles) {
        int i = 0;
        int j = 0;
        Vehicle[] newVehicles = new Vehicle[vehicles.length];

        while(i < vehicles.length) {
            if (vehicles[i] != null) {
                newVehicles[j] = vehicles[i];
                ++j;
            }
            ++i;
        }

        Vehicle[] finalVehicles = new Vehicle[i];
        for (int k = 0; k < i; ++k) {
            finalVehicles[k] = newVehicles[k];
        }

        return finalVehicles;
    }
}
