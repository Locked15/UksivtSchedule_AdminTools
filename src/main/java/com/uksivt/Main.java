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
        //TODO: Закончить ручное заполнение данных, начать с группы 21ПД-1.
        fullManualExtractedInsert();
    }

    /**
     * Метод для полуручного внесения данных о расписании групп в файлы.
     * <br>
     * Подходит для всех групп, кроме 1 курса и уКСК.
     *
     * @param reader Экземпляр класса "DataReader" для сканирования документа с расписанием.
     */
    private static void semiManualExtractedInsert(DataReader reader)
    {
        try
        {
            WeekSchedule schedule = reader.getWeekSchedule("19П-5");

            writeToFile(schedule, "19P-5");
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
    @SuppressWarnings({ "SpellCheckingInspection" })
    private static void fullManualExtractedInsert()
    {
        WeekSchedule web1 = new WeekSchedule();
        web1.setGroupName("21ПД-1");

        ArrayList<Lesson> monLesson = new ArrayList<>(7);
        monLesson.add(new Lesson(0, "Общество", "Анастасьев А. Г.", "323А"));
        monLesson.add(new Lesson(1, "Экономика", "Антипина А. В.", "323А"));
        monLesson.add(new Lesson(2, "Астрономия", null, "323А"));
        monLesson.add(new Lesson(3, "Математика",null, "323А"));
        monLesson.add(new Lesson(4));
        monLesson.add(new Lesson(5));
        monLesson.add(new Lesson(6));

        ArrayList<Lesson> secLesson = new ArrayList<>(7);
        secLesson.add(new Lesson(0));
        secLesson.add(new Lesson(1));
        secLesson.add(new Lesson(2));
        secLesson.add(new Lesson(3));
        secLesson.add(new Lesson(4, "Физ-Ра (1 Группа)", "Гильманов Р. А.",null));
        secLesson.add(new Lesson(4, "История (2 Группа)", "Акьюлов А. С.","323А"));
        secLesson.add(new Lesson(5, "История", "Акьюлов А. С.", "323А"));
        secLesson.add(new Lesson(6, "Информатика", "Защихина Е. В.", "323А"));

        ArrayList<Lesson> thiLesson = new ArrayList<>(7);
        thiLesson.add(new Lesson(0));
        thiLesson.add(new Lesson(1, "География", "Юнусова Л. Р.", "110"));
        thiLesson.add(new Lesson(2, "Ин. Яз.", "Сорокина Э. Г.", "110"));
        thiLesson.add(new Lesson(3));
        thiLesson.add(new Lesson(4));
        thiLesson.add(new Lesson(5));
        thiLesson.add(new Lesson(6));

        ArrayList<Lesson> fouLesson = new ArrayList<>(7);
        fouLesson.add(new Lesson(0));
        fouLesson.add(new Lesson(1));
        fouLesson.add(new Lesson(2));
        fouLesson.add(new Lesson(3, "ОБЖ", "Кобелева С. В.", "336"));
        fouLesson.add(new Lesson(4, "Лит-Ра (1 Группа)", "Постнова Д. В.", "336"));
        fouLesson.add(new Lesson(4, "Ин. Яз. (2 Группа)", "Сорокина Э. Г.", "336"));
        fouLesson.add(new Lesson(5));
        fouLesson.add(new Lesson(6));

        ArrayList<Lesson> fifLesson = new ArrayList<>(7);
        fifLesson.add(new Lesson(0, "Математика", null, "323А"));
        fifLesson.add(new Lesson(1, "Физ-Ра", "Гильманов Р. А.", null));
        fifLesson.add(new Lesson(2, "Русский", "Постнова Д. В.", "323А"));
        fifLesson.add(new Lesson(3, "Математика", null, null));
        fifLesson.add(new Lesson(4));
        fifLesson.add(new Lesson(5));
        fifLesson.add(new Lesson(6));

        ArrayList<Lesson> sixLesson = new ArrayList<>(7);
        sixLesson.add(new Lesson(0));
        sixLesson.add(new Lesson(1, "Баш. Яз.", "Юмагулова Г. К.","313"));
        sixLesson.add(new Lesson(2, "Лит-Ра", "Постнова Д. В.", "313"));
        sixLesson.add(new Lesson(3, "Право", "Исхакова С. И.", "336"));
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

        web1.setDays(days);

        writeToFile(web1, "21PD-1.json");
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

            FileWriter stream = new FileWriter("D:\\Java-Projects\\ExcelDataReader\\src\\main\\resources\\General\\PD\\" + fileName);
            stream.write(serialized);
            stream.close();
        }

        catch (Exception e)
        {
            e.fillInStackTrace();
        }
    }
}
