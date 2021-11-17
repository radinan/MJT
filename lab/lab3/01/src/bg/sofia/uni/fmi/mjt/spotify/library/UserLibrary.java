package bg.sofia.uni.fmi.mjt.spotify.library;

import bg.sofia.uni.fmi.mjt.spotify.exceptions.EmptyLibraryException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.LibraryCapacityExceededException;
import bg.sofia.uni.fmi.mjt.spotify.exceptions.PlaylistNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.playlist.Playlist;
import bg.sofia.uni.fmi.mjt.spotify.playlist.UserPlaylist;

public class UserLibrary implements Library{

    private final int capacity = 21;
    private final Playlist[] playlists;
    private int occupiedSpace;
    private final Playlist likedContent;

    public UserLibrary() {
        this.playlists = new Playlist[capacity];
        occupiedSpace = 0;
        this.likedContent = new UserPlaylist("Liked Content");
    }

    @Override
    public void add(Playlist playlist) throws LibraryCapacityExceededException {
        if (occupiedSpace == capacity) {
            throw new LibraryCapacityExceededException();
        }

        playlists[occupiedSpace] = playlist;
        ++occupiedSpace;
    }

    @Override
    public void remove(String name) throws EmptyLibraryException, PlaylistNotFoundException {
        if (likedContent != null && likedContent.getName().equals(name)) {
            throw new IllegalArgumentException();
        }

        if (occupiedSpace == 0) {
            throw new EmptyLibraryException();
        }

        boolean isFound = false;

        for (int i = 0; i < occupiedSpace; ++i) {
            if (playlists[i].getName().equals(name)) {
                isFound = true;

                for (int j = i; j < occupiedSpace; ++j) {
                    if (j == occupiedSpace - 1) {
                        playlists[j] = null;
                    } else {
                        playlists[j] = playlists[j+1];
                    }
                }
            }
        }

        if (!isFound) {
            throw new PlaylistNotFoundException();
        }
    }

    @Override
    public Playlist getLiked() {
        return likedContent;
    }
}
