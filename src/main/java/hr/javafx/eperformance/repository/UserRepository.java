package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.enums.EmployeeType;
import hr.javafx.eperformance.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for user data
 */

public class UserRepository {

    private static final String FILE_NAME = "dat/users.txt";

    /**
     * Method for reading all users from file
     * @return list of users
     */

    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> parts = List.of(line.split(":"));

                String email = parts.getFirst();
                String hashedPassword = parts.get(1);
                EmployeeType role = EmployeeType.valueOf(parts.get(2));

                User user = new User(email, hashedPassword, role);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Method for saving user to file
     * @param user user to save
     */

    public void save(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.newLine();
            writer.write(user.getEmail() + ":" + user.getHashedPassword() + ":" + user.getRole().name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for finding user by email
     * @param email user email
     * @return user if found, empty optional otherwise
     */

    public Optional<User> findUserByEmail(String email) {
        List<User> users = getAll();

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }
}
