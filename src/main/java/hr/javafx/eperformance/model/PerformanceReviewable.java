package hr.javafx.eperformance.model;

public sealed interface PerformanceReviewable permits ImprovementPlan{

    /**
     * Calculates the average performance rating in a single improvement plan
     * @return the average performance rating in a single improvement plan
     */
    double calculateAveragePerformanceRating();
}
