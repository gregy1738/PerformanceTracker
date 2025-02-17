package hr.javafx.eperformance.helper;

import hr.javafx.eperformance.model.User;

public class SessionManager {

    private SessionManager() {}

    private static User loggedInUser;

    public static void setLoggedInUser(User user){
        loggedInUser = user;
    }

    public static User getLoggedInUser(){
        return loggedInUser;
    }

}
