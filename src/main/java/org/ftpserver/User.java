package org.ftpserver;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String password;
    private List<User> users = new ArrayList<User >();


    public User(String name, String passwrd) {
        userName = name;
        password = passwrd;
    }

    private void add(User user) {
        users.add(user);
    }


}
