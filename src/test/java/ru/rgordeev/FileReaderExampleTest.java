package ru.rgordeev;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

/**
 * Модульные тесты для класса FileReaderExample.
 * Проверяет функциональность чтения бинарных и текстовых файлов.
 */
class FileReaderExampleTest {
    /**
     * Экземпляр тестируемого класса.
     * Создается заново перед каждым тестом.
     */
    private FileReaderExample fileReaderExample;

    /**
     * Временная директория для тестовых файлов.
     * Автоматически создается перед каждым тестом и удаляется после.
     * Аннотация @TempDir обеспечивает изоляцию тестов и автоматическую очистку.
     */
    @TempDir
    Path tempDir;

    /**
     * Инициализация перед каждым тестом.
     * Создает новый экземпляр FileReaderExample.
     */
    @BeforeEach
    void setUp() {
        fileReaderExample = new FileReaderExample();
    }

    /**
     * Тест проверяет корректность чтения бинарного файла.
     * Создает временный файл с известными данными и проверяет,
     * что метод readBinaryFile корректно их считывает.
     *
     * @throws IOException если возникли проблемы при работе с файлом
     */
    @Test
    void testReadBinaryFile() throws IOException {
        // Подготовка тестовых данных
        byte[] testData = {1, 2, 3, 4, 5};
        File testFile = tempDir.resolve("test.bin").toFile();
        try (FileOutputStream fos = new FileOutputStream(testFile)) {
            fos.write(testData);
        }

        // Выполнение тестируемого метода
        List<Byte> result = fileReaderExample.readBinaryFile(testFile.getPath());

        // Проверка результатов
        Assertions.assertEquals(testData.length, result.size(),
                "Размер прочитанных данных должен совпадать с размером исходных данных");
        for (int i = 0; i < testData.length; i++) {
            Assertions.assertEquals(testData[i], result.get(i),
                    "Каждый байт должен совпадать с исходными данными");
        }
    }

    /**
     * Тест проверяет корректность чтения текстового файла.
     * Создает временный файл с тестовым текстом и проверяет,
     * что метод readTextFile корректно его считывает.
     *
     * @throws IOException если возникли проблемы при работе с файлом
     */
    @Test
    void testReadTextFile() throws IOException {
        // Подготовка тестовых данных
        String testContent = "Hello, World!";
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile, StandardCharsets.UTF_8)) {
            writer.write(testContent);
        }

        // Выполнение тестируемого метода
        String result = fileReaderExample.readTextFile(testFile.getPath());

        // Проверка результатов
        Assertions.assertEquals(testContent, result,
                "Прочитанный текст должен совпадать с исходным");
    }

    /**
     * Тест проверяет, что при попытке чтения несуществующего бинарного файла
     * генерируется исключение IOException.
     */
    @Test
    void testReadBinaryFileNotFound() {
        // Подготовка тестовых данных
        String nonExistentFile = "non-existent.bin";

        // Проверка генерации исключения
        Assertions.assertThrows(IOException.class,
                () -> fileReaderExample.readBinaryFile(nonExistentFile),
                "Должно быть выброшено исключение при попытке чтения несуществующего файла");
    }

    /**
     * Тест проверяет, что при попытке чтения несуществующего текстового файла
     * генерируется исключение IOException.
     */
    @Test
    void testReadTextFileNotFound() {
        // Подготовка тестовых данных
        String nonExistentFile = "non-existent.txt";

        // Проверка генерации исключения
        Assertions.assertThrows(IOException.class,
                () -> fileReaderExample.readTextFile(nonExistentFile),
                "Должно быть выброшено исключение при попытке чтения несуществующего файла");
    }
}
