package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.List;

public class Twitch implements StreamingPlatform {

    UserService userService;

    public Twitch(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Stream startStream(String username, String title, Category category) throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isEmpty() ||
                title == null || title.isEmpty() || category == null) {
            throw new IllegalArgumentException();
        }

        User user = userService.getUsers().get(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (user.getStatus().equals(UserStatus.STREAMING)) {
            throw new UserStreamingException();
        }

        user.setStatus(UserStatus.STREAMING);

        return new Stream(new Metadata(title, category, user));
    }

    @Override
    public Video endStream(String username, Stream stream) throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isEmpty() || stream == null) {
            throw new IllegalArgumentException();
        }

        User user = userService.getUsers().get(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getStatus().equals(UserStatus.STREAMING)) {
            throw new UserStreamingException();
        }

        stream.endStream();
        user.setStatus(UserStatus.OFFLINE);

        return new Video(stream);
    }
    
    @Override
    public void watch(String username, Content content) throws UserNotFoundException, UserStreamingException {
        if (username == null || username.isEmpty() || content == null) {
            throw new IllegalArgumentException();
        }

        User user = userService.getUsers().get(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (user.getStatus().equals(UserStatus.STREAMING)) {
            throw new UserStreamingException();
        }

        content.startWatching(user);
        //user.addToHistory
    }

    @Override
    public User getMostWatchedStreamer() {
        return null;
    }

    @Override
    public Content getMostWatchedContent() {
        return null;
    }

    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        return null;
    }
}
