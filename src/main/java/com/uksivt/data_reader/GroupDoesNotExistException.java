package com.uksivt.data_reader;

/**
 * Класс, определяющий исключение отсутствия указанной группы.
 */
class GroupDoesNotExistException extends Exception
{
    /**
     * Конструктор класса.
     *
     * @param message Сообщение исключения.
     */
    public GroupDoesNotExistException(String message)
    {
        //Вызываем конструктор базового класса:
        super(message);
    }
}
