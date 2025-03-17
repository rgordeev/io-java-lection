package ru.rgordeev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы с ZIP-архивами через Java NIO.2 FileSystem API.
 * Предоставляет возможности для создания ZIP-архивов, записи и чтения файлов,
 * а также копирования внешних файлов в архив.
 *
 * <p>Пример использования:</p>
 * <pre>
 * ZipFileSystemExample zip = new ZipFileSystemExample(Paths.get("example.zip"));
 * zip.writeFileToZip("hello.txt", "Hello, World!");
 * String content = zip.readFileFromZip("hello.txt");
 * </pre>
 */
public class ZipFileSystemExample {
    private static final Logger logger = LogManager.getLogger(ZipFileSystemExample.class);
    private final Path zipPath;

    /**
     * Создаёт новый экземпляр для работы с ZIP-архивом.
     *
     * @param zipPath путь к ZIP-файлу, который будет создан или изменён
     */
    public ZipFileSystemExample(Path zipPath) {
        this.zipPath = zipPath;
    }

    /**
     * Записывает текстовый файл в ZIP-архив.
     *
     * @param fileName имя файла внутри архива
     * @param content содержимое файла
     * @throws IOException если произошла ошибка при записи
     */
    public void writeFileToZip(String fileName, String content) throws IOException {
        Map<String, String> env = createZipEnvironment();

        try (FileSystem zipFs = createZipFileSystem(env)) {
            Path fileInsideZip = zipFs.getPath("/" + fileName);
            Files.write(fileInsideZip, content.getBytes());
            logger.info("Записан файл внутри ZIP: {} ({} байт)", fileInsideZip, content.length());
        }
    }

    /**
     * Читает содержимое файла из ZIP-архива.
     *
     * @param fileName имя файла внутри архива
     * @return содержимое файла в виде строки
     * @throws IOException если файл не найден или произошла ошибка чтения
     */
    public String readFileFromZip(String fileName) throws IOException {
        try (FileSystem zipFs = FileSystems.newFileSystem(zipPath, (ClassLoader) null)) {
            Path fileInsideZip = zipFs.getPath("/" + fileName);
            if (!Files.exists(fileInsideZip)) {
                throw new IOException("Файл " + fileName + " не найден в архиве");
            }
            return Files.readString(fileInsideZip);
        }
    }

    /**
     * Копирует внешний файл в ZIP-архив.
     *
     * @param sourceFile путь к исходному файлу
     * @param targetFileName имя файла внутри архива
     * @throws IOException если произошла ошибка при копировании
     */
    public void copyFileToZip(Path sourceFile, String targetFileName) throws IOException {
        Map<String, String> env = createZipEnvironment();

        try (FileSystem zipFs = createZipFileSystem(env)) {
            Path targetPath = zipFs.getPath("/" + targetFileName);
            Files.copy(sourceFile, targetPath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Файл {} скопирован в ZIP как {}", sourceFile.getFileName(), targetPath.getFileName());
        }
    }

    /**
     * Проверяет существование файла в ZIP-архиве.
     *
     * @param fileName имя файла для проверки
     * @return true если файл существует, false в противном случае
     * @throws IOException если произошла ошибка при доступе к архиву
     */
    public boolean fileExistsInZip(String fileName) throws IOException {
        try (FileSystem zipFs = FileSystems.newFileSystem(zipPath, (ClassLoader) null)) {
            return Files.exists(zipFs.getPath("/" + fileName));
        }
    }

    // Вспомогательный метод для создания ZIP FileSystem
    private FileSystem createZipFileSystem(Map<String, String> env) throws IOException {
        return FileSystems.newFileSystem(
                URI.create("jar:" + zipPath.toUri()),
                env
        );
    }

    // Вспомогательный метод для создания параметров ZIP-архива
    private Map<String, String> createZipEnvironment() {
        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        return env;
    }

    /**
     * Демонстрационный метод, показывающий основные возможности класса.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            // Создаём временную директорию для демонстрации
            Path tempDir = Files.createTempDirectory("zip_demo");
            Path zipFile = tempDir.resolve("example.zip");
            logger.info(tempDir.toAbsolutePath());

            ZipFileSystemExample zipDemo = new ZipFileSystemExample(zipFile);

            // 1. Демонстрация записи файла в архив
            logger.info("1. Записываем текстовый файл в архив...");
            zipDemo.writeFileToZip("hello.txt", "Привет, это демонстрация работы с ZIP!");

            // 2. Демонстрация чтения файла из архива
            logger.info("2. Читаем записанный файл...");
            String content = zipDemo.readFileFromZip("hello.txt");
            logger.info("Прочитанное содержимое: {}", content);

            // 3. Демонстрация копирования внешнего файла
            logger.info("3. Копируем внешний файл в архив...");
            // Создаём временный файл для демонстрации
            Path tempFile = tempDir.resolve("external.txt");
            Files.writeString(tempFile, "Это содержимое внешнего файла");
            zipDemo.copyFileToZip(tempFile, "copied_external.txt");

            // 4. Демонстрация проверки существования файлов
            logger.info("4. Проверяем наличие файлов в архиве...");
            logger.info("Файл hello.txt существует: {}", zipDemo.fileExistsInZip("hello.txt"));
            logger.info("Файл copied_external.txt существует: {}", zipDemo.fileExistsInZip("copied_external.txt"));
            logger.info("Несуществующий файл exists: {}", zipDemo.fileExistsInZip("nonexistent.txt"));

            // Очистка временных файлов
            Files.deleteIfExists(tempFile);
            Files.deleteIfExists(zipFile);
            Files.deleteIfExists(tempDir);

        } catch (IOException e) {
            logger.error("Произошла ошибка при демонстрации: ", e);
        }
    }
}
