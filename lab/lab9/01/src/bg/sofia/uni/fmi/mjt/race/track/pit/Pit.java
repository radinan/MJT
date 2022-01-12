package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Pit {
    private final int numberOfPitTeams;
    private final List<PitTeam> pitTeams;
    private Queue<Car> waitingCars;
    private final AtomicInteger pitStopsCount;

    private boolean hasRaceFinished;

    public Pit(int nPitTeams) {
        this.numberOfPitTeams = nPitTeams;
        this.pitTeams = new ArrayList<>();
        this.waitingCars = new ConcurrentLinkedQueue<>();
        this.pitStopsCount = new AtomicInteger();
        this.hasRaceFinished = false;
        initializePitTeams();
    }

    public void submitCar(Car car) {
        if (hasRaceFinished) {
            return;
        }
        pitStopsCount.incrementAndGet();

        synchronized (this) {
            System.out.println("Car " + car.getCarId() + " entered the pit.");
            waitingCars.add(car);
            this.notifyAll();
        }
    }

    public synchronized Car getCar() {
        while (waitingCars.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return waitingCars.isEmpty() ? null : waitingCars.poll();
    }

    public int getPitStopsCount() {
        return pitStopsCount.get();
    }

    public List<PitTeam> getPitTeams() {
        return pitTeams;
    }

    public synchronized void finishRace() {
        this.hasRaceFinished = true;
        this.notifyAll();
    }

    public boolean isPitClosed() {
        return hasRaceFinished;
    }

    private void initializePitTeams() {
        for (int i = 0; i < numberOfPitTeams; ++i) {
            pitTeams.add(i, new PitTeam(i, this));
            pitTeams.get(i).setName("PitTeam " + i);
            pitTeams.get(i).start();
        }
    }
}