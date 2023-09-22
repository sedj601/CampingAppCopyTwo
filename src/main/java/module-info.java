module com.camping {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.antdesignicons;
    requires org.controlsfx.controls;
    requires java.sql;
    
    opens com.camping to javafx.fxml;
    exports com.camping;
}
