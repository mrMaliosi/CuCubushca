package ru.nsu.ccfit.malinovskii.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.nsu.ccfit.malinovskii.Model.Objects.Context;

import static ru.nsu.ccfit.malinovskii.Model.Objects.Context.getContext;

public class SubjectCreationController {
    @FXML
    public TextField subjectNameField;
    @FXML
    public Spinner<Integer> tasksNumberSpinner;
    @FXML
    private Label errorLabel;
    @FXML
    public Button createButton;

    private String subjectName = null;
    private int tasksNumber = 0;
    Context context = getContext();

    public String getSubjectName() {
        return subjectName;
    }

    public int getTasksNumber() {
        return tasksNumber;
    }

    @FXML
    public void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, context.MAX_TASKS_NUMBER);
        tasksNumberSpinner.setValueFactory(valueFactory);

        createButton.setOnAction(e -> {
            subjectName = subjectNameField.getText();
            if (subjectName != null && !subjectName.isEmpty()) {
                tasksNumber = tasksNumberSpinner.getValue();
                Stage stage = (Stage) createButton.getScene().getWindow();
                stage.close();
            } else {
                errorLabel.setText("ERROR: wrong workspace name.");
            }
        });
    }
}
