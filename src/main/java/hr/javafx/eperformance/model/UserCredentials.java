package hr.javafx.eperformance.model;

import hr.javafx.eperformance.enums.EmployeeType;

/**
 * Represents user credentials class.
 * This class is used for storing user credentials.
 */

public class UserCredentials {

    private String email;
    private String hashedPassword;
    private EmployeeType role;


    /**
     * Constructs a new UserCredentials with the specified email, hashed password and role.
     *
     * @param email          the email
     * @param hashedPassword the hashed password
     * @param role           the role
     */

    public UserCredentials(String email, String hashedPassword, EmployeeType role) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public EmployeeType getRole() {
        return role;
    }

    public void setRole(EmployeeType role) {
        this.role = role;
    }
}
