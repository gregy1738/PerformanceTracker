package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.connection.DatabaseConnection;
import hr.javafx.eperformance.exception.DatabaseConnectionException;
import hr.javafx.eperformance.exception.EmptyResultSetException;
import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PerformanceReviewRepository extends AbstractRepository<PerformanceReview> {

    private static boolean databaseAccessInProgress = false;

    @Override
    public synchronized List<PerformanceReview> findAll() {

        databaseAccessInProgress("Pogreška: Nit prekinuta prilikom čekanja na pristup bazi podataka.");

        databaseAccessInProgress = true;
        List<PerformanceReview> performanceReviews = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connectToDatabase();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM performance_review")) {

            boolean hasResult = false;

            while (resultSet.next()) {
                hasResult = true;
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

            if (!hasResult) {
                throw new EmptyResultSetException("Nema zapisa u tablici izvještaja.");
            }

        } catch (EmptyResultSetException | SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError("Pogreška prilikom dohvaćanja podataka: " + e.getMessage());
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }

        return performanceReviews;
    }

    @Override
    public synchronized void save(PerformanceReview entity) {
        databaseAccessInProgress("Pogreška: Nit prekinuta prilikom čekanja na unos podataka.");

        databaseAccessInProgress = true;
        String sql = "INSERT INTO performance_review (employee_id, review, reviewer, comment, date, performance_rating, improvement_plan_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, entity.getEmployee().getId());
            preparedStatement.setString(2, entity.getReview());
            preparedStatement.setString(3, entity.getReviewer());
            preparedStatement.setString(4, entity.getComment());
            preparedStatement.setDate(5, Date.valueOf(entity.getDate()));
            preparedStatement.setDouble(6, entity.getPerformanceMetrics().getPerformanceRating());
            preparedStatement.setLong(7, entity.getImprovementPlan().getId());

            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError("Pogreška prilikom spremanja podataka: " + e.getMessage());
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

    @Override
    public synchronized void delete(PerformanceReview entity) {
        databaseAccessInProgress("Pogreška: Nit prekinuta prilikom čekanja na brisanje podataka.");

        databaseAccessInProgress = true;
        String sql = "DELETE FROM performance_review WHERE id = ?";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, entity.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError("Pogreška prilikom brisanja podataka: " + e.getMessage());
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

    @Override
    public synchronized void update(PerformanceReview entity) {
        databaseAccessInProgress("Pogreška: Nit prekinuta prilikom čekanja na ažuriranje podataka.");

        databaseAccessInProgress = true;
        String sql = "UPDATE performance_review SET review = ?, comment = ?, performance_rating = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, entity.getReview());
            preparedStatement.setString(2, entity.getComment());
            preparedStatement.setDouble(3, entity.getPerformanceMetrics().getPerformanceRating());
            preparedStatement.setLong(4, entity.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError("Pogreška prilikom ažuriranja podataka: " + e.getMessage());
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

    public synchronized void updateRatingOnly(Long reviewId, double newRating) {
        databaseAccessInProgress("Pogreška: Nit prekinuta prilikom čekanja na ažuriranje ocjene.");

        databaseAccessInProgress = true;
        String sql = "UPDATE performance_review SET performance_rating = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.connectToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, newRating);
            preparedStatement.setLong(2, reviewId);
            preparedStatement.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            LoggerUtil.logError("Pogreška prilikom ažuriranja ocjene: " + e.getMessage());
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }
    }

    public synchronized Optional<PerformanceReview> findById(Long id) {
        databaseAccessInProgress("Pogreška: Nit prekinuta prilikom čekanja na dohvaćanje podataka.");

        databaseAccessInProgress = true;
        Optional<PerformanceReview> result = Optional.empty();

        try {
            result = findAll().stream()
                    .filter(performanceReview -> performanceReview.getId().equals(id))
                    .findFirst();
        } finally {
            databaseAccessInProgress = false;
            notifyAll();
        }

        return result;
    }

    private synchronized void databaseAccessInProgress(String message) {
        while (databaseAccessInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LoggerUtil.logError(message);
            }
        }
    }
}