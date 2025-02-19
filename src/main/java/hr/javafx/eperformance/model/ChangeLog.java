package hr.javafx.eperformance.model;

import hr.javafx.eperformance.enums.EmployeeType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * This class is used to store the change log.
 */

public class ChangeLog implements Serializable {

    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private String changedByEmail;
    private EmployeeType changedByRole;
    private LocalDateTime timestamp;

    public ChangeLog(String fieldChanged, String oldValue, String newValue, String email, EmployeeType changedByRole) {
        this.fieldChanged = fieldChanged;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedByEmail = email;
        this.changedByRole = changedByRole;
        this.timestamp = LocalDateTime.now();
    }

    public String getFieldChanged() {
        return fieldChanged;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getChangedByEmail() {
        return changedByEmail;
    }

    public EmployeeType getChangedByRole() {
        return changedByRole;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Promjena: " + fieldChanged + " | Stara vrijednost: " + oldValue + " | Nova vrijednost: " + newValue +
                " | Promijenio: " + changedByRole + " | Vrijeme: " + timestamp;
    }
}
