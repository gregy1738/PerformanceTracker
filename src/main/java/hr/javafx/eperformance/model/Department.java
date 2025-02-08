package hr.javafx.eperformance.model;

import java.util.Set;

public class Department extends Entity {

    private String name;
    private String description;
    private Set<Employee> employees;

    public Department(String name, String description, Set<Employee> employees) {
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
}
