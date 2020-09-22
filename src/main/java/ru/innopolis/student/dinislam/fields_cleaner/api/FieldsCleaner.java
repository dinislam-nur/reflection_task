package ru.innopolis.student.dinislam.fields_cleaner.api;

import java.util.Set;

/**
 * Интерфейс очистки состояния объекта по заданным полям.
 */
public interface FieldsCleaner {

    /**
     * Метод очистки и вывода состояния объекта.
     * Также в случае, когда в качестве объекта передается Map<String, ?>, очищаются и выводятся
     * соответствующие записи и значения по ключам из входных Set-ов.
     *
     * @param obj             - объект (либо Map) который будет обрабатываться по полям.
     * @param fieldsToCleanup - Set полей (либо ключей), состояния которых необходимо обнулить.
     * @param fieldsToOutput  - Set полей (либо ключей), состояние которых необходимо вывести в консоль.
     * @throws IllegalArgumentException - выбрасывается в случае, если в объекте не
     *                                  имеется поля соответсвуещему полю из Set-ов. Либо в случае, ключи Map
     *                                  не типа String.
     */
    void cleanup(Object obj, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) throws IllegalArgumentException;
}
