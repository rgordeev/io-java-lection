package ru.rgordeev;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Утилитарный класс для чтения бинарных и текстовых файлов.
 * Предоставляет методы для безопасного чтения файлов с использованием try-with-resources
 * и логированием процесса чтения.
 */
public class FileReaderExample {
    // Логгер для записи информации о процессе чтения файлов
    private static final Logger logger = LogManager.getLogger(FileReaderExample.class);

    /**
     * Точка входа в программу. Демонстрирует использование методов чтения файлов.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        FileReaderExample reader = new FileReaderExample();

        try {
            // Демонстрация чтения бинарного файла
            List<Byte> binaryData = reader.readBinaryFile("files/data.bin");
            logger.info("Прочитано {} байт из data.bin", binaryData.size());

            // Демонстрация чтения текстового файла
            String textContent = reader.readTextFile("files/text.txt");
            logger.info("Прочитано {} символов из text.txt", textContent.length());

        } catch (IOException e) {
            logger.error("Произошла ошибка при чтении файлов", e);
        }
    }

    /**
     * Читает бинарный файл и возвращает его содержимое в виде списка байтов.
     *
     * @param filePath путь к файлу для чтения
     * @return список байтов, содержащихся в файле
     * @throws IOException если произошла ошибка при чтении файла
     */
    public List<Byte> readBinaryFile(String filePath) throws IOException {
        List<Byte> bytes = new ArrayList<>();
        try (InputStream in = new FileInputStream(filePath)) {
            logger.info("Открыт {} для чтения байтов", filePath);

            int byteValue;
            // Читаем файл побайтово, пока не достигнем конца файла (-1)
            while ((byteValue = in.read()) != -1) {
                bytes.add((byte) byteValue);
                logger.debug("Прочитан байт: {}", byteValue);
            }

            logger.info("Чтение {} завершено", filePath);
        }
        return bytes;
    }

    /**
     * Читает текстовый файл и возвращает его содержимое в виде строки.
     * Использует кодировку UTF-8 для чтения файла.
     *
     * @param filePath путь к файлу для чтения
     * @return содержимое файла в виде строки
     * @throws IOException если произошла ошибка при чтении файла
     */
    public String readTextFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (Reader reader = new java.io.FileReader(filePath, StandardCharsets.UTF_8)) {
            logger.info("Открыт {} для чтения символов (UTF-8)", filePath);

            int charValue;
            // Читаем файл посимвольно, пока не достигнем конца файла (-1)
            while ((charValue = reader.read()) != -1) {
                content.append((char) charValue);
                logger.debug("Прочитан символ: {}", (char) charValue);
            }

            logger.info("Чтение {} завершено", filePath);
        }
        return content.toString();
    }
}
