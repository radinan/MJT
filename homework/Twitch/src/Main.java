import bg.sofia.uni.fmi.mjt.twitch.StreamingPlatform;
import bg.sofia.uni.fmi.mjt.twitch.Twitch;
import bg.sofia.uni.fmi.mjt.twitch.content.Category;
import bg.sofia.uni.fmi.mjt.twitch.content.Content;
import bg.sofia.uni.fmi.mjt.twitch.content.stream.Stream;
import bg.sofia.uni.fmi.mjt.twitch.user.*;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserService;
import bg.sofia.uni.fmi.mjt.twitch.user.service.UserServiceTwitch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, User> map = new HashMap<>();
        User user1 = new UserTwitch("user1");
        User user2 = new UserTwitch("user2");
        User user3 = new UserTwitch("user3");
        User user4 = new UserTwitch("user4");
        map.put("user1", user1);
        map.put("user2", user2);
        map.put("user3", user3);
        map.put("user4", user4);


        UserService userService = new UserServiceTwitch(map);
        StreamingPlatform twitch = new Twitch(userService);

//        user1.setStatus(UserStatus.STREAMING);
        try {
            Content stream = twitch.startStream("user1", "Stream1", Category.ESPORTS);
            twitch.watch("user2", stream);
            twitch.watch("user3", stream);

            Content video = twitch.endStream("user1", (Stream)stream);
            twitch.watch("user3", video);

            Content stream2 = twitch.startStream("user2", "Stream1", Category.IRL);
            twitch.watch("user3", stream);


            twitch.getMostWatchedStreamer();
            twitch.getMostWatchedContent();
            twitch.getMostWatchedContentFrom("user1");



            User mostWatchedStreamer = twitch.getMostWatchedStreamer(); //user1
            Content mostWatchedContent = twitch.getMostWatchedContent(); //stream
//            Content mostWatchedContentFrom = twitch.getMostWatchedContentFrom("user1");
            List<Category> test = twitch.getMostWatchedCategoriesBy("user3");


            System.out.println("Success");
        } catch (UserNotFoundException e) {
            System.out.println("UserNotFound");
        } catch (UserStreamingException e) {
            System.out.println("UserStreaming");
        }

    }
}
