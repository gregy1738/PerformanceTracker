package hr.javafx.eperformance.helper;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.model.User;
import hr.javafx.eperformance.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Scanner;

/**
 * Class used for initial seeding of the database with an admin user.
 */

public class UserDataInitialSeed {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String password = scanner.nextLine();
        User adminUser = new User("adminUser@admin.com", BCrypt.hashpw(password, BCrypt.gensalt()), EmployeeType.DIRECTOR);

        UserRepository userRepository = new UserRepository();
        userRepository.save(adminUser);

    }

}
