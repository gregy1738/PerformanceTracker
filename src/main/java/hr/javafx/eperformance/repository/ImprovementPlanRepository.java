package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.connection.DatabaseConnection;
import hr.javafx.eperformance.exception.DatabaseConnectionException;
import hr.javafx.eperformance.exception.EmptyResultSetException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.model.ImprovementPlan;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImprovementPlanRepository extends AbstractRepository<ImprovementPlan> {
    @Override
    public List<ImprovementPlan> findAll() {
        List<ImprovementPlan> improvementPlans = new ArrayList<>();

        try(Connection connection = DatabaseConnection.connectToDatabase();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM improvement_plan")) {

            boolean hasResult = false;

            while(resultSet.next()) {
                hasResult = true;
                Long id = resultSet.getLong("id");
                Long employeeId = resultSet.getLong("employee_id");
                String description = resultSet.getString("description");
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                LocalDate endDate = resultSet.getDate("end_date").toLocalDate();

                Employee employee = new EmployeeRepository().findById(employeeId).orElse(null);

                ImprovementPlan improvementPlan = new ImprovementPlan(id, employee, description, null, startDate, endDate);
                improvementPlans.add(improvementPlan);
            }

            if(!hasResult) {
                throw new EmptyResultSetException("Nema zapisa u tablici plan poboljsanja.");
            }

        } catch (EmptyResultSetException | SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
        return improvementPlans;
    }

    @Override
    public void save(ImprovementPlan entity) {
        String sql = "INSERT INTO improvement_plan (employee_id, description, start_date, end_date) VALUES (?, ?, ?, ?)";

        try(Connection connection = DatabaseConnection.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, entity.getEmployee().getId());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setDate(3, Date.valueOf(entity.getStartDate()));
            preparedStatement.setDate(4, Date.valueOf(entity.getEndDate()));

            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    @Override
    public void delete(ImprovementPlan entity) {
        String sql = "DELETE FROM improvement_plan WHERE id = ?";

        try(Connection connection = DatabaseConnection.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    @Override
    public void update(ImprovementPlan entity) {
        String sql = "UPDATE improvement_plan SET start_date = ?, end_date = ? WHERE id = ?";

        try(Connection connection = DatabaseConnection.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDate(1, Date.valueOf(entity.getStartDate()));
            preparedStatement.setDate(2, Date.valueOf(entity.getEndDate()));
            preparedStatement.setLong(3, entity.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    public Optional<ImprovementPlan> findById(Long id){
        return  findAll().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }
}
