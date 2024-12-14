package ru.nsu.ccfit.malinovskii.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.nsu.ccfit.malinovskii.Model.Objects.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static ru.nsu.ccfit.malinovskii.Model.CuCubushca.fm;
import static ru.nsu.ccfit.malinovskii.Model.Objects.Context.getContext;

public class WorkspaceController {
    @FXML
    private VBox workspaceContainer;

    private Workspace workspace;  // Текущая рабочая область
    Context context = getContext();

    // Поле для хранения диаграммы, чтобы обновлять её позже
    private PieChart pieChart;

    // Метод для установки текущей рабочей области
    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
        initializeWorkspace();
    }

    // Метод для инициализации рабочего пространства
    public void initializeWorkspace() {
        List<String> subjectNames = workspace.getSubjectNames();

        for (String subjectName : subjectNames) {
            List<Task> tasks = workspace.getSubjectTasks(subjectName);
            createTable(subjectName, tasks);
        }
    }

    private void createTable(String subjectName, List<Task> tasks) {
        // Создание HBox для размещения таблицы и диаграммы рядом
        HBox tableAndChartContainer = new HBox(20); // Отступ между таблицей и диаграммой
        tableAndChartContainer.setAlignment(Pos.CENTER_LEFT);

        // Создание TableView
        TableView<ObservableList<String>> tableView = new TableView<>();
        tableView.setMaxWidth(400);
        tableView.setMaxHeight(160);

        // Столбец для предмета
        TableColumn<ObservableList<String>, String> subjectColumn = new TableColumn<>(subjectName);
        subjectColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));  // Первая ячейка строки
        tableView.getColumns().add(subjectColumn);

        // Создание столбцов для каждой задачи
        for (Task task : tasks) {
            TableColumn<ObservableList<String>, String> taskColumn = new TableColumn<>(task.getName());
            taskColumn.setCellFactory(col -> new TableCell<ObservableList<String>, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(null);

                    if (!empty) {
                        // Создаем ChoiceBox для изменения статуса задачи
                        ChoiceBox<String> choiceBox = new ChoiceBox<>();
                        choiceBox.getItems().addAll("-", "Начато", "Уже близко", "PASS");

                        // Устанавливаем текущее состояние задачи в ChoiceBox
                        String currentStatus = task.getStatus().name();  // Получаем текущий статус задачи
                        choiceBox.setValue(currentStatus);

                        // Обработчик изменения состояния задачи
                        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                            if (!Objects.equals(newValue, oldValue)){
                                switch (newValue){
                                    case "-":
                                        task.setStatus(Status.NOT_DONE);
                                        break;
                                    case "Начато":
                                        task.setStatus(Status.NOT_DONE);
                                        break;
                                    case "Уже близко":
                                        task.setStatus(Status.IN_WORK);
                                        break;
                                    case "Готово":
                                        task.setStatus(Status.PASS);
                                        break;
                                }

                                fm.save(context.getWorkspaces());
                                updatePieChart(tasks); // Обновляем диаграмму
                            }
                        });

                        setGraphic(choiceBox);  // Устанавливаем ChoiceBox в ячейку
                        setStyle("-fx-font-size: 20px; -fx-background-color: white;");  // Устанавливаем стиль
                        setAlignment(Pos.CENTER);  // Выравнивание по центру
                    }
                }
            });
            tableView.getColumns().add(taskColumn);  // Добавляем столбец для каждой задачи
        }

        // Заполняем строки данными
        for (int i = 0; i < 1; i++) {
            ObservableList<String> row = FXCollections.observableArrayList();
            row.add("Статус");
            // Добавляем пустые значения для каждой задачи
            for (int j = 0; j < tasks.size(); j++) {
                row.add("");
            }

            tableView.getItems().add(row);
        }

        // Стилизация строк через TableRow
        tableView.setRowFactory(tv -> {
            TableRow<ObservableList<String>> row = new TableRow<>();
            row.setStyle("-fx-font-size: 20px;");
            return row;
        });

        // Добавляем таблицу в контейнер
        tableAndChartContainer.getChildren().add(tableView);

        // Создание круговой диаграммы (PieChart)
        pieChart = createPieChart(tasks);  // Сохраняем диаграмму в поле
        tableAndChartContainer.getChildren().add(pieChart);  // Добавляем диаграмму рядом с таблицей

        // Добавляем контейнер с таблицей и диаграммой в основной контейнер
        workspaceContainer.getChildren().add(tableAndChartContainer);
    }

    private void updatePieChart(List<Task> tasks) {
        int tasksStarted = 0;
        int tasksAlmost = 0;
        int tasksReady = 0;

        for (Task task : tasks) {
            switch (task.getStatus()){
                case IN_WORK -> ++tasksAlmost;
                case PASS -> ++tasksReady;
                default -> ++tasksStarted;
            }
        }
        int tasksAll = tasksStarted + tasksAlmost + tasksReady;

        PieChart.Data slice1 = new PieChart.Data("Надо сделать", (double)tasksStarted/tasksAll * 100);
        PieChart.Data slice2 = new PieChart.Data("Уже близко", (double)tasksAlmost/tasksAll * 100);
        PieChart.Data slice3 = new PieChart.Data("Готово", (double)tasksReady/tasksAll * 100);
        pieChart.setLegendVisible(true);

        // Обновляем существующую диаграмму
        pieChart.getData().clear();  // Очищаем старые данные диаграммы
        pieChart.getData().addAll(slice1, slice2, slice3);
        //pieChart.setLegendVisible(true); // Отображение легенды диаграммы
    }

    private PieChart createPieChart(List<Task> tasks) {
        int tasksStarted = 0;
        int tasksAlmost = 0;
        int tasksReady = 0;

        for (Task task : tasks) {
            switch (task.getStatus()){
                case IN_WORK -> ++tasksAlmost;
                case PASS -> ++tasksReady;
                default -> ++tasksStarted;
            }
        }
        int tasksAll = tasksStarted + tasksAlmost + tasksReady;

        PieChart.Data slice1 = new PieChart.Data("Надо сделать", (double)tasksStarted/tasksAll);
        PieChart.Data slice2 = new PieChart.Data("Уже близко", (double)tasksAlmost/tasksAll);
        PieChart.Data slice3 = new PieChart.Data("Готово", (double)tasksReady/tasksAll);

        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(slice1, slice2, slice3);
        pieChart.setMaxWidth(200);  // Ограничиваем ширину диаграммы
        pieChart.setMaxHeight(200); // Ограничиваем высоту диаграммы
        pieChart.setLegendVisible(true); // Отображение легенды диаграммы

        return pieChart;
    }

    // Обработчик для создания нового предмета
    @FXML
    private void handleCreateSubject() {
        try {
            URL motivationXmlUrl = getClass().getResource("/ru/nsu/ccfit/malinovskii/view/subject-creation-view.fxml");
            FXMLLoader loader = new FXMLLoader(motivationXmlUrl);
            Parent root = loader.load();

            // Получаем контроллер из FXML
            SubjectCreationController controller = loader.getController();

            // Создаём новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Создание предмета");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Модальное окно, блокирует остальные окна до закрытия
            stage.showAndWait(); // Ждём закрытия окна

            // Получаем имя рабочего пространства, которое было введено в окне
            String newName = controller.getSubjectName();
            int tasksNumber = controller.getTasksNumber();

            // Если имя было введено, добавляем рабочую область в контекст
            if (newName != null && !newName.isEmpty()) {
                Subject newSubject = new Subject(newName);
                for (int i = 1; i <= tasksNumber; ++i) {
                    newSubject.addTaskByName(String.valueOf(i));
                }
                workspace.addSubject(newSubject);
                fm.save(context.getWorkspaces());

                createTable(newSubject.getName(), newSubject.getTasksList()); // Создаем таблицу для нового предмета
            } else {
                System.out.println("Имя рабочей области не может быть пустым!");
            }
        } catch (IOException ex) {
            // Обработка ошибок при загрузке FXML
            ex.printStackTrace();
        }
    }
}
