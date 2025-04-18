package hr.javafx.eperformance.service;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.exception.InvalidCredentialsException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.helper.SessionManager;
import hr.javafx.eperformance.model.User;
import hr.javafx.eperformance.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

/**
 * Service class for user authentication
 */

public class AuthService implements IAuthService {

    private final UserRepository userRepository = new UserRepository();

    /**
     * Method for user login
     * @param email user email
     * @param password user password
     * @return true if login is successful
     */
    @Override
    public Boolean login(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);

        if(user.isEmpty()){
            LoggerUtil.logError("Pokušaj prijave s nepostojećim korisnikom: {} ", email);
            throw new InvalidCredentialsException("Neuspješna prijava");
        }

        if(!BCrypt.checkpw(password, user.get().getHashedPassword())){
            LoggerUtil.logError("Pokušaj prijave s krivom lozinkom za korisnika: {} ", email);
            throw new InvalidCredentialsException("Neuspješna prijava");
        }

        SessionManager.setLoggedInUser(user.get());
        LoggerUtil.logInfo("Korisnik {} se uspješno prijavio", email);
        return true;
    }

    @Override
    public void register(String firstName, String lastName, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@firma.com";
        User user = new User(email, hashedPassword, EmployeeType.REGULAR);
        userRepository.save(user);
        LoggerUtil.logInfo("Korisnik {} se uspješno registrirao", email);
    }
}
