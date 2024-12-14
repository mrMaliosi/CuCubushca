package ru.nsu.ccfit.malinovskii.Controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ru.nsu.ccfit.malinovskii.Model.Objects.Context;
import ru.nsu.ccfit.malinovskii.Model.Objects.Status;
import ru.nsu.ccfit.malinovskii.Model.Objects.Subject;
import ru.nsu.ccfit.malinovskii.Model.Objects.Task;

import java.util.List;
import java.util.Objects;

import static ru.nsu.ccfit.malinovskii.Model.CuCubushca.fm;
import static ru.nsu.ccfit.malinovskii.Model.Objects.Context.getContext;

public class SubjectController {
    @FXML
    private Subject subject;
    @FXML
    private TableView<ObservableList<String>> tableView; // Это предполагается уже у вас как таблица
    @FXML
    private GridPane grid;

    private PieChart pieChart;
    Context context = getContext();

    @FXML
    public void initialize() {
        // Инициализация будет происходить в setSubject, когда будет установлен subject.
    }

    // Метод для установки текущего предмета
    public void setSubject(Subject subject) {
        this.subject = subject;
        initializeSubject();
    }

    // Метод для инициализации предмета
    public void initializeSubject() {
        // Создаем контейнер HBox для таблицы и диаграммы
        HBox tableAndChartContainer = new HBox(20); // Отступ между таблицей и диаграммой
        tableAndChartContainer.setAlignment(Pos.CENTER_LEFT);

        // Инициализация заголовков
        Label labelSubjectName = new Label(subject.getName());
        labelSubjectName.setStyle("-fx-alignment: center;");
        Label labelStatus = new Label("Статус");
        labelSubjectName.setStyle("-fx-alignment: center;");

        grid.setHgap(5);  // Расстояние между колонками
        //grid.setVgap(1);  // Расстояние между строками

        // Добавляем границу для всей сетки
        grid.setStyle("-fx-border-color: black; -fx-border-width: 2;"); // Установка границ для grid
        grid.add(labelSubjectName, 0, 0);
        grid.add(labelStatus, 1, 0);

        // Список задач
        List<Task> tasks = subject.getTasksList();
        int taskN = 1;

        // Очищаем старые столбцы и строки
        grid.getColumnConstraints().clear();

        // Добавление выбора статуса для каждой задачи
        for (Task task : tasks) {
            // Создание нового столбца и добавление его в GridPane
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(col);

            // Добавление названия задачи в первую колонку
            Label labelColumnName = new Label(task.getName());
            labelColumnName.setStyle("-fx-border-color: black; -fx-border-width: 1;"); // Установка границы для ячейки
            grid.add(labelColumnName, 0, taskN);

            // Создание отдельного ChoiceBox для каждой задачи
            ChoiceBox<String> choiceBox = new ChoiceBox<>();
            choiceBox.getItems().addAll("NOT_DONE", "IN_WORK", "PASS");
            choiceBox.setValue(task.getStatus().name());

            // Устанавливаем цвет фона в зависимости от статуса задачи
            String bgColor = getStatusColor(task.getStatus());
            choiceBox.setStyle("-fx-background-color: " + bgColor + "; -fx-border-color: black; -fx-border-width: 1;");

            // Добавление ChoiceBox в GridPane
            grid.add(choiceBox, 1, taskN);

            // Обработчик изменения выбора в ChoiceBox
            choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println(oldValue + " " + newValue);
                if (!newValue.equals(oldValue)) {
                    switch (newValue){
                        case "NOT_DONE":
                            task.setStatus(Status.NOT_DONE);
                            break;
                        case "IN_WORK":
                            task.setStatus(Status.IN_WORK);
                            break;
                        case "PASS":
                            task.setStatus(Status.PASS);
                            break;
                    }

                    String newBgColor = getStatusColor(task.getStatus());
                    choiceBox.setStyle("-fx-background-color: " + newBgColor + "; -fx-border-color: black; -fx-border-width: 1;");
                    fm.save(context.getWorkspaces());
                    updatePieChart(tasks); // Обновляем диаграмму
                }
            });
            ++taskN;
        }

        // Создание круговой диаграммы (PieChart)
        pieChart = createPieChart(tasks);  // Сохраняем диаграмму в поле
        tableAndChartContainer.getChildren().add(pieChart);  // Добавляем диаграмму рядом с таблицей

        // Добавляем HBox в GridPane
        grid.add(tableAndChartContainer, 0, taskN + 1, 2, 1); // Размещение HBox в GridPane ниже таблицы
    }

    // Метод для получения цвета в зависимости от статуса
    private String getStatusColor(Status status) {
        switch (status) {
            case IN_WORK:
                return "#FFA500"; // Оранжевый для статуса "Начато"
            case PASS:
                return "#32CD32"; // Зеленый для статуса "Готово"
            default:
                return "#FF6347"; // Красный для статуса "Не сделано"
        }
    }

    // Метод для обновления диаграммы PieChart после изменения статуса
    private void updatePieChart(List<Task> tasks) {
        int tasksStarted = 0;
        int tasksAlmost = 0;
        int tasksReady = 0;

        for (Task task : tasks) {
            switch (task.getStatus()) {
                case IN_WORK -> ++tasksAlmost;
                case PASS -> ++tasksReady;
                default -> ++tasksStarted;
            }
        }
        int tasksAll = tasksStarted + tasksAlmost + tasksReady;

        PieChart.Data[] pieData = new PieChart.Data[3];
        int i = 0;
        for (PieChart.Data slice : pieChart.getData()){
            switch (i){
                case 0:
                    slice.setPieValue((double)tasksStarted / tasksAll);
                    pieData[i] = slice;
                    break;
                case 1:
                    slice.setPieValue((double)tasksAlmost / tasksAll);
                    pieData[i] = slice;
                    break;
                case 2:
                    slice.setPieValue((double)tasksReady / tasksAll);
                    pieData[i] = slice;
                    break;
            }
            ++i;
        }
    }

    // Метод для создания данных для PieChart
    private PieChart.Data[] createPieChartData(List<Task> tasks) {
        int tasksStarted = 0;
        int tasksAlmost = 0;
        int tasksReady = 0;

        for (Task task : tasks) {
            switch (task.getStatus()) {
                case IN_WORK -> ++tasksAlmost;
                case PASS -> ++tasksReady;
                default -> ++tasksStarted;
            }
        }
        int tasksAll = tasksStarted + tasksAlmost + tasksReady;

        PieChart.Data slice1 = new PieChart.Data("NOT_DONE", (double)tasksStarted / tasksAll);
        PieChart.Data slice2 = new PieChart.Data("IN_WORK", (double)tasksAlmost / tasksAll);
        PieChart.Data slice3 = new PieChart.Data("PASS", (double)tasksReady / tasksAll);

        return new PieChart.Data[] { slice1, slice2, slice3 };
    }

    // Метод для создания диаграммы PieChart
    private PieChart createPieChart(List<Task> tasks) {
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(createPieChartData(tasks));
        pieChart.setMaxWidth(200);  // Ограничиваем ширину диаграммы
        pieChart.setMaxHeight(200); // Ограничиваем высоту диаграммы
        pieChart.setLegendVisible(false); // Отображение легенды диаграммы
        return pieChart;
    }
}
