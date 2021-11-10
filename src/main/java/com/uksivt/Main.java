package com.uksivt;

import java.io.FileWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;


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
        DataReader reader = new DataReader();
        ArrayList<String> groups = reader.getGroups();

        System.out.println("Список групп:");

        for (String group : groups)
        {
            System.out.println(group);
        }

        try
        {
            var schedule = reader.getWeekSchedule("19ВЕБ-2");

            System.out.println("Here i am.");
        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println("\nКонец.");
    }

    /**
     * Метод для ручного внесения данных.
     * Содержит шаблон для заполнения, что упрощает и ускоряет этот долгий и скучный процесс.
     * <br>
     * Также установлено подавление орфографии, ибо имена считаются ошибками.
     */
    @SuppressWarnings({ "WriteOnlyObject", "SpellCheckingInspection" })
    public static void extractedInsert()
    {
        WeekSchedule web1 = new WeekSchedule();
        web1.setGroupName("21ВЕБ-1");

        ArrayList<Lesson> monLessons = new ArrayList<>(1);
        monLessons.add(new Lesson(3, "Информатика", "Васильева А. А.", "117"));
        monLessons.add(new Lesson(4, "Физика", "Нагаев Р. А.", "336"));
        monLessons.add(new Lesson(5, "Ин. Яз. (1 Группа)", "Нуриева А. М.", "336"));
        monLessons.add(new Lesson(5, "Лит-Ра (2 Группа)", "Бокарева С. Ф.", "336"));
        monLessons.add(new Lesson(6, "Химия", "Гареева С. Т.", "336"));

        ArrayList<Lesson> secLessons = new ArrayList<>(1);
        secLessons.add(new Lesson(1, "Баш. Яз.", "Хисамутдинова Р. М.", "334"));
        secLessons.add(new Lesson(2, "Общество", "Ахметшина Е. В.", "334"));
        secLessons.add(new Lesson(3, "Математика", "Гущина М. В.", "334"));

        ArrayList<Lesson> thiLessons = new ArrayList<>(1);
        thiLessons.add(new Lesson(0, "Русский", "Бокарева С. Ф.", "327"));
        thiLessons.add(new Lesson(1, "Математика", "Гущина М. В.", "327"));
        thiLessons.add(new Lesson(2, "География", "[ПУСТО]", "327"));

        ArrayList<Lesson> fouLesson = new ArrayList<>(1);
        fouLesson.add(new Lesson(0, "Математика", "Гущина М. В.", "327"));
        fouLesson.add(new Lesson(1, "Физ-Ра", "", ""));

        ArrayList<Lesson> fifLesson = new ArrayList<>(5);
        fifLesson.add(new Lesson(2, "Физика", "Нагаев Р. А.", ""));
        fifLesson.add(new Lesson(3, "ОБЖ", "Ин. Яз.", ""));
        fifLesson.add(new Lesson(4, "Ин. Яз.", "Нуриева А. М.", ""));
        fifLesson.add(new Lesson(5, "История (1 Группа)", "Акъюлов А. С.", ""));
        fifLesson.add(new Lesson(5, "Физ-Ра (2 Группа)", "", ""));

        ArrayList<Lesson> sixLesson = new ArrayList<>(2);
        sixLesson.add(new Lesson(0, "Лит-Ра", "Бокарева С. Ф.", "334"));
        sixLesson.add(new Lesson(1, "История", "Акъюлов А. С.", "334"));

        ArrayList<Lesson> sevLesson = new ArrayList<>(1);

        ArrayList<DaySchedule> days = new ArrayList<>(7);
        days.add(new DaySchedule(Days.Monday, monLessons));
        days.add(new DaySchedule(Days.Tuesday, secLessons));
        days.add(new DaySchedule(Days.Wednesday, thiLessons));
        days.add(new DaySchedule(Days.Thursday, fouLesson));
        days.add(new DaySchedule(Days.Friday, fifLesson));
        days.add(new DaySchedule(Days.Saturday, sixLesson));
        days.add(new DaySchedule(Days.Sunday, sevLesson));

        web1.setDays(days);

        try
        {
            ObjectMapper serializer = new ObjectMapper();
            String serialized = serializer.writerWithDefaultPrettyPrinter().writeValueAsString(days);

            FileWriter stream = new FileWriter("D:\\Java-Projects\\ExcelDataReader\\src\\main\\resources\\General\\WEB\\21WEB-1.json");
            stream.write(serialized);
            stream.close();
        }

        catch (Exception e)
        {
            e.fillInStackTrace();
        }
    }
}
