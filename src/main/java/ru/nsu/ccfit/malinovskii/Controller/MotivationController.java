package ru.nsu.ccfit.malinovskii.Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MotivationController {

    @FXML
    private ImageView imageView;  // Элемент для отображения изображения

    @FXML
    private Label quoteLabel;     // Элемент для отображения цитаты

    private Random random = new Random();

    public void initialize() {
        String jarFilePath = null;
        try {
            jarFilePath = MotivationController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        listFilesInJar(jarFilePath, "/ru/nsu/ccfit/malinovskii/motivation/posters/");
        // Загружаем случайное изображение
        loadRandomImage();

        // Загружаем случайную цитату
        loadRandomQuote();
    }

    // Метод для загрузки случайного изображения
    private void loadRandomImage() {
        // Стандартизированные имена изображений
        List<String> imageNames = List.of("1.jpg", "2.jpg", "3.jpg");

        // Выбираем случайное имя изображения
        String randomImageName = imageNames.get(random.nextInt(imageNames.size()));

        // Загружаем изображение
        InputStream imageStream = getClass().getResourceAsStream("/ru/nsu/ccfit/malinovskii/motivation/posters/" + randomImageName);
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imageView.setImage(image);  // Устанавливаем изображение в ImageView
        } else {
            System.out.println("Image not found: " + randomImageName);
        }
    }

    // Метод для загрузки случайной цитаты
    private void loadRandomQuote() {
        // Стандартизированные имена файлов цитат
        List<String> quoteNames = List.of("1.txt", "2.txt");

        // Выбираем случайное имя файла цитаты
        String randomQuoteName = quoteNames.get(random.nextInt(quoteNames.size()));

        // Читаем цитатуНу,
        InputStream quoteStream = getClass().getResourceAsStream("/ru/nsu/ccfit/malinovskii/motivation/quotes/" + randomQuoteName);
        if (quoteStream != null) {
            try {
                // Читаем строку из файла (можно использовать BufferedReader)
                String randomQuote = new String(quoteStream.readAllBytes());
                quoteLabel.setText(randomQuote);  // Устанавливаем цитату в Label
            } catch (IOException e) {
                e.printStackTrace();
                quoteLabel.setText("Error loading quote.");
            }
        } else {
            System.out.println("Quote not found: " + randomQuoteName);
            quoteLabel.setText("Error loading quote.");
        }
    }

    public static void listFilesInJar(String jarFilePath, String dirPath) {
        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                // Проверяем, что файл находится в нужной директории
                if (entryName.startsWith(dirPath)) {
                    System.out.println(entryName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
