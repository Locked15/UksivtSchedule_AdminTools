package com.uksivt;

/**
 * Класс, предоставляющий логику для одной пары.
 */
public class Lesson
{
    /**
     * Внутреннее поле, содержащее номер пары.
     */
    private final Integer number;

    /**
     * Внутреннее поле, содержащее название предмета.
     */
    private final String name;

    /**
     * Внутреннее поле, содержащее имя преподавателя.
     */
    private final String teacher;

    /**
     * Внутреннее поле, содержащее название кабинета, где будет пара.
     */
    private final String place;

    /**
     * Метод для получения значения поля "name".
     *
     * @return Значение поля "name".
     */
    public String getName()
    {
        return name;
    }

    /**
     * Метод для получения значения поля "number".
     *
     * @return Значение поля "number".
     */
    public Integer getNumber()
    {
        return number;
    }

    /**
     * Метод для получения значения поля "teacher".
     *
     * @return Значение поля "teacher".
     */
    public String getTeacher()
    {
        return teacher;
    }

    /**
     * Метод для получения значения поля "place".
     *
     * @return Значение поля "place".
     */
    public String getPlace()
    {
        return place;
    }

    /**
     * Конструктор класса.
     *
     * @param number Номер пары.
     * @param name Название предмета.
     * @param teacher Преподаватель.
     */
    public Lesson(Integer number, String name, String teacher, String place)
    {
        this.number = number;
        this.name = name;
        this.teacher = teacher;
        this.place = place;
    }
}
