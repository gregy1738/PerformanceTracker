package hr.javafx.eperformance.model;

public sealed interface EmployeeCountable permits Department{

    int countEmployees();

}
