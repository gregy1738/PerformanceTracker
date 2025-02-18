package hr.javafx.eperformance.model;

import java.time.LocalDate;

public class PerformanceReview extends Entity {
    private Employee employee;
    private String review;
    private String reviewer;
    private String comment;
    private LocalDate date;
    private ImprovementPlan improvementPlan;
    private final PerformanceMetrics performanceMetrics;

    private PerformanceReview(Builder builder) {
        super(builder.id);
        this.employee = builder.employee;
        this.review = builder.review;
        this.reviewer = builder.reviewer;
        this.comment = builder.comment;
        this.date = builder.date;
        this.improvementPlan = builder.improvementPlan;
        this.performanceMetrics = builder.performanceMetrics;
    }

    public Employee getEmployee() {
        return employee;
    }

    public String getReview() {
        return review;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getComment() {
        return comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public ImprovementPlan getImprovementPlan() {
        return improvementPlan;
    }

    public static class Builder {
        private Long id;
        private Employee employee;
        private String review;
        private String reviewer;
        private String comment;
        private LocalDate date;
        private ImprovementPlan improvementPlan;
        private PerformanceMetrics performanceMetrics;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public Builder review(String review) {
            this.review = review;
            return this;
        }

        public Builder reviewer(String reviewer) {
            this.reviewer = reviewer;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder improvementPlan(ImprovementPlan improvementPlan) {
            this.improvementPlan = improvementPlan;
            return this;
        }

        public Builder performanceMetrics(PerformanceMetrics performanceMetrics) {
            this.performanceMetrics = performanceMetrics;
            return this;
        }

        public PerformanceReview build() {
            return new PerformanceReview(this);
        }
    }
}
