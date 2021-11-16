package com.uksivt.data_reader;

import com.uksivt.schedule_elements.DaySchedule;
import com.uksivt.schedule_elements.Days;
import com.uksivt.schedule_elements.Lesson;
import com.uksivt.schedule_elements.WeekSchedule;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.util.*;

/**
 * Класс, предоставляющий логику для получения данных из Excel-файла.
 */
public class DataReader
{
    //region Область: Поля работы с документами.
    /**
     * Внутреннее поле, содержащее объект, нужный для работы с xlsx-файлом.
     */
    private XSSFWorkbook workbook;

    /**
     * Внутреннее поле, содержащее объект, нужный для работы с docx-файлом.
     */
    private XWPFDocument document;
    //endregion

    //region Область: Константы.
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
     * Внутренняя константа, содержащая значение пустой ячейки таблицы Word.
     * <br>
     * В отличие от Excel, пустые ячейки в Word действительно пустые.
     */
    private static final String WORD_TABLE_EMPTY_CELL_VALUE;
    //endregion

    //region Область: Конструкторы класса.

    /**
     * Конструктор класса.
     */
    public DataReader(String pathToFile, String pathToChangingFile)
    {
        try
        {
            FileInputStream excelDocument = new FileInputStream(pathToFile);
            FileInputStream wordDocument = new FileInputStream(pathToChangingFile);

            workbook = new XSSFWorkbook(excelDocument);
            document = new XWPFDocument(wordDocument);
        }

        catch (Exception ex)
        {
            ex.fillInStackTrace();
        }
    }

    //Статический конструктор класса.
    static
    {
        //region Инициализация константы "EMPTY_CELL_VALUE":
        EMPTY_CELL_VALUE = " ".repeat(29);
        //endregion

        //region Инициализация константы "WORD_TABLE_EMPTY_CELL_VALUE":
        WORD_TABLE_EMPTY_CELL_VALUE = "";
        //endregion

        //region Инициализация константы "UNITED_CELL_VALUE":
        UNITED_CELL_VALUE = "";
        //endregion
    }
    //endregion

    //region Область: Обработка документа Excel.

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
     * @throws GroupDoesNotExistException Ошибка, возникающая при отсутствии указанной группы в расписании.
     * @throws NullPointerException Ошибка выполнения связанная с указанием на "null".
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
    //endregion

    //region Область: Обработка документа Word.

    /**
     * Метод для получения расписания на день с учетом замен.
     *
     * @return Расписание на день, учитывающее замены.
     *
     * @throws WrongDayInDocument При обработке документа обнаружилось несоответствие дней.
     */
    public DaySchedule getDayScheduleWithChanges(WeekSchedule schedule, Days day) throws WrongDayInDocument
    {
        //region Область: Переменные оригинальных значений.
        String groupName = schedule.getGroupName();
        DaySchedule originalSchedule = schedule.getDays().get(Days.getIndexByValue(day));
        //endregion

        //region Подобласть: Переменные для проверки групп "На Практику".
        String onPractiseString = "";
        StringBuilder technicalString = new StringBuilder();
        //endregion

        //region Подобласть: Переменные для составления измененного расписания.
        Integer cellNumber;
        String possibleNumbs;
        Lesson currentLesson;
        Boolean cycleStopper = false;
        Boolean changesListen = false;
        Boolean absoluteChanges = false;
        ArrayList<Lesson> newLessons = new ArrayList<>(1);
        //endregion

        //region Подобласть: Список с параграфами.
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        //endregion

        //Самым последним параграфом идет имя исполнителя, поэтому его игнорируем:
        for (int i = 0; i < paragraphs.size() - 1; i++)
        {
            String text;

            //Пятым параграфом идет название дня и недели. Проверяем корректность:
            if (i == 5)
            {
                text = paragraphs.get(i).getText().toLowerCase(Locale.ROOT);

                if (!text.contains(Days.toString(day).toLowerCase(Locale.ROOT)))
                {
                    throw new WrongDayInDocument("День отправленного расписания и документа с заменами не совпадают.");
                }
            }

            //Первыми идут параграфы с инициалами администрации, игнорируем:
            else if (i > 5)
            {
                text = paragraphs.get(i).getText();

                technicalString.append(text);
            }
        }

        //Порой бывает так, что замен нет, а вот перераспределение кабинетов - да, ...
        //... поэтому такой случай надо обработать:
        if (technicalString.toString().contains("– на практике"))
        {
            //Однако замены "на практику" всегда идут сверху, так что их индекс всегда 0.
            onPractiseString = Arrays.asList(technicalString.toString().split("– на практике")).get(0);
        }

        //Проверяем участие проверяемой группы на "практику":
        if (onPractiseString.toLowerCase(Locale.ROOT).contains(groupName.toLowerCase(Locale.ROOT)))
        {
            return DaySchedule.getOnPractiseSchedule(originalSchedule.day);
        }

        //Если группа НЕ на практике, то начинаем проверять таблицу с заменами:
        else
        {
            Iterator<XWPFTable> tables = document.getTablesIterator();

            while (tables.hasNext())
            {
                XWPFTable currentTable = tables.next();
                List<XWPFTableRow> rows = currentTable.getRows();

                for (XWPFTableRow row : rows)
                {
                    cellNumber = 0;
                    possibleNumbs = "";
                    currentLesson = new Lesson();
                    List<XWPFTableCell> cells = row.getTableCells();

                    for (XWPFTableCell cell : cells)
                    {
                        String text = cell.getText();
                        String lowerText = text.toLowerCase(Locale.ROOT);

                        //Перед проведением всех остальных проверок, ...
                        //... необходимо выполнить проверку на пустое содержимое ячейки:
                        if (text.equals(WORD_TABLE_EMPTY_CELL_VALUE))
                        {
                            cellNumber++;

                            continue;
                        }

                        //Если мы встретили ячейку, содержащую название ...
                        //... нужной группы начинаем считывание замен:
                        if (lowerText.equals(groupName.toLowerCase(Locale.ROOT)))
                        {
                            changesListen = true;

                            //Если замены по центру, то они на весь день:
                            absoluteChanges = cellNumber != 0;
                        }

                        //Если мы встречаем название другой группы во время чтения замен, ...
                        //... то прерываем цикл:
                        else if (changesListen && !lowerText.equals(groupName.toLowerCase(Locale.ROOT)) &&
                        (cellNumber == 0 || cellNumber == 3))
                        {
                            cycleStopper = true;

                            break;
                        }

                        //В ином случае мы продолжаем считывать замены, ...
                        //... ориентируясь на текущий номер ячейки:
                        else if (changesListen)
                        {
                            switch (cellNumber)
                            {
                                //Во второй ячейке находится номер пары:
                                case 1 -> possibleNumbs = text;

                                //В пятой ячейке название новой пары:
                                case 4 -> currentLesson.setName(text);

                                //В шестой - имя преподавателя:
                                case 5 -> currentLesson.setTeacher(text);

                                //В седьмой - место проведения пары:
                                case 6 -> currentLesson.setPlace(text);
                            }
                        }

                        cellNumber++;
                    }

                    if (changesListen && !possibleNumbs.equals(""))
                    {
                        newLessons.addAll(expandPossibleLessons(possibleNumbs, currentLesson));
                    }

                    //После прерывания первого цикла, прерываем и второй:
                    if (cycleStopper)
                    {
                        break;
                    }
                }
            }
        }

        //После обработки документа необходимо проверить полученные результаты, ...
        //... ведь у группы, возможно, вообще нет замен:
        if (newLessons.isEmpty())
        {
            return originalSchedule;
        }

        return originalSchedule.mergeChanges(newLessons, absoluteChanges);
    }

    /**
     * Метод, позволяющий раскрыть сокращенную запись номеров пар в полный вид.
     *
     * @param value Сокращенный (возможно) вид записи номеров пар.
     * @param lesson Пара, которая должна быть проведена.
     *
     * @return Полный вид пар.
     */
    private ArrayList<Lesson> expandPossibleLessons(String value, Lesson lesson)
    {
        String[] splatted = value.split(",");
        ArrayList<Lesson> toReturn = new ArrayList<>(1);

        //Чтобы преобразовать строку в число необходимо избавиться от пробелов:
        for (int i = 0; i < splatted.length; i++)
        {
            splatted[i] = splatted[i].replace(" ", "");
        }

        for (String s : splatted)
        {
            toReturn.add(new Lesson(Integer.parseInt(s), lesson.getName(), lesson.getTeacher(),
            lesson.getPlace()));
        }

        return toReturn;
    }
    //endregion

    //region Область: Внутренние методы.

    /**
     * Метод, нужный для получения таргетированного листа с которого будет считываться расписание.
     * Если группа не найдена, будет возвращено Null.
     *
     * @param groupName Имя группы для поиска страницы.
     *
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
    private ArrayList<TargetColumnBorders> searchTargetColumns(Sheet sheet,
    String groupName) throws NullPointerException
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
            indices.get(indices.size() - 1).RightBorderIndex = (Integer) (int) header.getLastCellNum();
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
    //endregion
}
