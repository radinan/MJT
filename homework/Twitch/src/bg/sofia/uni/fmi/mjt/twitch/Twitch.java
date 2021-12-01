package bg.sofia.uni.fmi.mjt.twitch;

import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.history.CategoryHistory;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.Metadata;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.StreamTwitch;
import bg.sofia.uni.fmi.mjt.twitch.content.video.Video;
import bg.sofia.uni.fmi.mjt.twitch.content.video.VideoTwitch;
import bg.sofia.uni.fmi.mjt.twitch.user.User;
import bg.sofia.uni.fmi.mjt.twitch.user.UserNotFoundException;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStatus;
import bg.sofia.uni.fmi.mjt.twitch.user.UserStreamingException;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;

import java.util.*;

public class Twitch implements StreamingPlatform {

    private UserService userService;
    private Map<String, List<Content>> usernameToContent;
    private Map<String, List<CategoryHistory>> usernameToHistory;

    public Twitch(UserService userService) {
        this.userService = userService;
        this.usernameToContent = new HashMap<>();
        this.usernameToHistory = new HashMap<>();
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

        Stream newStream = new StreamTwitch(new Metadata(title, category, user));
        addContent(username, newStream);

        return newStream;
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

        Video newVideo = new VideoTwitch(stream);
        addContent(username, newVideo);

        return newVideo;
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
        addToHistory(username, content.getMetadata().getCategory());
    }

    @Override
    public User getMostWatchedStreamer() {
        String maxUsername = "";
        long maxViews = -1;

        for (String username : userService.getUsers().keySet()) {
            long currentViews = getCombinedViews(username);

            if (currentViews > maxViews) {
                maxUsername = username;
                maxViews = currentViews;
            }
        }

        return userService.getUsers().get(maxUsername);
    }

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

        Comparator<Content> comparator = Comparator.comparing(Content::getNumberOfViews).reversed();
        content.sort(comparator);

        return content.get(0);
    }

    @Override
    public List<Category> getMostWatchedCategoriesBy(String username) throws UserNotFoundException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException();
        }

        User user = userService.getUsers().get(username);

        if (user == null) {
            throw new UserNotFoundException();
        }

        List<CategoryHistory> history = usernameToHistory.getOrDefault(username, new ArrayList<>());
        Comparator<CategoryHistory> comparator = Comparator.comparing(CategoryHistory::getViewsCount).reversed();
        history.sort(comparator);

        List<Category> mostWatchedCategories = new ArrayList<>();

        for (CategoryHistory categoryViewHistory : history) {
            mostWatchedCategories.add(categoryViewHistory.getCategory());
        }

        return mostWatchedCategories;
    }


    private void addContent(String username, Content content) {
        List<Content> contents = usernameToContent.getOrDefault(username, new ArrayList<>());
        contents.add(content);
        usernameToContent.put(username, contents);
    }

    private long getCombinedViews(String username) {
        List<Content> contents = usernameToContent.getOrDefault(username, new ArrayList<>());
        long sumViews = 0;

        for (Content content : contents) {
            sumViews += content.getNumberOfViews();
        }

        return sumViews;
    }

    private void addToHistory(String username, Category category) {
        List<CategoryHistory> history = usernameToHistory.getOrDefault(username, new ArrayList<>());

        for (CategoryHistory categoryHistory : history) {
            if (categoryHistory.getCategory().equals(category)) {
                categoryHistory.watch();
                return;
            }
        }

        CategoryHistory categoryHistory = new CategoryHistory(category);
        categoryHistory.watch();

        history.add(categoryHistory);

        usernameToHistory.put(username, history);
    }
}
