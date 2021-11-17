package com.uksivt.schedule_elements;

import java.util.*;

/**
 * Класс, предоставляющий логику для расписания одного конкретного дня.
 */
public class DaySchedule
{
    //region Область: Поля класса.
    /**
     * Поле, содержащее день, который описывается.
     */
    public final Days day;

    /**
     * Поле, содержащее пары, проводимые в этот день.
     */
    public final ArrayList<Lesson> lessons;
    //endregion

    //region Область: Конструктор класса.
    /**
     * Конструктор класса.
     *
     * @param day День недели.
     * @param lessons Пары этого дня.
     */
    public DaySchedule(Days day, ArrayList<Lesson> lessons)
    {
        this.day = day;
        this.lessons = lessons;
    }
    //endregion

    //region Область: Методы.
    /**
     * Метод, позволяющий слить оригинальное расписание и замены для него.
     *
     * @param changes Замены.
     * @param absoluteChanges Замены на весь день?
     *
     * @return Объединенное расписание с заменами.
     */
    public DaySchedule mergeChanges(ArrayList<Lesson> changes, Boolean absoluteChanges)
    {
        ArrayList<Lesson> mergedSchedule = this.lessons;

        if (absoluteChanges)
        {
            //Чтобы избавиться от возможных проблем со ссылками в будущем, ...
            //... создаем новый объект:
            return new DaySchedule(day, fillEmptyLessons(changes));
        }

        for (Lesson change : changes)
        {
            Integer lessonIndex = change.getNumber();

            if (change.getName().toLowerCase(Locale.ROOT).equals("нет"))
            {
                change.setName(null);
            }

            mergedSchedule.set(lessonIndex, change);
        }

        return new DaySchedule(this.day, mergedSchedule);
    }

    /**
     * Если замены на весь день, то возвращаемое значение содержит только замены.
     * Чтобы добавить пустые пары, используется этот метод.
     *
     * @param lessons Расписание замен.
     *
     * @return Расписание замен с заполнением.
     */
    private ArrayList<Lesson> fillEmptyLessons(ArrayList<Lesson> lessons)
    {
        for (int i = 0; i < 7; i++)
        {
            Boolean missing = true;

            for (Lesson lesson : lessons)
            {
                if (lesson.getNumber() == i)
                {
                    missing = false;

                    break;
                }
            }

            if (missing)
            {
                lessons.add(new Lesson(i, null, null, null));
            }
        }

        //Добавленные "пустые" пары находятся в конце списка, так что ...
        //... мы сортируем их в порядке номера пар:
        Collections.sort(lessons);

        return lessons;
    }

    /**
     * Статический метод, позволяющий получить расписание на день для группы на практике.
     *
     * @param day День недели для создания расписания.
     *
     * @return Расписание на день для группы с практикой.
     */
    public static DaySchedule getOnPractiseSchedule(Days day)
    {
        ArrayList<Lesson> lessons = new ArrayList<>(7);

        for (int i = 0; i < 7; i++)
        {
            lessons.add(new Lesson(i, "Практика", null, null));
        }

        return new DaySchedule(day, lessons);
    }

    /**
     * Метод, возвращающий строковое представление объекта.
     *
     * @return Строковое представление отправленного объекта.
     */
    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder(Days.toString(day) + ":\n" +
        "{");

        for (Lesson lesson : lessons)
        {
            toReturn.append("\n\t{").append("\n\t\tНомер пары: ").append(lesson.getNumber())
            .append("\n\t\t").append("Название пары: ").append(lesson.getName()).append("\n\t\t")
            .append("Кабинет: ").append(lesson.getPlace()).append("\n\t\t")
            .append("Преподаватель: ").append(lesson.getTeacher()).append("\n\t}");
        }

        toReturn.append("\n}");

        return toReturn.toString();
    }
    //endregion
}
