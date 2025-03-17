package ru.rgordeev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Класс для демонстрации и сравнения различных методов чтения файлов в Java.
 * Позволяет измерить и сравнить производительность буферизованного и небуферизованного чтения.
 *
 * <p>Класс предоставляет методы для:</p>
 * <ul>
 *   <li>Измерения времени чтения данных из потока ввода</li>
 *   <li>Сравнения производительности различных способов чтения файлов</li>
 *   <li>Тестирования на временных файлах большого размера</li>
 * </ul>
 */
public class IOOptimizationExample {
    /** Логгер для записи информации о производительности и ошибках */
    private static final Logger logger = LogManager.getLogger(IOOptimizationExample.class);

    /**
     * Измеряет время, необходимое для чтения всех данных из входного потока.
     *
     * @param in входной поток для чтения данных
     * @return время выполнения операции в наносекундах
     * @throws IllegalArgumentException если входной поток равен null
     * @throws IOException при ошибках ввода-вывода
     */
    public long measureReadTime(InputStream in) throws IOException {
        if (in == null) {
            throw new IllegalArgumentException("InputStream не может быть null");
        }

        long start = System.nanoTime();
        try {
            while (in.read() != -1) {
                // читаем все данные
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении данных", e);
            throw e;
        }
        return System.nanoTime() - start;
    }

    /**
     * Измеряет время чтения файла без использования буферизации.
     *
     * @param file путь к файлу для чтения
     * @return время выполнения операции в наносекундах
     * @throws IOException при ошибках работы с файлом
     */
    public long measureUnbufferedRead(Path file) throws IOException {
        try (InputStream in = new FileInputStream(file.toFile())) {
            long time = measureReadTime(in);
            logger.info("Время чтения без буфера: {} ms", time / 1_000_000);
            return time;
        }
    }

    /**
     * Измеряет время чтения файла с использованием буферизации через BufferedInputStream.
     *
     * @param file путь к файлу для чтения
     * @return время выполнения операции в наносекундах
     * @throws IOException при ошибках работы с файлом
     */
    public long measureBufferedRead(Path file) throws IOException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(file.toFile()))) {
            long time = measureReadTime(in);
            logger.info("Время чтения с BufferedInputStream: {} ms", time / 1_000_000);
            return time;
        }
    }

    /**
     * Сравнивает производительность чтения файла с буферизацией и без неё.
     * Выводит в лог разницу во времени выполнения.
     *
     * @param file путь к файлу для тестирования
     * @throws IOException при ошибках работы с файлом
     */
    public void compareReadPerformance(Path file) throws IOException {
        long unbufferedTime = measureUnbufferedRead(file);
        long bufferedTime = measureBufferedRead(file);

        logger.info("Разница в производительности: {} ms",
                (unbufferedTime - bufferedTime) / 1_000_000);
    }

    /**
     * Демонстрационный метод, создающий временный файл и проводящий сравнение
     * производительности различных методов чтения.
     *
     * <p>Создаёт временный файл размером примерно 5 МБ, проводит тесты
     * и затем удаляет временный файл.</p>
     *
     * @param args аргументы командной строки (не используются)
     * @throws IOException при ошибках работы с файлом
     */
    public static void main(String[] args) throws IOException {
        // Создаём временный файл для тестирования
        Path largeFile = Files.createTempFile("largeTest", ".bin");
        try (OutputStream out = new FileOutputStream(largeFile.toFile())) {
            // Записываем примерно 5 МБ данных
            byte[] data = new byte[1024];
            for (int i = 0; i < 5120; i++) {
                out.write(data);
            }
        }

        // Проводим тестирование
        IOOptimizationExample test = new IOOptimizationExample();
        test.compareReadPerformance(largeFile);

        // Удаляем временный файл
        Files.deleteIfExists(largeFile);
    }
}
