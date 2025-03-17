package ru.rgordeev;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Демонстрационный класс для работы с файловой системой используя Java NIO.2 API.
 * Показывает основные операции с файлами и директориями: создание, чтение,
 * запись, копирование, перемещение и удаление.
 *
 * <p>Класс демонстрирует следующие возможности:
 * <ul>
 *   <li>Создание и управление директориями
 *   <li>Операции с файлами (создание, чтение, запись)
 *   <li>Копирование и перемещение файлов
 *   <li>Работа с атрибутами файлов
 *   <li>Обход содержимого директорий
 * </ul>
 *
 * @author rgordeev
 */
public class NioFileExample {
    // Инициализация логгера для класса
    private static final Logger logger = LogManager.getLogger(NioFileExample.class);

    /**
     * Основной метод, демонстрирующий различные операции с файловой системой.
     *
     * @param args аргументы командной строки (не используются)
     * @throws IOException если происходит ошибка ввода-вывода при работе с файлами
     */
    public static void main(String[] args) throws IOException {
        // ЧАСТЬ 1: Работа с путями
        // Создаем путь к директории и файлу
        Path dir = Paths.get("example_dir");
        Path file = dir.resolve("notes.txt");  // Комбинируем пути для получения полного пути к файлу

        // ЧАСТЬ 2: Создание директории
        // Проверяем существование директории и создаем её при необходимости
        if (Files.notExists(dir)) {
            Files.createDirectories(dir);
            logger.info("Каталог {} создан.", dir.toAbsolutePath());
        } else {
            logger.info("Каталог {} уже существует.", dir.toAbsolutePath());
        }

        // ЧАСТЬ 3: Операции записи
        // Записываем текст в файл используя UTF-8 кодировку
        String text = "Hello NIO Files!";
        Files.writeString(file, text, StandardCharsets.UTF_8);
        logger.info("В файл {} записана строка: {}", file, text);

        // ЧАСТЬ 4: Операции чтения
        // Демонстрация различных способов чтения файла
        // Способ 1: Чтение всего содержимого как строки
        String content = Files.readString(file, StandardCharsets.UTF_8);
        logger.info("Прямое чтение файла как строки: {}", content);

        // Способ 2: Чтение файла построчно
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        logger.info("Чтение файла построчно, всего строк: {}", lines.size());
        for (String line : lines) {
            logger.info("Строка: {}", line);
        }

        // ЧАСТЬ 5: Копирование файлов
        // Демонстрация копирования с заменой существующего файла
        Path copyPath = dir.resolve("notes_copy.txt");
        Files.copy(file, copyPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("Файл скопирован в {}", copyPath.getFileName());

        // ЧАСТЬ 6: Перемещение/переименование файлов
        Path movedPath = dir.resolve("notes_renamed.txt");
        Files.move(copyPath, movedPath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("Файл {} перемещен/переименован в {}",
                copyPath.getFileName(), movedPath.getFileName());

        // ЧАСТЬ 7: Работа с атрибутами файлов
        // Получение размера и времени последнего изменения
        long size = Files.size(movedPath);
        FileTime modifiedTime = Files.getLastModifiedTime(movedPath);
        logger.info("Файл {}: размер = {} байт, послед. изм. = {}",
                movedPath.getFileName(), size, modifiedTime);

        // ЧАСТЬ 8: Обход директории
        // Использование DirectoryStream для эффективного перебора содержимого
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            logger.info("Содержимое каталога {}:", dir);
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    logger.info("  [DIR]  {}", entry.getFileName());
                } else {
                    logger.info("  [FILE] {} ({} bytes)",
                            entry.getFileName(), Files.size(entry));
                }
            }
        }

        // ЧАСТЬ 9: Очистка - удаление созданных файлов и директории
        // Демонстрация различных методов удаления
        Files.delete(movedPath);
        logger.info("Файл {} удален.", movedPath.getFileName());
        Files.deleteIfExists(file);
        logger.info("Исходный файл {} удален.", file.getFileName());
        Files.delete(dir);
        logger.info("Каталог {} удален.", dir);
    }
}
