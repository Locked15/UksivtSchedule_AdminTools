package com.uksivt.parser_elements;

import java.util.ArrayList;

/**
 * Класс, представляющий определение замен за месяц.
 */
public class MonthChanges
{
    //region Область: Поля.
    /**
     * Поле, содержащее текущий месяц.
     */
    String currentMonth;

    /**
     * Поле, содержащее список с заменами за месяц.
     */
    ArrayList<ChangeElement> changes;
    //endregion

    //region Область: Get-Свойства.
    /**
     * Метод для получения значения поля "currentMonth".
     *
     * @return Текущее значение поля.
     */
    public String getCurrentMonth()
    {
        return currentMonth;
    }

    /**
     * Метод для получения значения поля "changes".
     *
     * @return Текущее значение поля.
     */
    public ArrayList<ChangeElement> getChanges()
    {
        return changes;
    }
    //endregion

    //region Область: Set-Свойства.
    /**
     * Метод для установки значения поля "currentMonth".
     *
     * @param currentMonth Новое значение поля.
     */
    public void setCurrentMonth(String currentMonth)
    {
        this.currentMonth = currentMonth;
    }

    /**
     * Метод для установки значения поля "changes".
     *
     * @param changes Новое значение поля.
     */
    public void setChanges(ArrayList<ChangeElement> changes)
    {
        this.changes = changes;
    }
    //endregion

    //region Область: Конструкторы.
    /**
     * Конструктор по умолчанию.
     */
    public MonthChanges()
    {

    }

    /**
     * Конструктор класса.
     *
     * @param currentMonth Текущий месяц.
     * @param changes Список с заменами по дням.
     */
    public MonthChanges(String currentMonth, ArrayList<ChangeElement> changes)
    {
        this.currentMonth = currentMonth;
        this.changes = changes;
    }
    //endregion

    //region Область: Методы.
    /**
     * Метод для получения строкового представления объекта.
     *
     * @return Строковое представление.
     */
    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("\nНовый месяц: \n" +
        "CurrentMonth = " + currentMonth + ";\n" +
        "Changes:" +
        "\n{");

        for (ChangeElement change : changes)
        {
            toReturn.append(change.toString("\t"));
        }

        toReturn.append("}");

        return toReturn.toString();
    }
    //endregion
}
