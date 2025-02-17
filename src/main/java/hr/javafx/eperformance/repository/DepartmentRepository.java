package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.connection.DatabaseConnection;
import hr.javafx.eperformance.exception.DepartmentAlreadyExistsException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.Employee;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class DepartmentRepository extends AbstractRepository<Department> {

    @Override
    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connectToDatabase();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM department");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");

                Set<Employee> employees = findEmployeesByDepartment(id, connection);
                Department department = new Department(id, name, description, employees);

                departments.add(department);
            }
        } catch (IOException | SQLException e) {
            LoggerUtil.logError(e.getMessage());
        }

        return departments;
    }

    @Override
    public void save(Department entity) {

        if (findByName(entity.getName()).isPresent()) {
            String message = "Odjel s imenom " + entity.getName() + " već postoji.";
            LoggerUtil.logError(message);
            throw new DepartmentAlreadyExistsException(message);
        }

        String sql = "INSERT INTO department (name, description) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());

            preparedStatement.executeUpdate();

        } catch (IOException | SQLException e) {
            LoggerUtil.logError(e.getMessage());
        }

    }

    @Override
    public void delete(Department entity) {
        String sql = "DELETE FROM department WHERE id = ?";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();

        } catch (IOException | SQLException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    @Override
    public void update(Department entity) {
        String sql = "UPDATE department SET description = ? WHERE name = ?";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getDescription());
            preparedStatement.setString(2, entity.getName());

            preparedStatement.executeUpdate();
        } catch (IOException | SQLException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    private Set<Employee> findEmployeesByDepartment(Long id, Connection connection) {
        Set<Employee> employees = new HashSet<>();

        String sql = "SELECT * FROM employee WHERE department_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String jobTitle = resultSet.getString("job_title");
                BigDecimal salary = resultSet.getBigDecimal("salary");

                Employee employee = new Employee.Builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .jobTitle(jobTitle)
                        .salary(salary)
                        .build();
                employees.add(employee);
            }
        } catch (SQLException e) {
            LoggerUtil.logError(e.getMessage());
        }

        return employees;
    }

    public Optional<Department> findByName(String name) {
        return findAll().stream()
                .filter(department -> department.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<Department> findById(Long id) {
        return findAll().stream()
                .filter(department -> department.getId().equals(id))
                .findFirst();
    }
}
