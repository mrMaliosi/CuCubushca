module org.example.cucubushca {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    // Экспортируем пакет для доступа из javafx.graphics
    exports ru.nsu.ccfit.malinovskii.Model to javafx.graphics;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires static lombok;
    //requires com.almasb.fxgl.all;

    opens ru.nsu.ccfit.malinovskii.Model to javafx.base;
    opens ru.nsu.ccfit.malinovskii to javafx.fxml;
    opens ru.nsu.ccfit.malinovskii.Model.Objects to javafx.base;  // Открытие пакета для javafx.base

    exports ru.nsu.ccfit.malinovskii.Model.Objects;
    exports ru.nsu.ccfit.malinovskii to javafx.graphics;
    exports ru.nsu.ccfit.malinovskii.Controller;
    opens ru.nsu.ccfit.malinovskii.Controller to javafx.fxml;
}