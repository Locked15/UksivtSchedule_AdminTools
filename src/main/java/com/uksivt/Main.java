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
        //TODO: Продолжить заполнение данных, начать с группы 21ПСА-3.
        fullManualExtractedInsert();
    }

    /**
     * Метод для вывода информации из считанных файлов с расписанием и заменами.
     */
    private static void testParsers()
    {
        DataReader reader = new DataReader("C:\\Users\\Земфира\\Desktop\\Prog.xlsx",
        "C:\\Users\\Земфира\\Desktop\\ProgSec.docx");
        ArrayList<String> groups = reader.getGroups();

        try
        {
            var schedule = reader.getWeekSchedule("19П-3");
            var newSchedule = reader.getDayScheduleWithChanges(schedule, Days.Friday);

            //Место для установки Точки Останова:
            System.out.println();
        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println("\nКонец.");
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
        web1.setGroupName("21ПСА-2");

        ArrayList<Lesson> monLesson = new ArrayList<>(7);
        monLesson.add(new Lesson(0));
        monLesson.add(new Lesson(1));
        monLesson.add(new Lesson(2, "Математика", null, "Чз общежитие"));
        monLesson.add(new Lesson(3, "Русский", "Мустакимова И. М.", "Чз общежитие"));
        monLesson.add(new Lesson(4, "Информатика", "Мусина Г. А.", "Чз общежитие"));
        monLesson.add(new Lesson(5, "Общество", "Исмагилова Г. М.", "Чз общежитие"));
        monLesson.add(new Lesson(6));

        ArrayList<Lesson> secLesson = new ArrayList<>(7);
        secLesson.add(new Lesson(0));
        secLesson.add(new Lesson(1));
        secLesson.add(new Lesson(2, "Ин. Яз.", "Вачаева М. В.", "Чз общежитие"));
        secLesson.add(new Lesson(3, "Баш. Яз.", "Мухамедьянова Г. М.", "Чз общежитие"));
        secLesson.add(new Lesson(4, "Экономика", "Засыпкин К. Н.", "Чз общежитие"));
        secLesson.add(new Lesson(5, "Математика", null,"Чз общежитие"));
        secLesson.add(new Lesson(6));

        ArrayList<Lesson> thiLesson = new ArrayList<>(7);
        thiLesson.add(new Lesson(0));
        thiLesson.add(new Lesson(1));
        thiLesson.add(new Lesson(2));
        thiLesson.add(new Lesson(3, "Лит-Ра (1 Группа)", "Мустакимова И. М.", "Чз общежитие"));
        thiLesson.add(new Lesson(4, "История", "Салихов Г. Г.", "Чз общежитие"));
        thiLesson.add(new Lesson(5, "География", "Юнусова Л. Р.", "Чз общежитие"));
        thiLesson.add(new Lesson(6));

        ArrayList<Lesson> fouLesson = new ArrayList<>(7);
        fouLesson.add(new Lesson(0));
        fouLesson.add(new Lesson(1));
        fouLesson.add(new Lesson(2, "Астрономия", null, "Чз общежитие"));
        fouLesson.add(new Lesson(3, "Право", "Исхакова С. И.", "Чз общежитие"));
        fouLesson.add(new Lesson(4, "Ин. Яз. (2 Группа)", "Вачаева М. В.", "Чз общежитие"));
        fouLesson.add(new Lesson(5));
        fouLesson.add(new Lesson(6));

        ArrayList<Lesson> fifLesson = new ArrayList<>(7);
        fifLesson.add(new Lesson(0));
        fifLesson.add(new Lesson(1));
        fifLesson.add(new Lesson(2, "Математика", null, "Чз общежитие"));
        fifLesson.add(new Lesson(3, "Физ-Ра", "Гильманов Р. А.", null));
        fifLesson.add(new Lesson(4, "История", "Салихов Г. Г.", "Чз общежитие"));
        fifLesson.add(new Lesson(5));
        fifLesson.add(new Lesson(6));

        ArrayList<Lesson> sixLesson = new ArrayList<>(7);
        sixLesson.add(new Lesson(0));
        sixLesson.add(new Lesson(1));
        sixLesson.add(new Lesson(2, "ОБЖ", null, "Чз общежитие"));
        sixLesson.add(new Lesson(3, "Лит-Ра", "Мустакимова И. М.", "Чз общежитие"));
        sixLesson.add(new Lesson(4, "Физ-Ра (1 Группа)", "Гильманов Р. А.", null));
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

        writeToFile(web1, "21PSA-2.json");
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

            FileWriter stream = new FileWriter("D:\\Java-Projects\\ExcelDataReader\\src\\main\\resources\\General\\PSA\\" + fileName);
            stream.write(serialized);
            stream.close();
        }

        catch (Exception e)
        {
            e.fillInStackTrace();
        }
    }
}
