package ru.rgordeev;

import org.junit.jupiter.api.Test;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class InputStreamVsReaderTest {

    @Test
    void testReadTextFileWithInputStreamAndReader() throws IOException {
        // Подготовка: создаем временный текстовый файл с известным содержимым
        Path tempFile = Files.createTempFile("example", ".txt");
        String content = "Привет, мир!";  // текст с русскими символами
        Files.writeString(tempFile, content, StandardCharsets.UTF_8);

        // Чтение с помощью InputStream (байтово) и преобразование в строку
        StringBuilder resultBytes = new StringBuilder();
        try (InputStream in = Files.newInputStream(tempFile)) {
            int byteVal;
            while ((byteVal = in.read()) != -1) {
                resultBytes.append((char) byteVal); // некорректный способ для многобайтовой кодировки, для теста
            }
        }
        String bytesAsString = resultBytes.toString();

        // Чтение с помощью Reader (символьно)
        StringBuilder resultChars = new StringBuilder();
        try (Reader reader = Files.newBufferedReader(tempFile, StandardCharsets.UTF_8)) {
            int charVal;
            while ((charVal = reader.read()) != -1) {
                resultChars.append((char) charVal);
            }
        }
        String charsAsString = resultChars.toString();

        // Сравнение результатов
        assertNotEquals(content, bytesAsString, "Неправильное чтение байтов без учета кодировки не должно совпадать с исходным текстом");
        assertEquals(content, charsAsString, "Чтение через Reader должно восстановить оригинальный текст");
    }
}
