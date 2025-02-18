package hr.javafx.eperformance.controller;

import hr.javafx.eperformance.model.ChangeLog;
import hr.javafx.eperformance.repository.ChangeLogRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ChangeLogController {

    @FXML
    private TableView<ChangeLog> changeLogTableView;

    @FXML
    private TableColumn<ChangeLog, String> fieldColumn;

    @FXML
    private TableColumn<ChangeLog, String> oldValueColumn;

    @FXML
    private TableColumn<ChangeLog, String> newValueColumn;

    @FXML
    private TableColumn<ChangeLog, String> changedByEmailColumn;

    @FXML
    private TableColumn<ChangeLog, String> changedByColumn;

    @FXML
    private TableColumn<ChangeLog, String> timestampColumn;

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

        fieldColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFieldChanged()));
        oldValueColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOldValue()));
        newValueColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNewValue()));
        changedByEmailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getChangedByEmail()));
        changedByColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getChangedByRole())));
        timestampColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimestamp().format(formatter)));

        List<ChangeLog> logs = ChangeLogRepository.getAllChanges();
        changeLogTableView.setItems(FXCollections.observableList(logs));
    }
}
