module hr.javafx.eperformance.gregurec_projekt {
    requires javafx.controls;
    requires javafx.fxml;


    opens hr.javafx.eperformance to javafx.fxml;
    exports hr.javafx.eperformance;
}