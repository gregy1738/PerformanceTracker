package hr.javafx.eperformance.model;

/**
 * PerformanceMetrics class represents the performance metrics of an employee.
 * @param performanceRating double value of performance rating of an employee
 */

public record PerformanceMetrics(double performanceRating) {
    double getPerformanceRating() {
        return performanceRating;
    }
}
