package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.Pit;

import java.util.ArrayList;
import java.util.List;

public class RaceTrack implements Track {

    private final Pit pit;
    private final List<Car> finishedCars;
    private int finishedCarsCount;

    public RaceTrack(int teamCount) {
        this.pit = new Pit(teamCount);
        this.finishedCars = new ArrayList<>();
        this.finishedCarsCount = 0;
    }

    @Override
    public void enterPit(Car car) {
        if (car.getNPitStops() == 0) {
            finishedCars.add(car);
            ++finishedCarsCount;
            System.out.println("Car " + car.getCarId() + " finished.");
        } else {
            pit.submitCar(car);
        }
    }

    @Override
    public int getNumberOfFinishedCars() {
        return finishedCarsCount;
    }

    @Override
    public List<Integer> getFinishedCarsIds() {
        return finishedCars.stream()
                .map(Car::getCarId)
                .toList();
    }

    @Override
    public Pit getPit() {
        return pit;
    }
}
