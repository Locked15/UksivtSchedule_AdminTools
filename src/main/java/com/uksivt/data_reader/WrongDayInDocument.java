package com.uksivt.data_reader;

/**
 * Класс, определяющие исключение попытки обработать документ с заменами с неправильным днем.
 */
class WrongDayInDocument extends Exception
{
    /**
     * Конструктор класса.
     *
     * @param message Сообщение исключения.
     */
    public WrongDayInDocument(String message)
    {
        //Вызываем конструктор базового класса:
        super(message);
    }
}
