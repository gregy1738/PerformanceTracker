package hr.javafx.eperformance.repository;

import hr.javafx.eperformance.helper.LoggerUtil;
import hr.javafx.eperformance.model.ChangeLog;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeLogRepository {

    private ChangeLogRepository() {}

    private static final String FILE_NAME = "dat/changelog.dat";

    public static void saveChange(ChangeLog changeLog) {
        List<ChangeLog> logs = getAllChanges();
        logs.add(changeLog);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(logs);
        } catch (IOException e) {
            LoggerUtil.logError("Pogreška prilikom spremanja promjena.");
        }
    }

    public static List<ChangeLog> getAllChanges() {
        List<ChangeLog> changeLogs = new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            changeLogs = (List<ChangeLog>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LoggerUtil.logError("Pogreška prilikom čitanja promjena.");
        }

        return changeLogs;
    }
}
