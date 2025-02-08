package hr.javafx.eperformance.model;

/**
 * EvaluationReport class represents the evaluation report of an employee.
 * @param <T> Employee type
 * @param <U> Evaluation type
 */

public class EvaluationReport<T extends Employee, U>{

    private T employee;
    private U evaluation;

    public EvaluationReport(T employee, U evaluation) {
        this.employee = employee;
        this.evaluation = evaluation;
    }

    public T getEmployee() {
        return employee;
    }

    public void setEmployee(T employee) {
        this.employee = employee;
    }

    public U getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(U evaluation) {
        this.evaluation = evaluation;
    }

}
