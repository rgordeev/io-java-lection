package ru.rgordeev;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

class FilePerformanceTestTest {
    private static Path testFile;
    private IOOptimizationExample performanceTest;

    @BeforeAll
    static void setUp() throws IOException {
        testFile = Files.createTempFile("test", ".bin");
        try (OutputStream out = new FileOutputStream(testFile.toFile())) {
            byte[] data = new byte[1024];
            for (int i = 0; i < 100; i++) { // Создаем файл поменьше для тестов
                out.write(data);
            }
        }
    }

    @BeforeEach
    void init() {
        performanceTest = new IOOptimizationExample();
    }

    @AfterAll
    static void tearDown() throws IOException {
        Files.deleteIfExists(testFile);
    }

    @Test
    void shouldMeasureReadTime() throws IOException {
        try (InputStream in = new FileInputStream(testFile.toFile())) {
            long time = performanceTest.measureReadTime(in);
            Assertions.assertTrue(time > 0);
        }
    }

    @Test
    void bufferedInputShouldBeFasterThanUnbuffered() throws IOException {
        long unbufferedTime = performanceTest.measureUnbufferedRead(testFile);
        long bufferedTime = performanceTest.measureBufferedRead(testFile);

        Assertions.assertTrue(bufferedTime <= unbufferedTime,
                "Буферизованное чтение должно быть быстрее небуферизованного");
    }

    @Test
    void shouldThrowExceptionOnNullInput() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> performanceTest.measureReadTime(null));
    }
}
