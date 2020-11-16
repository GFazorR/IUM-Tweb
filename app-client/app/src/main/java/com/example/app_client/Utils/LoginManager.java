package com.example.app_client.Utils;

import com.example.app_client.Model.User;
import com.orhanobut.hawk.Hawk;

public class LoginManager {
    public static User getUser() {
        return Hawk.get("user");
    }

    public static void setUser(User u) {
        Hawk.put("user", u);
    }

    public static boolean isLoggedIn() {
        return getToken() != null;
    }

    public static String getToken() {
        User user = getUser();
        if (user != null) return user.getToken();
        return null;
    }

    public static void clear() {
        Hawk.deleteAll();
    }
}
