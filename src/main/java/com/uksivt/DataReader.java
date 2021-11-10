package com.uksivt;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
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
     * @exception GroupDoesNotExistException Ошибка, возникающая при отсутствии указанной группы в расписании.
     * @exception NullPointerException Ошибка выполнения связанная с указанием на "null".
     */
    public WeekSchedule getWeekSchedule(String groupName) throws NullPointerException, GroupDoesNotExistException
    {
        WeekSchedule toReturn;
        Sheet currentSheet = searchTargetSheet(groupName);

        if (currentSheet != null)
        {
            //region Подобласть: Прочие переменные.
            Integer lessonNumber = 0;
            Integer firstCycleIterator = 0;
            Days currentDay = null;
            //endregion

            //region Подобласть: Списки с расписаниями.
            ArrayList<Lesson> lessons;
            ArrayList<DaySchedule> schedules = new ArrayList<>(6);
            //endregion

            //region Подобласть: Итераторы и переменные итераторов.
            Integer i;
            Integer column;
            Iterator<Row> rows;
            //endregion

            //region Подобласть: Списки с координатами.
            ArrayList<TargetColumnBorders> indices = searchTargetColumns(currentSheet, groupName);
            ArrayList<DayColumnCoordinates> daysIndices = searchDaysCoordinates(currentSheet);
            //endregion

            //region Подобласть: Переменные считывания пар.
            Lesson currentLesson;
            Boolean lessonPlaceAdded = false;
            Boolean lessonNameIsAdded = false;
            //endregion

            for (column = 0; column < 2; column++)
            {
                //Определяем (в первой итерации) и сбрасываем (во второй), ...
                //... значения переменных:
                i = 0;
                rows = currentSheet.rowIterator();
                currentLesson = new Lesson();
                lessons = new ArrayList<>(6);

                while (rows.hasNext())
                {
                    Row currentRow = rows.next();
                    Iterator<Cell> cells = currentRow.cellIterator();

                    //Пропускаем область с заголовком.
                    if (currentRow.getRowNum() < daysIndices.get(0).Coordinates.getRow())
                    {
                        continue;
                    }

                    //Условие для смены текущего дня.
                    if (currentRow.getRowNum() == DayColumnCoordinates.getAddressByDay(Days.getValueByIndex(i), daysIndices).getRow())
                    {
                        if (i > 0)
                        {
                            //Добавляем последнюю пару:
                            lessons.add(currentLesson);

                            //Добавляем получение расписание на день в список, ...
                            //... после чего очищаем список с парами на день:
                            schedules.add(new DaySchedule(currentDay, lessons));
                            lessons = new ArrayList<>();
                        }

                        currentLesson = new Lesson();
                        currentDay = Days.getValueByIndex(i + column * 3);
                        firstCycleIterator = 0;
                        lessonNumber = 0;

                        currentLesson.setNumber(lessonNumber);

                        //Так как "Условие для смены номера пары" НЕ выполняется, если ...
                        //... пары следующего дня идут с утра, то и значение переменной не сбрасывается, ...
                        //... так что мы сбрасываем его вручную:
                        lessonNameIsAdded = false;

                        i++;
                    }

                    //Условие для смены номера пары.
                    if (firstCycleIterator != 0 && firstCycleIterator % 4 == 0)
                    {
                        lessons.add(currentLesson);

                        lessonNumber++;

                        lessonPlaceAdded = false;
                        lessonNameIsAdded = false;
                        currentLesson = new Lesson();
                        currentLesson.setNumber(lessonNumber);
                    }

                    //Итерируем конкретные ячейки:
                    while (cells.hasNext())
                    {
                        Cell currentCell = cells.next();

                        //Пропускаем первые ячейки:
                        if (indices.get(column).LeftBorderIndex >= currentCell.getColumnIndex())
                        {
                            continue;
                        }

                        //Прерываем цикл, когда итератор выходит за пределы ячеек с расписанием:
                        else if (indices.get(column).RightBorderIndex <= currentCell.getColumnIndex())
                        {
                            break;
                        }

                        //В оригинальном документе кабинеты могут быть указаны в разных ячейках, ...
                        //... к примеру, на одной строке с преподавателем ИЛИ с названием предмета.
                        try
                        {
                            String cellValue = currentCell.getStringCellValue();
                            CellAddress curCell = currentCell.getAddress();

                            if (!cellValue.equals(EMPTY_CELL_VALUE) && !cellValue.equals(UNITED_CELL_VALUE))
                            {
                                if (currentCell.getColumnIndex() + 1 == indices.get(column).RightBorderIndex)
                                {
                                    //Однако место проведения пары может быть нестандартным, ...
                                    //... к примеру, "321А" и тогда тип будет уже строковым.
                                    currentLesson.setPlace(cellValue);

                                    lessonPlaceAdded = true;
                                }

                                else if (lessonNameIsAdded)
                                {
                                    currentLesson.setTeacher(cellValue);
                                }

                                else
                                {
                                    currentLesson.setName(cellValue);

                                    lessonNameIsAdded = true;
                                }
                            }
                        }

                        //Так как кабинет - числовой тип, мы обязаны обработать исключение, ...
                        //... ведь остальные ячейки имеют строковый тип.
                        catch (IllegalStateException exception)
                        {
                            //Я не уверен, но на всякий случай добавлена такая проверка:
                            if (!lessonPlaceAdded)
                            {
                                Integer temp = ((int) currentCell.getNumericCellValue());
                                String cellCabinetNumber = temp.toString();

                                currentLesson.setPlace(cellCabinetNumber);
                            }
                        }
                    }

                    firstCycleIterator++;
                }

                //Вручную добавляем последнюю пару, а затем и день:
                lessons.add(currentLesson);
                schedules.add(new DaySchedule(currentDay, lessons));
            }

            toReturn = new WeekSchedule(groupName, schedules);
        }

        else
        {
            throw new GroupDoesNotExistException("Указанная группа не найдена.");
        }

        return toReturn;
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
     *
     * @throws NullPointerException Ошибка выполнения связанная с указанием на "null".
     */
    private ArrayList<TargetColumnBorders> searchTargetColumns(Sheet sheet, String groupName) throws NullPointerException
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

                if (previousCell == null)
                {
                    throw new NullPointerException("Неопознанная ошибка.\n\"PreviousCell\" был \"null\".");
                }

                indices.get(i).LeftBorderIndex = previousCell.getColumnIndex();

                listen = true;
            }

            else if (listen && (currentCell.getCellStyle().getWrapText() ||
            (!value.equals(UNITED_CELL_VALUE) && !value.equals(EMPTY_CELL_VALUE))))
            {
                indices.get(i).RightBorderIndex = currentCell.getColumnIndex();

                listen = false;
                i++;
            }
        }

        //Скорее всего, цикл закончится до того, как мы пропишем последнее значение:
        if (indices.get(indices.size() - 1).RightBorderIndex == null)
        {
            //Очередное доказательство того, что "Integer" и "int" — это не одно и то же.
            indices.get(indices.size() - 1).RightBorderIndex = (Integer)(int)header.getLastCellNum();
        }

        return indices;
    }

    /**
     * Метод, нужный для получения координат ячеек с днями.
     *
     * @param sheet Страница для сканирования.
     *
     * @return Список с координатами ячеек дней.
     */
    private ArrayList<DayColumnCoordinates> searchDaysCoordinates(Sheet sheet)
    {
        Iterator<Row> rows = sheet.rowIterator();
        ArrayList<DayColumnCoordinates> toReturn = new ArrayList<>(1);

        while (rows.hasNext())
        {
            Row currentRow = rows.next();
            Iterator<Cell> cells = currentRow.cellIterator();

            while (cells.hasNext())
            {
                String value;
                Days parsedValue;
                Cell currentCell = cells.next();

                //Мы проходим по всем ячейкам. Однако некоторые из них не строковые.
                try
                {
                    value = currentCell.getStringCellValue();
                    parsedValue = Days.fromString(value);
                }

                //Получение строки из НЕ-строковой ячейки вызовет исключение.
                //Мы его перехватываем.
                catch (IllegalStateException exception)
                {
                    continue;
                }

                if (parsedValue != null)
                {
                    toReturn.add(new DayColumnCoordinates(currentCell.getAddress().getColumn(),
                    currentCell.getAddress().getRow(), parsedValue));
                }
            }
        }

        return toReturn;
    }
}

/**
 * Класс, представляющий логику для границ таргетированной колонны.
 */
class TargetColumnBorders
{
    /**
     * Поле, содержащее индекс левой границы столбца.
     *
     * Индекс границы является индексом границы другого столбца, примыкающего к таргетированному.
     */
    public Integer LeftBorderIndex;

    /**
     * Поле, содержащее индекс правой границы столбца.
     *
     * Индекс границы является индексом границы другого столбца, примыкающего к таргетированному.
     */
    public Integer RightBorderIndex;

    /**
     * Конструктор класса.
     */
    public TargetColumnBorders()
    {

    }
}

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
