package hr.javafx.eperformance.model;

import java.time.LocalDate;

public class PerformanceReview extends Entity{

    private Employee employee;
    private String review;
    private String reviewer;
    private String comment;
    private LocalDate date;
    private final PerformanceMetrics performanceMetrics;

    public PerformanceReview(Employee employee, String review, String reviewer, String comment, LocalDate date, PerformanceMetrics performanceMetrics) {
        this.employee = employee;
        this.review = review;
        this.reviewer = reviewer;
        this.comment = comment;
        this.date = date;
        this.performanceMetrics = performanceMetrics;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }
}
