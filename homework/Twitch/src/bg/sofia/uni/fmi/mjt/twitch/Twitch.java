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

import java.util.*;

public class Twitch implements StreamingPlatform {

    UserService userService;
    Map<String, List<Content>> usernameToContent;
    Map<String, Map<Category, Integer>> usernameToHistory;

    public Twitch(UserService userService) {
        this.userService = userService;
        this.usernameToContent = new HashMap<>();
        this.usernameToHistory = new HashMap<>();
//        this.contentCountByCategory = new Integer[contentsIndexes.length];
    }

    /**
     * Starts a new {@link Stream} and returns a reference to it.
     *
     * @param username the username of the streamer
     * @param title    the title of the stream
     * @param category the {@link Category} of the stream
     * @return the started {@link Stream}
     * @throws IllegalArgumentException if any of the parameters are null or if strings are empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     * @throws UserStreamingException   if a user with this username is currently
     *                                  streaming
     */
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

    /**
     * Ends an existing {@link Stream} and returns a new {@link Video} which was
     * made of it.
     *
     * @param username the username of the streamer
     * @param stream   the stream to end
     * @return the created {@link Video}
     * @throws IllegalArgumentException if any of the parameters are null or if {@code username} is empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     * @throws UserStreamingException   if a user with this username is currently not
     *                                  streaming
     */
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

    /**
     * Watches a content.
     *
     * @param username the username of the watcher
     * @param content  the content to watch
     * @throws IllegalArgumentException if any of the parameters are null or if {@code username} is empty
     * @throws UserNotFoundException    if a user with this username is not found in
     *                                  the service
     * @throws UserStreamingException   if the user with the specified username is
     *                                  currently streaming
     */
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
    }

    /**
     * Returns the {@link User} whose {@link Content}s have the most views combined in the
     * service or null if there is no such user.
     *
     * @return the {@link User} whose {@link Content}s have the most views combined in the
     * service or null if there is no such user
     */
    @Override
    public User getMostWatchedStreamer() {
        long maxViews = -1;
        String maxUsername = null;

        for (String username : userService.getUsers().keySet()) {
            long currentViews = getCombinedViews(username);

            if (currentViews > maxViews) {
                maxUsername = username;
                maxViews = currentViews;
            }
        }

        return userService.getUsers().get(maxUsername);
    }

    /**
     * Returns the {@link Content} which has the most views in the service
     * or null if there is not such content.
     *
     * @return the {@link Content} which has the most views in the service
     * or null if there is not such content
     */
    @Override
    public Content getMostWatchedContent() {
        Content maxContent = null;
        int maxViews = -1;

        for (String username : userService.getUsers().keySet()) {
            try {
                Content currentContent = getMostWatchedContentFrom(username);

                if (currentContent != null && currentContent.getNumberOfViews() > maxViews) {
                    maxContent = currentContent;
                    maxViews = currentContent.getNumberOfViews();
                }
            } catch (UserNotFoundException ignored) {}
        }

        return maxContent;
    }

    /**
     * Returns the {@link Content} from user with name username which has the most
     * views in the service or null if there is not such content.
     *
     * @return the {@link Content} from user with name username which has the most
     *         views in the service or null if there is not such content
     * @throws IllegalArgumentException if {@code username} is null or empty
     * @throws UserNotFoundException if a user with this username is not found in
     *                               the service
     */
    @Override
    public Content getMostWatchedContentFrom(String username) throws UserNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException();
        }

        User user = userService.getUsers().get(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<Content> content = usernameToContent.get(username);

        if (content == null || content.isEmpty()) {
            return null;
        }

        content.sort(Comparator.comparing(Content::getNumberOfViews).reversed());
        return content.get(0);
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        return null;
    }

    private long getCombinedViews(String username) {
        List<Content> contents = usernameToContent.get(username);
        long sumViews = 0;

        for (Content content : contents) {
            sumViews += content.getNumberOfViews();
        }

        return sumViews;
    }

    private void addToHistory(String username, Content content) {
    }
}
