package com.uksivt;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Класс, предоставляющий логику для получения данных из Excel-файла.
 */
public class DataReader
{
    /**
     * Внутреннее поле, содержащее объект, нужный для работы с xlsx-файлом.
     */
    private XSSFWorkbook workbook;

    /**
     * Внутренняя константа, содержащая путь к файлу с расписанием.
     */
    private static final String PATH_TO_FILE;

    /**
     * Внутренняя константа, содержащая значение якобы "пустой" ячейки.
     * На самом деле в таких ячейках содержится 29 пробелов.
     */
    private static final String EMPTY_CELL_VALUE;

    /**
     * Конструктор класса.
     */
    public DataReader()
    {
        try
        {
            FileInputStream stream = new FileInputStream(PATH_TO_FILE);

            workbook = new XSSFWorkbook(stream);
        }

        catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    //Статический конструктор класса.
    static
    {
        //region Инициализация константы "PATH_TO_FILE":
        PATH_TO_FILE = "C:\\Users\\Земфира\\Desktop\\Prog.xlsx";
        //endregion

        //region Инициализация константы "EMPTY_CELL_VALUE":
        EMPTY_CELL_VALUE = " ".repeat(29);
        //endregion
    }

    /**
     * Метод для получения списка групп.
     *
     * @return Список, содержащий группы студентов.
     */
    public ArrayList<String> getGroups()
    {
        Iterator<Sheet> sheets = workbook.sheetIterator();
        ArrayList<String> groups = new ArrayList<>(1);

        while (sheets.hasNext())
        {
            Sheet currentSheet = sheets.next();
            Row header = currentSheet.getRow(0);
            Iterator<Cell> cells = header.cellIterator();

            while (cells.hasNext())
            {
                Cell currentCell = cells.next();
                CellType cellType = currentCell.getCellType();

                if (cellType == CellType.STRING)
                {
                    String value = currentCell.getStringCellValue();

                    if (!value.equals(EMPTY_CELL_VALUE) && !groups.contains(value))
                    {
                        groups.add(value);
                    }
                }
            }
        }

        return groups;
    }

    /**
     * Метод, нужный для получения расписания у указанной группы.
     *
     * @param groupName Название группы для получения расписания.
     *
     * @return Расписание на неделю для указанной группы.
     *
     * @exception GroupDoesNotExistException — При отсутствии указанной группы в расписании.
     */
    public ArrayList<DaySchedule> getWeekSchedule(String groupName) throws GroupDoesNotExistException
    {
        //TODO: Надо разобраться с ЭТИМ...
        return null;
    }
}

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
