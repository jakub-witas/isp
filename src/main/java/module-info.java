module com.jwbw.isp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.jwbw.isp to javafx.fxml;
    exports com.jwbw.isp;
    exports com.jwbw.Controllers;
    opens com.jwbw.Controllers to javafx.fxml;
    exports com.jwbw;
    opens com.jwbw to javafx.fxml;
}