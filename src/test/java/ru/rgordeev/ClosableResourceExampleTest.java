package ru.rgordeev;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Набор тестов для проверки функциональности класса ClosableResourceExample.
 * Тесты охватывают основные сценарии использования ресурса:
 * - создание нового ресурса
 * - закрытие ресурса
 * - выполнение операций с открытым и закрытым ресурсом
 * - повторное закрытие ресурса
 */
class ClosableResourceExampleTest {

    /**
     * Проверяет, что newly созданный ресурс находится в открытом состоянии.
     *
     * Ожидаемое поведение:
     * - После создания ресурс должен быть в открытом состоянии (не закрыт)
     */
    @Test
    void whenCreated_thenResourceIsNotClosed() {
        ClosableResourceExample resource = new ClosableResourceExample();
        assertFalse(resource.isClosed());
    }

    /**
     * Проверяет корректность закрытия ресурса.
     *
     * Ожидаемое поведение:
     * - После вызова метода close() ресурс должен перейти в закрытое состояние
     */
    @Test
    void whenClosed_thenResourceIsClosed() {
        ClosableResourceExample resource = new ClosableResourceExample();
        resource.close();
        assertTrue(resource.isClosed());
    }

    /**
     * Проверяет возможность выполнения операций с открытым ресурсом.
     *
     * Ожидаемое поведение:
     * - Метод doSomething() должен выполняться без исключений на открытом ресурсе
     */
    @Test
    void whenResourceOpen_thenCanDoSomething() {
        ClosableResourceExample resource = new ClosableResourceExample();
        assertDoesNotThrow(() -> resource.doSomething());
    }

    /**
     * Проверяет невозможность выполнения операций с закрытым ресурсом.
     *
     * Ожидаемое поведение:
     * - При попытке вызвать doSomething() на закрытом ресурсе
     *   должно быть выброшено IllegalStateException
     */
    @Test
    void whenResourceClosed_thenCannotDoSomething() {
        ClosableResourceExample resource = new ClosableResourceExample();
        resource.close();
        assertThrows(IllegalStateException.class, resource::doSomething);
    }

    /**
     * Проверяет безопасность повторного закрытия ресурса.
     *
     * Ожидаемое поведение:
     * - Повторный вызов close() не должен вызывать исключений
     * - Реализует идемпотентность операции закрытия
     */
    @Test
    void whenClosingTwice_thenNoException() {
        ClosableResourceExample resource = new ClosableResourceExample();
        resource.close();
        assertDoesNotThrow(resource::close);
    }
}
