package hr.javafx.eperformance.model;

import java.util.Set;

/**
 * This class is used to represent the department.
 */

public non-sealed class Department extends Entity implements EmployeeCountable {

    private String name;
    private String description;
    private Set<Employee> employees;

    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Department(Long id, String name, String description, Set<Employee> employees) {
        super(id);
        this.name = name;
        this.description = description;
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public int countEmployees() {
        return employees.size();
    }
}
