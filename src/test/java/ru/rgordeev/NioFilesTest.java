package ru.rgordeev;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class NioFilesTest {

    @Test
    void testWriteReadCopyMoveDelete() throws IOException {
        Path tempDir = Files.createTempDirectory("nioTestDir");
        Path original = tempDir.resolve("original.txt");
        Path copy = tempDir.resolve("copy.txt");
        Path moved = tempDir.resolve("moved.txt");

        String text = "NIO Files Test";
        // Запись строки в файл
        Files.writeString(original, text, StandardCharsets.UTF_8);
        assertTrue(Files.exists(original));
        // Чтение обратно
        String readBack = Files.readString(original, StandardCharsets.UTF_8);
        assertEquals(text, readBack);

        // Копирование файла
        Files.copy(original, copy, StandardCopyOption.REPLACE_EXISTING);
        assertTrue(Files.exists(copy));
        assertEquals(text, Files.readString(copy, StandardCharsets.UTF_8));

        // Перемещение (переименование) файла copy -> moved
        Files.move(copy, moved, StandardCopyOption.REPLACE_EXISTING);
        assertFalse(Files.exists(copy));
        assertTrue(Files.exists(moved));
        assertEquals(text, Files.readString(moved, StandardCharsets.UTF_8));

        // Удаление файлов и каталога
        Files.delete(moved);
        Files.delete(original);
        Files.delete(tempDir);
        assertFalse(Files.exists(tempDir));
    }
}
