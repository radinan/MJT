package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalTime;

public class Stream implements Content {

    private final Metadata metadata;
    private final LocalTime start;
    private LocalTime end;
    private Duration duration;
    //set watchers?
    private int views;

    public Stream(Metadata metadata) {
        this.metadata = metadata;
        this.start = LocalTime.now();
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        return end == null ?
                Duration.between(start, LocalTime.now()) :
                duration;
    }

    @Override
    public void startWatching(User user) {
        //add
        ++views;
    }

    @Override
    public void stopWatching(User user) {
        //remove
    }

    @Override
    public int getNumberOfViews() {
        return views;
    }

    public void endStream() {
        end = LocalTime.now();
        duration = Duration.between(start, end);
    }
}
