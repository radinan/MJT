package bg.sofia.uni.fmi.mjt.race.track;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private final int id;
    private AtomicInteger leftPitStops;
    private final Track track;

    private static final Random RANDOM = new Random();

    public Car(int id, int nPitStops, Track track) {
        this.id = id;
        this.leftPitStops = new AtomicInteger(nPitStops);
        this.track = track;

        System.out.println("Car" + id + " created.");
    }

    @Override
    public void run() {
        while (leftPitStops.get() >= 0 && !track.getPit().isPitClosed()) {
            try {
                System.out.println("Car " + id + " = lap" + " (" + leftPitStops + ")");
                Thread.sleep((long) (RANDOM.nextDouble() * 1000));
                track.enterPit(this);
                leftPitStops.decrementAndGet();
            } catch (InterruptedException e) {
                System.out.println("Gyrmim bby");
                e.printStackTrace();
            }
        }
    }

    public int getCarId() {
        return id;
    }

    public int getNPitStops() {
        return leftPitStops.get();
    }

    public Track getTrack() {
        return track;
    }

}