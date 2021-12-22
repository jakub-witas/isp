module com.jwbw.isp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.jwbw.isp to javafx.fxml, javafx.graphics;
    exports com.jwbw.isp;
    exports com.jwbw.gui.Controllers;
    exports com.jwbw;
    opens com.jwbw to javafx.fxml;
    opens com.jwbw.gui.Controllers to javafx.fxml, javafx.graphics;
}