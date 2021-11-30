package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

public class Video implements Content {

    private final Metadata metadata;
    private final Duration duration;
    //set? watchers
    private int views;

    public Video(Stream stream) {
        this.metadata = stream.getMetadata();
        this.duration = stream.getDuration();
        this.views = 0;
    }

    @Override
    public Metadata getMetadata() {
        return metadata;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void startWatching(User user) {
        //watchers.add(user)
        ++views;
    }

    @Override
    public void stopWatching(User user) {
        //watchers.remove(user)
    }

    @Override
    public int getNumberOfViews() {
        return views;
    }
}
