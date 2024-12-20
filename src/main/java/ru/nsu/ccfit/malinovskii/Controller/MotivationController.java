package ru.nsu.ccfit.malinovskii.Controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class MotivationController {
    @FXML
    private ImageView imageView;  // Элемент для отображения изображения
    @FXML
    private Label quoteLabel;     // Элемент для отображения цитаты

    private final Random random = new Random();

    // Количество файлов изображений и цитат
    private static final int NUM_IMAGES = 10;  // Например, 3 изображения (1.jpg, 2.jpg, 3.jpg)
    private static final int NUM_QUOTES = 10;  // Например, 2 цитаты (1.txt, 2.txt)

    // Метод для инициализации
    public void initialize() {
        quoteLabel.setWrapText(true);  // Включаем обтекание текста
        quoteLabel.setMaxWidth(506);   // Устанавливаем максимальную ширину для обтекания
        loadRandomImage();
        loadRandomQuote();
    }

    // Метод для загрузки случайного изображения
    private void loadRandomImage() {
        // Выбираем случайное число для изображения (от 1 до NUM_IMAGES)
        int randomImageNumber = random.nextInt(NUM_IMAGES) + 1;
        String randomImageName = randomImageNumber + ".jpg";  // Формируем имя файла изображения

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
        // Выбираем случайное число для цитаты (от 1 до NUM_QUOTES)
        int randomQuoteNumber = random.nextInt(NUM_QUOTES) + 1;
        String randomQuoteName = randomQuoteNumber + ".txt";  // Формируем имя файла цитаты

        // Читаем цитату с учётом кодировки UTF-8
        InputStream quoteStream = getClass().getResourceAsStream("/ru/nsu/ccfit/malinovskii/motivation/quotes/" + randomQuoteName);
        if (quoteStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(quoteStream, StandardCharsets.UTF_8))) {
                StringBuilder quoteContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    quoteContent.append(line).append("\n");
                }

                quoteLabel.setText(quoteContent.toString().trim());
            } catch (IOException e) {
                e.printStackTrace(System.err);
                quoteLabel.setText("Error loading quote.");
            }
        } else {
            System.out.println("Quote not found: " + randomQuoteName);
            quoteLabel.setText("Error loading quote.");
        }
    }
}
