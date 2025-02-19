package hr.javafx.eperformance.model;

/**
 * This interface is used to count employees in certain department.
 */

public sealed interface EmployeeCountable permits Department{

    int countEmployees();

}
