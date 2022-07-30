package com.uksivt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uksivt.data_reader.DataReader;
import com.uksivt.schedule_elements.DaySchedule;
import com.uksivt.schedule_elements.Days;
import com.uksivt.schedule_elements.Lesson;
import com.uksivt.schedule_elements.WeekSchedule;

import java.io.FileWriter;
import java.util.ArrayList;


/**
 * Базовый класс программы.
 */
public class Main
{
    /**
     * Точка входа в программу.
     *
     * @param args Аргументы сценария.
     */
    public static void main(String[] args)
    {
        System.out.println("Hello, World!");
    }

    //region Область: Методы добавления данных.
    /**
     * Метод для почти автоматического внесения данных о расписании групп.
     * <br/>
     * Записывает расписания сразу всех обнаруженных в документе групп.
     * <br/>
     * <br/>
     * <strong>ПРИ ПЕРЕХОДЕ К ДРУГОМУ ОТДЕЛЕНИЮ НЕОБХОДИМО ПРАВИТЬ "writeToFile()"!</strong>
     *
     * @param reader Экземпляр класса "DataReader" для считывания.
     */
    @SuppressWarnings({ "unused" })
    private static void almostAutomaticExtractedInsert(DataReader reader)
    {
        for (String group : reader.getGroups())
        {
            try
            {
                writeToFile(reader.getWeekSchedule(group), group + ".json");
            }

            catch (Exception ex)
            {
                System.out.println("\nПри добавлении произошла ошибка:\n" + ex.getMessage());
            }
        }
    }

    /**
     * Метод для полуручного внесения данных о расписании групп в файлы.
     * <br>
     * Подходит для всех групп, кроме 1 курса и уКСК.
     *
     * @param reader Экземпляр класса "DataReader" для сканирования документа с расписанием.
     */
    @SuppressWarnings({ "unused" })
    private static void semiManualExtractedInsert(DataReader reader)
    {
        try
        {
            String group = "";
            WeekSchedule schedule = reader.getWeekSchedule(group);

            writeToFile(schedule, group + ".json");
        }

        catch (Exception ex)
        {
            System.out.println("\nПри добавлении произошла ошибка:\n" + ex.getMessage());
        }
    }

    /**
     * Метод для ручного внесения данных.
     * Содержит шаблон для заполнения, что упрощает и ускоряет этот долгий и скучный процесс.
     * <br>
     * Также установлено подавление орфографии, ибо имена считаются ошибками.
     */
    @SuppressWarnings({
    "SpellCheckingInspection",
    "unused" })
    private static void fullManualExtractedInsert()
    {
        WeekSchedule group = new WeekSchedule();
        group.setGroupName("21ПД-3");

        ArrayList<Lesson> monLesson = new ArrayList<>(7);
        monLesson.add(new Lesson(0));
        monLesson.add(new Lesson(1));
        monLesson.add(new Lesson(2));
        monLesson.add(new Lesson(3));
        monLesson.add(new Lesson(4, "Ин. Яз.", "Сорокина Э. Г.", "314"));
        monLesson.add(new Lesson(5, "ОБЖ", "Исмаилова Р. Х.", "314"));
        monLesson.add(new Lesson(6, "Информатика", "Рысаева Э. Р.", "314"));

        ArrayList<Lesson> secLesson = new ArrayList<>(7);
        secLesson.add(new Lesson(0));
        secLesson.add(new Lesson(1, "Математика", "Максимова В. Д.", "314"));
        secLesson.add(new Lesson(2, "Математика", "Максимова В. Д.", "314"));
        secLesson.add(new Lesson(3, "Экология (1 Неделя)", "Юнусова Л. Р.", "314"));
        secLesson.add(new Lesson(3, "История (2 Неделя)", "Гумерова Э. Г.", "314"));
        secLesson.add(new Lesson(4, "Баш. Яз.", "Юмагулова Г. К.", "314"));
        secLesson.add(new Lesson(5));
        secLesson.add(new Lesson(6));

        ArrayList<Lesson> thiLesson = new ArrayList<>(7);
        thiLesson.add(new Lesson(0, "Экономика", "Киселева М. В.", "314"));
        thiLesson.add(new Lesson(1, "История", "Гумерова Э. Г.", "314"));
        thiLesson.add(new Lesson(2));
        thiLesson.add(new Lesson(3));
        thiLesson.add(new Lesson(4));
        thiLesson.add(new Lesson(5));
        thiLesson.add(new Lesson(6));

        ArrayList<Lesson> fouLesson = new ArrayList<>(7);
        fouLesson.add(new Lesson(0, "Лит-Ра", "Кинзина А. З.", "314"));
        fouLesson.add(new Lesson(1, "Физ-Ра (1 Неделя)", "Валиев Р. Ф.", null));
        fouLesson.add(new Lesson(1, "Ин. Яз. (2 Неделя)", "Сорокина Э. Г.", "314"));
        fouLesson.add(new Lesson(2, "Математика", "Максимова В. Д.", "314"));
        fouLesson.add(new Lesson(3));
        fouLesson.add(new Lesson(4));
        fouLesson.add(new Lesson(5));
        fouLesson.add(new Lesson(6));

        ArrayList<Lesson> fifLesson = new ArrayList<>(7);
        fifLesson.add(new Lesson(0, "Право", "Альмухаметова Г. А.", "314"));
        fifLesson.add(new Lesson(1, "Русский", "Кинзина А. З.", "314"));
        fifLesson.add(new Lesson(2, "Естествознание (1 Неделя)", "Сыртланова Л. А.", "314"));
        fifLesson.add(new Lesson(2, "Лит-Ра (2 Неделя)", "Кинзина А. З.", "314"));
        fifLesson.add(new Lesson(3));
        fifLesson.add(new Lesson(4));
        fifLesson.add(new Lesson(5));
        fifLesson.add(new Lesson(6));

        ArrayList<Lesson> sixLesson = new ArrayList<>(7);
        sixLesson.add(new Lesson(0));
        sixLesson.add(new Lesson(1));
        sixLesson.add(new Lesson(2, "Естествознание", "Сыртланова Л. А.", "314"));
        sixLesson.add(new Lesson(3, "Физ-Ра", "Валиев Р. Ф.", null));
        sixLesson.add(new Lesson(4));
        sixLesson.add(new Lesson(5));
        sixLesson.add(new Lesson(6));

        ArrayList<Lesson> sevLesson = new ArrayList<>(7);
        sevLesson.add(new Lesson(0));
        sevLesson.add(new Lesson(1));
        sevLesson.add(new Lesson(2));
        sevLesson.add(new Lesson(3));
        sevLesson.add(new Lesson(4));
        sevLesson.add(new Lesson(5));
        sevLesson.add(new Lesson(6));

        ArrayList<DaySchedule> days = new ArrayList<>(7);
        days.add(new DaySchedule(Days.Monday, monLesson));
        days.add(new DaySchedule(Days.Tuesday, secLesson));
        days.add(new DaySchedule(Days.Wednesday, thiLesson));
        days.add(new DaySchedule(Days.Thursday, fouLesson));
        days.add(new DaySchedule(Days.Friday, fifLesson));
        days.add(new DaySchedule(Days.Saturday, sixLesson));
        days.add(new DaySchedule(Days.Sunday, sevLesson));

        group.setDays(days);

        writeToFile(group, group.getGroupName() + ".json");
    }
    //endregion

    //region Область: Метод записи данных в файл.
    /**
     * Внутренний метод для записи полученных значений в файл.
     *
     * @param schedule Расписание, которое следует записать.
     * @param fileName Название файла, куда будет записано расписание.
     */
    private static void writeToFile(WeekSchedule schedule, String fileName)
    {
        try
        {
            ObjectMapper serializer = new ObjectMapper();
            String serialized = serializer.writerWithDefaultPrettyPrinter().writeValueAsString(schedule);

            FileWriter stream = new FileWriter("D:\\Java-Projects\\ExcelDataReader\\src\\main\\resources\\General\\" + fileName);
            stream.write(serialized);
            stream.close();
        }

        catch (Exception e)
        {
            e.fillInStackTrace();
        }
    }
    //endregion
}
