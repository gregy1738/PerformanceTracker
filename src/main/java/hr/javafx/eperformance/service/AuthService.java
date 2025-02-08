package hr.javafx.eperformance.service;

import hr.javafx.eperformance.exception.InvalidCredentialsException;
import hr.javafx.eperformance.model.User;
import hr.javafx.eperformance.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Service class for user authentication
 */

public class AuthService implements IAuthService {

    public static final Logger logger = LoggerFactory.getLogger(AuthService.class);
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
            logger.error("Pokušaj prijave korisnika koji ne postoji");
            throw new InvalidCredentialsException("Neuspješna prijava");
        }

        if(!BCrypt.checkpw(password, user.get().getHashedPassword())){
            logger.error("Pokušaj prijave s krivom lozinkom za korisnika: {} ", email);
            throw new InvalidCredentialsException("Neuspješna prijava");
        }

        return true;
    }
}
