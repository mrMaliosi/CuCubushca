package ru.nsu.ccfit.malinovskii.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class SettingsController {
    @FXML
    private ChoiceBox choiceBox;

    @FXML
    public void initialize(){
        choiceBox.getSelectionModel().select(0);
    }
}
