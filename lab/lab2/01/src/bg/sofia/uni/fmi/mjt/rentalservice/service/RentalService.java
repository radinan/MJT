package bg.sofia.uni.fmi.mjt.rentalservice.service;

import bg.sofia.uni.fmi.mjt.rentalservice.location.Location;
import bg.sofia.uni.fmi.mjt.rentalservice.vehicle.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

// fix findNearestAvailableVehicleInRadius() !!!!!
public class RentalService implements RentalServiceAPI {
    private Vehicle[] vehicles;
    private Vehicle[] booked;

    public RentalService(Vehicle[] vehicles) {
        this.vehicles = Arrays.copyOf(vehicles, vehicles.length);
        this.booked = new Vehicle[vehicles.length];
    }

    @Override
    public double rentUntil(Vehicle vehicle, LocalDateTime until) {
        if (vehicle == null || !containsVehicle(vehicle) || isBooked(vehicle)) {
            return -1;
        }

        long difference = Duration.between(LocalDateTime.now(), until).toMinutes();

        return vehicle.getPricePerMinute() * difference;
    }

    @Override
    public Vehicle findNearestAvailableVehicleInRadius(String type, Location location, double maxDistance) {
        return null; // to be fixed
    }

    private boolean containsVehicle(Vehicle vehicle) {
        for (Vehicle v : vehicles) {
            if (v.equals(vehicle)) return true;
        }

        return false;
    }

    private boolean isBooked(Vehicle vehicle) {
        for (Vehicle v : booked) {
            if (v.equals(vehicle)) return true;
        }

        return false;
    }

/*    private double[] getDistances(Location location, Vehicle[] vehicles) {
        double[] distances = new double[vehicles.length];
        int i = 0;

        for (Vehicle v : vehicles) {
            double xDif = location.getX() - v.getLocation().getX();
            double yDif = location.getY() - v.getLocation().getY();
            double distance = Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));

            distances[i] = distance;
            ++i;
        }

        return distances;
    }

    private int findMinDistance(double[] distances, double maxDistance) {
        double minValue = maxDistance + 1;
        int minValueIndex = -1;

        for (int i = 0; i < distances.length; ++i) {
            if (distances[i] < minValue) {
                minValue = distances[i];
                minValueIndex = i;
            }
        }

        return minValueIndex;
    }*/

}
