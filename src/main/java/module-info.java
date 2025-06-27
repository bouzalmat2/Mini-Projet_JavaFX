module com.mycompany.javafx_mvc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;

    opens com.mycompany.javafx_mvc to javafx.fxml;
    exports com.mycompany.javafx_mvc;
}
