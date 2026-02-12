package org.ftpserver.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String password;
    private static List<User> users = new ArrayList<User >();


    public User(String name, String passwrd) {
        userName = name;
        password = passwrd;
    }


    public static void initUser() {
        users.add(new User("admin", "123"));
        users.add(new User("anonymous", "blabla"));
        users.add(new User("assane.kane", "password"));
    }

    public static boolean isValid(String name, String password) {
        if (name.equals("anonymous") ) {
            return true;
        }

        for (User u : users) {
            if (name.equals(u.userName) && password.equals(u.password)) return true;
        }
        return  false;
    }
}
