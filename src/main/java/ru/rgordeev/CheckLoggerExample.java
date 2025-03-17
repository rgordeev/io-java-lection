package ru.rgordeev;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Демонстрационный класс для показа работы с логированием через Log4j2.
 * Класс содержит примеры использования различных уровней логирования
 * и обработки исключений.
 */
public class CheckLoggerExample {
    // Инициализация логгера для данного класса
    private static final Logger logger = LogManager.getLogger(CheckLoggerExample.class);

    /**
     * Точка входа в программу. Демонстрирует различные возможности логирования.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        // Логирование начала выполнения программы
        logger.info("Приложение запущено!");

        try {
            // Демонстрация обработки исключения
            // Намеренно вызываем ArithmeticException для примера логирования ошибок
            int result = 10 / 0;
        } catch (Exception e) {
            // Логирование ошибки вместе со стектрейсом
            logger.error("Ошибка при вычислении: ", e);
        }

        // Пример сообщения уровня DEBUG
        // Будет видно только при соответствующей настройке уровня логирования
        logger.debug("Это сообщение DEBUG; по умолчанию не будет видно при уровне INFO.");

        // Логирование завершения программы
        logger.info("Завершение программы.");
    }
}
