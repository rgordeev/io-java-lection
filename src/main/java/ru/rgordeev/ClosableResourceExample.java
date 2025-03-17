package ru.rgordeev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * ClosableResourceExample представляет собой простой ресурс, реализующий интерфейс AutoCloseable.
 * Класс демонстрирует базовое управление ресурсами с возможностью закрытия.
 *
 * @author Your Name
 * @version 1.0
 */
public class ClosableResourceExample implements AutoCloseable {
    // Логгер для записи информации о состоянии ресурса
    private static final Logger logger = LogManager.getLogger(ClosableResourceExample.class);

    // Флаг, указывающий, закрыт ли ресурс
    private boolean closed;

    /**
     * Конструктор создает новый экземпляр ресурса.
     * При создании ресурс считается открытым.
     */
    public ClosableResourceExample() {
        this.closed = false;
        logger.info("ClosableResourceExample: ресурс открыт (конструктор)");
    }

    /**
     * Выполняет определенную операцию с ресурсом.
     *
     * @throws IllegalStateException если ресурс уже закрыт
     */
    public void doSomething() {
        if (closed) {
            logger.error("Попытка использовать закрытый ресурс");
            throw new IllegalStateException("Ресурс уже закрыт!");
        }
        logger.info("ClosableResourceExample: выполнение операции");
    }

    /**
     * Проверяет, закрыт ли ресурс.
     *
     * @return true если ресурс закрыт, false в противном случае
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Закрывает ресурс. После закрытия ресурс больше нельзя использовать.
     */
    @Override
    public void close() {
        if (!closed) {
            closed = true;
            logger.info("ClosableResourceExample: ресурс закрыт (close() вызван)");
        } else {
            throw new RuntimeException("Hello!");
        }
    }

    /**
     * Демонстрационный метод main для показа использования класса ClosableResourceExample.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Демонстрация использования try-with-resources
        try (ClosableResourceExample resource = new ClosableResourceExample()) {
            resource.doSomething();
            System.out.println("Ресурс используется");
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }

        // Демонстрация попытки использования закрытого ресурса
//        ClosableResourceExample resource = new ClosableResourceExample();
//        resource.close();
        try (ClosableResourceExample resource = new ClosableResourceExample()) {
            resource.close();
            resource.doSomething(); // Вызовет IllegalStateException
        } catch (IllegalStateException e) {
            System.err.println("Ожидаемая ошибка: " + e.getMessage() + Arrays.stream(e.getSuppressed()).findFirst().get().getMessage());
        }
    }
}
