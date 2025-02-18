package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.connection.DatabaseConnection;
import hr.javafx.eperformance.exception.DatabaseConnectionException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.model.Department;
import hr.javafx.eperformance.model.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepository extends AbstractRepository<Employee> {

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connectToDatabase();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM employee");

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String job = resultSet.getString("job_title");
                BigDecimal salary = resultSet.getBigDecimal("salary");
                Long departmentId = resultSet.getLong("department_id");

                Department department = new DepartmentRepository().findById(departmentId)
                        .orElse(null);

                Employee employee = new Employee.Builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName(lastName)
                        .jobTitle(job)
                        .salary(salary)
                        .department(department)
                        .build();

                employees.add(employee);

            }

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
        return employees;
    }

    @Override
    public void save(Employee entity) {

        String sql = "INSERT INTO employee (first_name, last_name, job_title, salary, department_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getJobTitle());
            preparedStatement.setBigDecimal(4, entity.getSalary());
            preparedStatement.setLong(5, entity.getDepartment().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }

    }

    @Override
    public void delete(Employee entity) {

        String sql = "DELETE FROM employee WHERE id = ?";

        try(Connection connection = DatabaseConnection.connectToDatabase();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, entity.getId());
            statement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }

    }

    @Override
    public void update(Employee entity) {

        String sql = "UPDATE employee SET first_name = ?, last_name = ?, job_title = ?, salary = ?, department_id = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getJobTitle());
            statement.setBigDecimal(4, entity.getSalary());
            statement.setLong(5, entity.getDepartment().getId());
            statement.setLong(6, entity.getId());

            statement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    public Optional<Employee> findById(Long id){
        return findAll().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
}
