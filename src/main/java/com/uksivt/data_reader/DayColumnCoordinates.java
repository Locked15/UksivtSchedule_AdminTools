package com.uksivt.data_reader;

import com.uksivt.schedule_elements.Days;
import org.apache.poi.ss.util.CellAddress;

import java.util.ArrayList;

/**
 * Класс, представляющий логику для получения координат ячеек с днями.
 */
class DayColumnCoordinates
{
    /**
     * Поле, содержащее адрес ячейки.
     */
    public CellAddress Coordinates;

    /**
     * Поле, содержащее значение дня, находящееся в данной ячейке.
     */
    public Days CurrentDay;

    /**
     * Конструктор класса.
     *
     * @param x Координата 'x' нужной ячейки.
     * @param y Координата 'y' нужной ячейки.
     * @param day День, содержащийся в указанной ячейке.
     */
    public DayColumnCoordinates(Integer x, Integer y, Days day)
    {
        Coordinates = new CellAddress(y, x);
        CurrentDay = day;
    }

    /**
     * Метод для получения адреса ячейки с указанным днем.
     *
     * @param day Значение ячейки, по которой нужно получить адрес.
     * @param coordinates Список "DayColumnCoordinates", содержащий все координаты.
     *
     * @return Адрес нужной ячейки.
     */
    public static CellAddress getAddressByDay(Days day, ArrayList<DayColumnCoordinates> coordinates)
    {
        for (DayColumnCoordinates coordinate : coordinates)
        {
            if (day.equals(coordinate.CurrentDay))
            {
                return coordinate.Coordinates;
            }
        }

        //Если мы дошли сюда, значит поиск ничего не нашел.
        return null;
    }
}
