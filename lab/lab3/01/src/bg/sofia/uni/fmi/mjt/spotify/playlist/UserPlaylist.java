package bg.sofia.uni.fmi.mjt.spotify.playlist;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.playable.Playable;

public class UserPlaylist implements Playlist{

    private String name;
    private final int capacity = 20;
    private final Playable[] content;
    private int occupiedSlots;

    public UserPlaylist(String name) {
        this.name = name;
        content = new Playable[capacity];
        occupiedSlots = 0;
    }

    @Override
    public void add(Playable playable) throws PlaylistCapacityExceededException {
        if (occupiedSlots == capacity) {
            throw new PlaylistCapacityExceededException();
        }

        content[occupiedSlots] = playable;
        ++occupiedSlots;
    }

    @Override
    public String getName() {
        return name;
    }

}
