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
        //TODO: Доделать расписание первого курса.
    }

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
                System.out.println("\nПри добавлении произошла ошибка:\n"+ ex.getMessage());
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
            System.out.println("\nПри добавлении произошла ошибка:\n"+ ex.getMessage());
        }
    }

    /**
     * Метод для ручного внесения данных.
     * Содержит шаблон для заполнения, что упрощает и ускоряет этот долгий и скучный процесс.
     * <br>
     * Также установлено подавление орфографии, ибо имена считаются ошибками.
     */
    @SuppressWarnings({ "SpellCheckingInspection",
    "unused" })
    private static void fullManualExtractedInsert()
    {
        WeekSchedule web1 = new WeekSchedule();
        web1.setGroupName("19КСК-2");

        ArrayList<Lesson> monLesson = new ArrayList<>(7);
        monLesson.add(new Lesson(0, "ПЦУ", "Лихарев И. А.", "3"));
        monLesson.add(new Lesson(1, "Физ-Ра", "Баранов А. В.", null));
        monLesson.add(new Lesson(2, "Основы философии", "Каримова И. Р.", "3"));
        monLesson.add(new Lesson(3));
        monLesson.add(new Lesson(4));
        monLesson.add(new Lesson(5));
        monLesson.add(new Lesson(6));

        ArrayList<Lesson> secLesson = new ArrayList<>(7);
        secLesson.add(new Lesson(0));
        secLesson.add(new Lesson(1));
        secLesson.add(new Lesson(2));
        secLesson.add(new Lesson(3, "Ист. пит СВТ", "Ларионова Е. В.", "11"));
        secLesson.add(new Lesson(4, "Ист. пит СВТ", "Ларионова Е. В.", "11"));
        secLesson.add(new Lesson(5));
        secLesson.add(new Lesson(6));

        ArrayList<Lesson> thiLesson = new ArrayList<>(7);
        thiLesson.add(new Lesson(0, "Пр. по прототипированию", "Шарипов Н. Т.", "001"));
        thiLesson.add(new Lesson(1, "Пр. по прототипированию", "Шарипов Н. Т.", "001"));
        thiLesson.add(new Lesson(2, "ОАИП", "Лепеева Д. И.", "001"));
        thiLesson.add(new Lesson(3));
        thiLesson.add(new Lesson(4));
        thiLesson.add(new Lesson(5));
        thiLesson.add(new Lesson(6));

        ArrayList<Lesson> fouLesson = new ArrayList<>(7);
        fouLesson.add(new Lesson(0, "КС", "Поглазов К. Ю.", "3"));
        fouLesson.add(new Lesson(1, "КС", "Поглазов К. Ю.", "3"));
        fouLesson.add(new Lesson(2, "БЖД", null, "3"));
        fouLesson.add(new Lesson(3));
        fouLesson.add(new Lesson(4));
        fouLesson.add(new Lesson(5));
        fouLesson.add(new Lesson(6));

        ArrayList<Lesson> fifLesson = new ArrayList<>(7);
        fifLesson.add(new Lesson(0));
        fifLesson.add(new Lesson(1, "Прикл эл-ка", "Колесников Д. Н.",  "14"));
        fifLesson.add(new Lesson(2, "Ин. Яз.", "Каримова А. А.", "324"));
        fifLesson.add(new Lesson(3, "Прикл эл-ка", "Колесников Д. Н.", "14"));
        fifLesson.add(new Lesson(4, "Прикл эл-ка", "Колесников Д. Н.", "14"));
        fifLesson.add(new Lesson(5));
        fifLesson.add(new Lesson(6));

        ArrayList<Lesson> sixLesson = new ArrayList<>(7);
        sixLesson.add(new Lesson(0));
        sixLesson.add(new Lesson(1));
        sixLesson.add(new Lesson(2));
        sixLesson.add(new Lesson(3));
        sixLesson.add(new Lesson(4, "Пр САПП", "Лихарев И. А.", "11"));
        sixLesson.add(new Lesson(5, "Пр САПП", "Лихарев И. А.", "11"));
        sixLesson.add(new Lesson(6, "Пр САПП", "Лихарев И. А.", "11"));

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

        web1.setDays(days);

        writeToFile(web1, web1.getGroupName() + ".json");
    }

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

            FileWriter stream = new FileWriter("D:\\Java-Projects\\ExcelDataReader\\src\\main\\resources\\Programming\\" + fileName);
            stream.write(serialized);
            stream.close();
        }

        catch (Exception e)
        {
            e.fillInStackTrace();
        }
    }
}
