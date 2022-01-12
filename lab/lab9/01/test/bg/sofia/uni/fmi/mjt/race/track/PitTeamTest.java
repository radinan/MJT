package bg.sofia.uni.fmi.mjt.race.track;

import bg.sofia.uni.fmi.mjt.race.track.pit.PitTeam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PitTeamTest {

    @Test
    void getPitStoppedCarsCount() {
        RaceTrack track = new RaceTrack(2);

        for (int i = 0; i < 3; ++i) {
            Car car = new Car(i, 1, track);
            Thread thread = new Thread(car);
            thread.start();
        }

        try {
            Thread.sleep(5000);
            System.out.println("CLOSING....");
            track.getPit().finishRace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int sumOfAllTeamsStops = 0;
        for (PitTeam p : track.getPit().getPitTeams()) {
            sumOfAllTeamsStops += p.getPitStoppedCars();
        }

        Assertions.assertEquals(sumOfAllTeamsStops, track.getPit().getPitStopsCount(), "Incorrect pit stops saving.");
    }
}