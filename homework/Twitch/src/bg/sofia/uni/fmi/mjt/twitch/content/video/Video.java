package bg.sofia.uni.fmi.mjt.twitch.content.video;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;

public interface Video extends Content {
    Metadata getMetadata();

    Duration getDuration();

    void startWatching(User user);

    void stopWatching(User user);

    int getNumberOfViews();
}
