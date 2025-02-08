package hr.javafx.eperformance.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * This class represents an employee.
 */

public class Employee extends Entity {

    private String firstName;
    private String lastName;
    private String jobTitle;
    private BigDecimal salary;
    private Department department;

    /**
     * Constructor for the Employee class.
     * @param builder instance of the Builder class
     */

    public Employee(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.jobTitle = builder.jobTitle;
        this.salary = builder.salary;
        this.department = builder.department;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Department getDepartment() {
        return department;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String jobTitle;
        private BigDecimal salary;
        private Department department;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder jobTitle(String jobTitle) {
            this.jobTitle = jobTitle;
            return this;
        }

        public Builder salary(BigDecimal salary) {
            this.salary = salary;
            return this;
        }

        public Builder department(Department department) {
            this.department = department;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName)
                && Objects.equals(jobTitle, employee.jobTitle) && Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, jobTitle, department);
    }
}
