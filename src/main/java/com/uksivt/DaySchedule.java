package com.uksivt;

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
    //endregion
}

/**
 * Перечисление, нужное для отображения дня.
 */
enum Days
{
    //region Элементы перечисления.
    /**
     * Понедельник.
     */
    Monday,

    /**
     * Вторник.
     */
    Tuesday,

    /**
     * Среда.
     */
    Wednesday,

    /**
     * Четверг.
     */
    Thursday,

    /**
     * Пятница.
     */
    Friday,

    /**
     * Суббота.
     */
    Saturday,

    /**
     * Воскресенье.
     */
    Sunday;
    //endregion

    /**
     * Метод для получения дня по указанному индексу.
     *
     * @param i Индекс по которому нужно получить значение.
     *
     * @return Значение, соответствующее данному индексу.
     *
     * @throws IndexOutOfBoundsException Введенный индекс превышает количество значений.
     */
    public static Days getValueByIndex(Integer i) throws IndexOutOfBoundsException
    {
        return switch (i)
        {
            case 0 -> Monday;

            case 1 -> Tuesday;

            case 2 -> Wednesday;

            case 3 -> Thursday;

            case 4 -> Friday;

            case 5 -> Saturday;

            case 6 -> Sunday;

            default -> throw new IndexOutOfBoundsException("Введен слишком большой индекс.");
        };
    }

    /**
     * Метод для получения индекса для указанного дня.
     *
     * @param day День, индекс которого нужно получить.
     *
     * @return Значение, соответствующее данному индексу.
     *
     * @throws IndexOutOfBoundsException Введенный индекс превышает количество значений.
     */
    public static Integer getIndexByValue(Days day)
    {
        return switch (day)
        {
            case Monday -> 0;

            case Tuesday -> 1;

            case Wednesday -> 2;

            case Thursday -> 3;

            case Friday -> 4;

            case Saturday -> 5;

            case Sunday -> 6;
        };
    }

    /**
     * Метод для преобразования Enum-значения в строковый вид.
     *
     * @param day Элемент перечисления Enum.
     *
     * @return Строковое представление перечисления.
     */
    public static String toString(Days day)
    {
        String newDay = "";

        switch (day)
        {
            case Monday -> newDay = "Monday";

            case Tuesday -> newDay = "Tuesday";

            case Wednesday -> newDay = "Wednesday";

            case Thursday -> newDay = "Thursday";

            case Friday -> newDay = "Friday";

            case Saturday -> newDay = "Saturday";

            case Sunday -> newDay = "Sunday";
        }

        return translateToRussianLanguage(newDay);
    }

    /**
     * Метод для преобразования Enum-значения в строковый вид.
     *
     * @param day Элемент перечисления Enum.
     *
     * @return Строковое представление перечисления.
     */
    public static Days fromString(String day)
    {
        day = translateToOriginalLanguage(day);

        return switch (day)
        {
            case "Monday" -> Monday;

            case "Tuesday" -> Tuesday;

            case "Wednesday" -> Wednesday;

            case "Thursday" -> Thursday;

            case "Friday" -> Friday;

            case "Saturday" -> Saturday;

            case "Sunday" -> Sunday;

            default -> null;
        };
    }

    /**
     * Метод для перевода русскоязычных названий дней в оригинальный (английский) язык.
     *
     * @param day День недели на русском.
     *
     * @return День недели на английском.
     */
    public static String translateToOriginalLanguage(String day)
    {
        day = day.toLowerCase(Locale.ROOT);

        return switch (day)
        {
            case "понедельник" -> "Monday";

            case "вторник" -> "Tuesday";

            case "среда" -> "Wednesday";

            case "четверг" -> "Thursday";

            case "пятница" -> "Friday";

            case "суббота" -> "Saturday";

            case "воскресенье" -> "Sunday";

            default -> day;
        };
    }

    /**
     * Метод для перевода англоязычных названий дней в русский язык.
     *
     * @param day День недели на английском.
     *
     * @return День недели на русском.
     */
    public static String translateToRussianLanguage(String day)
    {
        return switch (day)
        {
            case "Monday" -> "Понедельник";

            case "Tuesday" -> "Вторник";

            case "Wednesday" -> "Среда";

            case "Thursday" -> "Четверг";

            case "Friday" -> "Пятница";

            case "Saturday" -> "Суббота";

            case "Sunday" -> "Воскресенье";

            default -> day;
        };
    }
}
