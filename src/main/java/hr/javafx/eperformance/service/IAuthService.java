package hr.javafx.eperformance.service;

/**
 * Service for authentication.
 */

public interface IAuthService {

    Boolean login(String email, String password);

    void register(String firstName, String lastName, String password);

}
