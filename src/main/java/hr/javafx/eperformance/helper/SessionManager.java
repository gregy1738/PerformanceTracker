package hr.javafx.eperformance.helper;

import hr.javafx.eperformance.model.User;

/**
 * Class that manages the session of the logged-in user.
 */

public class SessionManager {

    private SessionManager() {}

    private static User loggedInUser;

    /**
     * Method that sets the logged-in user.
     * @param user User that is logged in.
     */

    public static void setLoggedInUser(User user){
        loggedInUser = user;
    }

    /**
     * Method that returns the logged-in user.
     * @return User that is logged in.
     */

    public static User getLoggedInUser(){
        return loggedInUser;
    }

}
