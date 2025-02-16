package hr.javafx.eperformance.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ImprovementPlan extends Entity {

    private Employee employee;
    private String description;
    private List<PerformanceReview> performanceReviews;
    private LocalDate startDate;
    private LocalDate endDate;

    public ImprovementPlan(Employee employee, String description, List<PerformanceReview> performanceReviews, LocalDate startDate, LocalDate endDate) {
        this.employee = employee;
        this.description = description;
        this.performanceReviews = new ArrayList<>(performanceReviews);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PerformanceReview> getPerformanceReviews() {
        return performanceReviews;
    }

    public void addPerformanceReview(PerformanceReview performanceReview) {
        performanceReviews.add(performanceReview);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
