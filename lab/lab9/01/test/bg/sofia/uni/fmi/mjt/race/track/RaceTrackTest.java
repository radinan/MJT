package bg.sofia.uni.fmi.mjt.race.track;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RaceTrackTest {

    @Test
    void enterPit() {
        Track track = new RaceTrack(1);
        Car car = new Car(5, 0, track);
        track.enterPit(car);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(track.getFinishedCarsIds().get(0), 5, "Cars are not finishing correctly.");
    }

    @Test
    void getNumberOfFinishedCars() {
        Track track = new RaceTrack(1);

        for (int i = 0; i < 10; ++i) {
            Car car = new Car(i, 0, track);
            Thread thread = new Thread(car);
            thread.start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(10, track.getNumberOfFinishedCars(), "All cars should finish");
    }

    @Test
    void getNumberOfFinishedCarsFinishRace() {
        Track track = new RaceTrack(1);

        for (int i = 0; i < 3; ++i) {
            Car car = new Car(i, 3, track);
            Thread thread = new Thread(car);
            thread.start();
        }

        try {
            Thread.sleep(2000);
            System.out.println("CLOSING....");
            track.getPit().finishRace();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert (track.getNumberOfFinishedCars() < 3);
    }

    @Test
    void getFinishedCarsIds() {
        Track track = new RaceTrack(1);

        for (int i = 0; i < 10; ++i) {
            Car car = new Car(i, 4, track);
            Thread thread = new Thread(car);
            thread.start();
        }

        try {
            Thread.sleep(2000);
            System.out.println("CLOSING....");
            track.getPit().finishRace();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(track.getFinishedCarsIds().size(), track.getNumberOfFinishedCars(), "Methods don't return same data");
    }

    @Test
    void getPit() {
        Track track = new RaceTrack(1);

        Assertions.assertNotEquals(track.getPit(), null, "Pit is not initialized");
    }
}