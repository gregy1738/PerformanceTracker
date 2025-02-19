package hr.javafx.eperformance.helper;

import hr.javafx.eperformance.model.Employee;
import hr.javafx.eperformance.model.PerformanceReview;
import hr.javafx.eperformance.repository.PerformanceReviewRepository;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class is used to help with the threads.
 * It is used to get the highest rated employee in Employee search controller.
 */

public class ThreadHelper {

    private final PerformanceReviewRepository performanceReviewRepository = new PerformanceReviewRepository();

    /**
     * This method is used to get the highest rated employee.
     * @param highestRatingLabel
     */

    public void highestPerformanceRating(Label highestRatingLabel) {
        Timeline ratingTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            Map<Employee, Double> highestRatedEmployeeMap = getHighestRatedEmployeeMap();

            if (!highestRatedEmployeeMap.isEmpty()) {
                Employee highestRatedEmployee = highestRatedEmployeeMap.keySet().iterator().next();
                double highestRating = highestRatedEmployeeMap.get(highestRatedEmployee);
                String employeeName = highestRatedEmployee.getFirstName() + " " + highestRatedEmployee.getLastName();

                highestRatingLabel.setText("Najveću ocjenu: " + highestRating + " ima zaposlenik " + employeeName);
            } else {
                highestRatingLabel.setText("Nema dostupnih ocjena u izvještajima.");
            }
        }));
        ratingTimeline.setCycleCount(Animation.INDEFINITE);
        ratingTimeline.play();

    }

    public void updatePerformanceRating(Label updateStatusLabel) {
        Timeline deleteTimeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            synchronized (performanceReviewRepository) {
                List<PerformanceReview> reviews = performanceReviewRepository.findAll();

                if (!reviews.isEmpty()) {
                    boolean reviewDeleted = false;

                    for (PerformanceReview review : reviews) {
                        if (review.getPerformanceMetrics().getPerformanceRating() == 5.0) {
                            performanceReviewRepository.delete(review);
                            reviewDeleted = true;

                            Platform.runLater(() -> updateStatusLabel.setText("Obrisana recenzija za zaposlenika: " +
                                    review.getEmployee().getFirstName() + " s ocjenom 5.0"));
                        }
                    }

                    if (!reviewDeleted) {
                        Platform.runLater(() -> updateStatusLabel.setText("Nema izvještaja s ocjenom 5.0 za brisanje."));
                    }
                } else {
                    Platform.runLater(() -> updateStatusLabel.setText("Nema izvještaja za pregled."));
                }
            }
        }));

        deleteTimeline.setCycleCount(Animation.INDEFINITE);
        deleteTimeline.play();
    }


    /**
     * This method is used to get the highest rated employee.
     * @return Map<Employee, Double> - highest rated employee map with his rating.
     */

    private Map<Employee, Double> getHighestRatedEmployeeMap() {
        List<PerformanceReview> reviews = performanceReviewRepository.findAll();

        Map<Employee, Double> ratingMap = reviews.stream()
                .collect(Collectors.toMap(
                        PerformanceReview::getEmployee,
                        review -> review.getPerformanceMetrics().getPerformanceRating(),
                        Double::max
                ));

        Optional<Map.Entry<Employee, Double>> highestRatingGrade = ratingMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (highestRatingGrade.isPresent()) {
            Map<Employee, Double> result = new HashMap<>();
            result.put(highestRatingGrade.get().getKey(), highestRatingGrade.get().getValue());
            return result;
        }
        return Map.of();
    }

}
