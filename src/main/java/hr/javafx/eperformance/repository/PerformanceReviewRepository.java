package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.connection.DatabaseConnection;
import hr.javafx.eperformance.exception.DatabaseConnectionException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PerformanceReviewRepository extends AbstractRepository<PerformanceReview> {
    @Override
    public List<PerformanceReview> findAll() {
        List<PerformanceReview> performanceReviews = new ArrayList<>();

        try(Connection connection = DatabaseConnection.connectToDatabase();
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT * FROM performance_review");

            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long employeeId = resultSet.getLong("employee_id");
                String review = resultSet.getString("review");
                String reviewer = resultSet.getString("reviewer");
                String comment = resultSet.getString("comment");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                double rating = resultSet.getDouble("performance_rating");
                Long improvementPlanId = resultSet.getLong("improvement_plan_id");

                Employee employee = new EmployeeRepository().findById(employeeId).orElse(null);
                ImprovementPlan improvementPlan = new ImprovementPlanRepository().findById(improvementPlanId).orElse(null);

                PerformanceReview performanceReview = new PerformanceReview.Builder()
                        .id(id)
                        .employee(employee)
                        .review(review)
                        .reviewer(reviewer)
                        .comment(comment)
                        .date(date)
                        .improvementPlan(improvementPlan)
                        .performanceMetrics(new PerformanceMetrics(rating))
                        .build();

                performanceReviews.add(performanceReview);
            }

        } catch(SQLException | DatabaseConnectionException e){
            LoggerUtil.logError(e.getMessage());
        }
        return performanceReviews;
    }

    @Override
    public void save(PerformanceReview entity) {
        String sql = "INSERT INTO performance_review (employee_id, review, reviewer, comment, date, performance_rating, improvement_plan_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConnection.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, entity.getEmployee().getId());
            preparedStatement.setString(2, entity.getReview());
            preparedStatement.setString(3, entity.getReviewer());
            preparedStatement.setString(4, entity.getComment());
            preparedStatement.setDate(5, Date.valueOf(entity.getDate()));
            preparedStatement.setDouble(6, entity.getPerformanceMetrics().getPerformanceRating());
            preparedStatement.setLong(7, entity.getImprovementPlan().getId());

            preparedStatement.executeUpdate();

        } catch(SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    @Override
    public void delete(PerformanceReview entity) {
        String sql = "DELETE FROM performance_review WHERE id = ?";

        try(Connection connection = DatabaseConnection.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();

        } catch(SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    @Override
    public void update(PerformanceReview entity) {
        String sql = "UPDATE performance_review SET review = ?, comment = ?, performance_rating = ? WHERE id = ?";

        try(Connection connection = DatabaseConnection.connectToDatabase();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getReview());
            preparedStatement.setString(2, entity.getComment());
            preparedStatement.setDouble(3, entity.getPerformanceMetrics().getPerformanceRating());
            preparedStatement.setLong(4, entity.getId());

            preparedStatement.executeUpdate();

        } catch(SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError(e.getMessage());
        }
    }

    public Optional<PerformanceReview> findById(Long id){
        return findAll().stream()
                .filter(performanceReview -> performanceReview.getId().equals(id))
                .findFirst();
    }
}
