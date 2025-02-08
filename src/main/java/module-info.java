module hr.javafx.eperformance.gregurec_projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires java.logging;
    requires org.slf4j;


    opens hr.javafx.eperformance to javafx.fxml;
    exports hr.javafx.eperformance;
    exports hr.javafx.eperformance.controller;
    opens hr.javafx.eperformance.controller to javafx.fxml;
}