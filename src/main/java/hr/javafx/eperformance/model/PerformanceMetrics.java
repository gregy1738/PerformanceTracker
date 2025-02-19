package hr.javafx.eperformance.model;

/**
 * PerformanceMetrics class represents the performance metrics of an employee.
 * @param performanceRating double value of performance rating of an employee
 */

public record PerformanceMetrics(Double performanceRating) {
    public Double getPerformanceRating() {
        return performanceRating;
    }
}
