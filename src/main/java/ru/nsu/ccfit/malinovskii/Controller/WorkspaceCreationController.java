package ru.nsu.ccfit.malinovskii.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class WorkspaceCreationController {
    @FXML
    private Label errorLabel;
    @FXML
    public Button createButton;
    @FXML
    public TextField workspaceNameField;

    private String workspaceName = null;

    public String getWorkspaceName() {
        return workspaceName;
    }

    @FXML
    public void initialize() {
        createButton.setOnAction(e -> {
            workspaceName = workspaceNameField.getText();
            if (workspaceName != null && !workspaceName.isEmpty()) {
                Stage stage = (Stage) createButton.getScene().getWindow();
                stage.close();  // Закрываем окно после создания рабочего пространства
            } else {
                errorLabel.setText("ERROR: wrong workspace name.");
            }
        });
    }
}
