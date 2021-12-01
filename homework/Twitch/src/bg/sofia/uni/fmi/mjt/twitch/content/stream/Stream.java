package bg.sofia.uni.fmi.mjt.twitch.content.stream;

import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.time.Duration;
import java.time.LocalTime;

public interface Stream extends Content {
    Metadata getMetadata();

    void startWatching(User user);

    void stopWatching(User user);

    int getNumberOfViews();

    void endStream();
}
