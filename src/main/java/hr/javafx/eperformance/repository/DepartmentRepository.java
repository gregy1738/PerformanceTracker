package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.connection.DatabaseConnection;
import hr.javafx.eperformance.exception.DepartmentAlreadyExistsException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.Employee;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

                Set<Employee> employees = findEmployeesByDepartment(name, connection);
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

        if (findByName(entity.getName()) != null) {
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
        String sql = "DELETE FROM department WHERE name = ?";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getName());
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

    private Set<Employee> findEmployeesByDepartment(String name, Connection connection) {
        Set<Employee> employees = new HashSet<>();

        String sql = "SELECT * FROM employee WHERE department_id = (SELECT id FROM department WHERE name = ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
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

    private Department findByName(String name) {
        return findAll().stream()
                .filter(department -> department.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
