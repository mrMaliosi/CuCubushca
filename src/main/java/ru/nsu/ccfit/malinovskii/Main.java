package ru.nsu.ccfit.malinovskii;

import javafx.application.Application;
import ru.nsu.ccfit.malinovskii.Model.CuCubushca;

import static javafx.application.Application.launch;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static CuCubushca application;

    public static void main(String[] args) {
        application = new CuCubushca();

        try {
            Application.launch(CuCubushca.class, args);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            //logger.error("Exception: ", e);
        }
    }
}