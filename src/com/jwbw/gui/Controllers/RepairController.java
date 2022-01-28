package com.jwbw.gui.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import java.sql.SQLException;

public class RepairController {

    @FXML
    private Button repairButton;

    @FXML
    public void initialize() throws SQLException {
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Oznacz jako naprawione");
        repairButton.setTooltip(tooltip);
        repairButton.setOnMouseEntered(e -> repairButton.setStyle("-fx-background-color: green"));
        repairButton.setOnMouseExited(e -> repairButton.setStyle("-fx-background-color: #99CCFF;"));
    }

    public void handleButtonRepair() {

    }
}
