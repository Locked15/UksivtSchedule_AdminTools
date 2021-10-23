package com.uksivt;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.*;
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
     * Внутренняя константа, содержащая значение столбца, подвергнутого объединению.
     */
    private static final String UNITED_CELL_VALUE;

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

        //region Инициализация константы "UNITED_CELL_VALUE":
        UNITED_CELL_VALUE = "";
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
        Sheet currentSheet = searchTargetSheet(groupName);

        if (currentSheet != null)
        {
            //TODO: Реализовать получение нужных данных.
            ArrayList<TargetColumnBorders> indices = searchTargetColumns(currentSheet, groupName);

            return null;
        }

        else
        {
            throw new GroupDoesNotExistException("Указанная группа не найдена.");
        }
    }

    /**
     * Метод, нужный для получения таргетированного листа с которого будет считываться расписание.
     * Если группа не найдена, будет возвращено Null.
     *
     * @param groupName Имя группы для поиска страницы.
     * @return Таргетированная страница ЛИБО Null.
     */
    private Sheet searchTargetSheet(String groupName)
    {
        Iterator<Sheet> sheets = workbook.sheetIterator();

        while (sheets.hasNext())
        {
            Sheet currentSheet = sheets.next();
            Row header = currentSheet.getRow(0);
            Iterator<Cell> cells = header.cellIterator();

            while (cells.hasNext())
            {
                Cell currentCell = cells.next();
                String value = currentCell.getStringCellValue().toLowerCase(Locale.ROOT);

                if (value.equals(groupName.toLowerCase(Locale.ROOT)))
                {
                    return currentSheet;
                }
            }
        }

        //Если мы дошли до сюда, то поиски были безуспешны.
        return null;
    }

    /**
     * Метод, нужный для получения списка с номерами таргетированных столбцов.
     *
     * @param sheet Страница, на которой нужно провести поиск.
     * @param groupName Имя группы для поиска соответствующих колонн.
     *
     * @return Список с индексами нужных колонн.
     */
    private ArrayList<TargetColumnBorders> searchTargetColumns(Sheet sheet, String groupName)
    {
        Cell previousCell;
        Integer i = 0;
        Boolean listen = false;
        Cell currentCell = null;
        Row header = sheet.getRow(0);
        Iterator<Cell> cells = header.cellIterator();
        ArrayList<TargetColumnBorders> indices = new ArrayList<>(2);

        try
        {
            currentCell = cells.next();
        }

        catch (NoSuchElementException exc)
        {
            //Если элементов в коллекции нет, мы попадем сюда.
            //Чисто теоретически, это возможно...
        }

        while (cells.hasNext())
        {
            previousCell = currentCell;
            currentCell = cells.next();

            String value = currentCell.getStringCellValue().toLowerCase(Locale.ROOT);

            if (value.equals(groupName.toLowerCase(Locale.ROOT)))
            {
                indices.add(new TargetColumnBorders());
                indices.get(i).leftBorderIndex = previousCell.getColumnIndex();

                listen = true;
            }

            else if (listen && (currentCell.getCellStyle().getWrapText() ||
            (!value.equals(UNITED_CELL_VALUE) && !value.equals(EMPTY_CELL_VALUE))))
            {
                indices.get(i).rightBorderIndex = currentCell.getColumnIndex();

                listen = false;
                i++;
            }
        }

        //Скорее всего, цикл закончится до того, как мы пропишем последнее значение:
        if (indices.get(indices.size() - 1).rightBorderIndex == null)
        {
            //Очередное доказательство того, что "Integer" и "int" — это не одно и то же.
            indices.get(indices.size() - 1).rightBorderIndex = (Integer)(int)header.getLastCellNum();
        }

        return indices;
    }
}

class TargetColumnBorders
{
    /**
     * Поле, содержащее индекс левой границы столбца.
     *
     * Индекс границы является индексом границы другого столбца, примыкающего к таргетированному.
     */
    public Integer leftBorderIndex;

    /**
     * Поле, содержащее индекс правой границы столбца.
     *
     * Индекс границы является индексом границы другого столбца, примыкающего к таргетированному.
     */
    public Integer rightBorderIndex;

    /**
     * Конструктор класса.
     */
    public TargetColumnBorders()
    {

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
