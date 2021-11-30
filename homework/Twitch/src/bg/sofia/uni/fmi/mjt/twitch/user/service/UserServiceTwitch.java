package bg.sofia.uni.fmi.mjt.twitch.user.service;

import bg.sofia.uni.fmi.mjt.twitch.user.User;

import java.util.Map;

public class UserServiceTwitch implements UserService {
    Map<String, User> users;

    public UserServiceTwitch() {

    }

    @Override
    public Map<String, User> getUsers() {
        return null;
    }
}
