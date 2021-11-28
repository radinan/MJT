package bg.sofia.uni.fmi.mjt.twitch.content;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

public class Metadata {

    private final String title;
    private final Category category;
    private final User streamer;

    public Metadata(String title, Category category, User streamer) {
        this.title = title;
        this.category = category;
        this.streamer = streamer;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public User getStreamer() {
        return streamer;
    }

}
