package ru.rgordeev;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса ZipFileSystemExample.
 * Проверяет функциональность работы с ZIP-архивами, включая создание,
 * чтение и копирование файлов в архив.
 */
class ZipFileSystemExampleTest {

    /** Временная директория для тестовых файлов */
    @TempDir
    Path tempDir;

    /** Тестируемый экземпляр класса */
    private ZipFileSystemExample zipFileSystem;

    /** Путь к тестовому ZIP-архиву */
    private Path zipPath;

    /**
     * Инициализация перед каждым тестом.
     * Создает новый ZIP-файл во временной директории и инициализирует
     * экземпляр ZipFileSystemExample.
     */
    @BeforeEach
    void setUp() {
        zipPath = tempDir.resolve("test.zip");
        zipFileSystem = new ZipFileSystemExample(zipPath);
    }

    /**
     * Очистка после каждого теста.
     * Удаляет созданный ZIP-файл.
     *
     * @throws IOException если возникла ошибка при удалении файла
     */
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(zipPath);
    }

    /**
     * Проверяет создание нового файла в ZIP-архиве и корректность его содержимого.
     *
     * @throws IOException если возникла ошибка при работе с файлами
     */
    @Test
    void shouldCreateAndWriteFileToZip() throws IOException {
        String content = "Test content";
        String fileName = "test.txt";

        zipFileSystem.writeFileToZip(fileName, content);

        String readContent = zipFileSystem.readFileFromZip(fileName);
        assertEquals(content, readContent.trim());
    }

    /**
     * Проверяет копирование внешнего файла в ZIP-архив.
     * Создает временный файл с тестовым содержимым, копирует его в архив
     * и проверяет корректность операции.
     *
     * @throws IOException если возникла ошибка при работе с файлами
     */
    @Test
    void shouldCopyExternalFileToZip() throws IOException {
        // Создаём временный файл для теста
        Path sourceFile = tempDir.resolve("source.txt");
        String content = "External file content";
        Files.write(sourceFile, content.getBytes());

        String targetFileName = "copied.txt";
        zipFileSystem.copyFileToZip(sourceFile, targetFileName);

        assertTrue(zipFileSystem.fileExistsInZip(targetFileName));
        assertEquals(content, zipFileSystem.readFileFromZip(targetFileName).trim());
    }

    /**
     * Проверяет, что при попытке чтения несуществующего файла из архива
     * генерируется исключение IOException.
     */
    @Test
    void shouldThrowExceptionWhenReadingNonExistentFile() {
        assertThrows(IOException.class, () ->
                zipFileSystem.readFileFromZip("nonexistent.txt")
        );
    }
}
