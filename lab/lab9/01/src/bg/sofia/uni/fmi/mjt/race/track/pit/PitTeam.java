package bg.sofia.uni.fmi.mjt.race.track.pit;

import bg.sofia.uni.fmi.mjt.race.track.Car;

import java.util.concurrent.ThreadLocalRandom;

public class PitTeam extends Thread {
    private final int id;
    private final Pit pitStop;
    private int pitStoppedCarsCount;

    public PitTeam(int id, Pit pitStop) {
        this.id = id;
        this.pitStop = pitStop;
    }

    @Override
    public void run() {
        System.out.println("Team " + id + " started work.");

        Car car = null;

        while ((car = pitStop.getCar()) != null) {
            try {
                Thread.sleep((long) ThreadLocalRandom.current().nextDouble(200));
                ++pitStoppedCarsCount;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("PitTeam %s fixed %s cars%n", id, pitStoppedCarsCount);
    }

    public int getPitStoppedCars() {
        return pitStoppedCarsCount;
    }

}