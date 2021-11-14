package com.uksivt;

import com.uksivt.parser_elements.ChangeElement;
import com.uksivt.parser_elements.MonthChanges;
import com.uksivt.schedule_elements.Days;
import org.apache.xpath.operations.Bool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Класс, нужный для получения документа для парса с официального сайта.
 */
public class DataGetter
{
    /**
     * Внутренняя константа, содержащая путь к HTML-тегу с заменами.
     * <br>
     * Путь начинается с элемента <i>"section id="inside-page"</i>".
     */
    private static final String CHANGES_PATH_IN_HTML;

    /**
     * Внутренняя константа, содержащая неразрывный пробел.
     */
    private static final String NON_BREAKING_SPACE;

    /**
     * Поле, содержащее полученную веб-страницу.
     */
    private Document webPage;

    /**
     * Конструктор класса.
     */
    public DataGetter()
    {
        try
        {
            webPage = Jsoup.connect("https://www.uksivt.ru/zameny").get();
        }

        catch (Exception e)
        {
            System.out.println("В процессе подключения произошла ошибка.");
        }
    }

    //Статический конструктор.
    static
    {
        //region Подобласть: Инициализация константы "CHANGES_PATH_IN_HTML".
        CHANGES_PATH_IN_HTML = "section > div > div > div > div > div > div" +
        " > div > div > div > div > div > div > div > div > div > div > div";
        //endregion

        //region Подобласть: Инициализация константы "NON_BREAKING_SPACE".
        NON_BREAKING_SPACE = "\u00A0";
        //endregion
    }

    /**
     * Метод для получения списка возможных дней для просмотра замен.
     * <br>
     * В этом методе описываются теги, так что проверка орфографии отключена.
     *
     * @return Список с доступными заменами месяцам.
     */
    @SuppressWarnings("SpellCheckingInspection")
    public ArrayList<MonthChanges> getAvailableNodes()
    {
        //region Подобласть: Переменные считывания доступных замен.
        Integer i = 0;
        String currentMonth = "Январь";
        ArrayList<ChangeElement> changes = new ArrayList<>(30);
        ArrayList<MonthChanges> monthChanges = new ArrayList<>(10);
        //endregion

        //region Подобласть: Переменные парса веб-страницы.
        Element generalChange = webPage.getElementById("inside-page").select(CHANGES_PATH_IN_HTML).get(0);
        Elements listOfChanges = generalChange.children();
        //endregion

        for (Element element : listOfChanges)
        {
            String nodeName = element.nodeName();
            String text = element.text();

            //Месяцы всегда находятся в теге "p", но в самом начале страницы есть ...
            //... пробел, создаваемый этим же тегом. Он содержит в себе так называемый ...
            //... неразрывный пробел, так что это тоже необходимо учитывать.
            if (element.nodeName().equals("p") && !text.contentEquals(NON_BREAKING_SPACE))
            {
                //В первой итерации программа также зайдет сюда, так что это надо учесть.
                if (!changes.isEmpty())
                {
                    monthChanges.add(new MonthChanges(currentMonth, changes));
                }

                String temp = element.text();
                currentMonth = temp.substring(0, temp.lastIndexOf(' ')).replace(NON_BREAKING_SPACE, "");

                changes = new ArrayList<>(30);

                i = 1;
            }

            //Если мы встречаем тег "table", то мы дошли до таблицы с заменами на какой-либо месяц.
            else if (element.nodeName().equals("table"))
            {
                //В таблице первым тегом всегда идет "<thead>", определяющий заголовок, ...
                //... а вторым — "<tbody>", определяющий тело таблицы. Он нам и нужен.
                Element tableBody = element.children().get(1);
                Elements tableRows = tableBody.children();

                for (int j = 0; j < tableRows.size(); j++)
                {
                    Integer dayCounter = 0;
                    Element currentRow = tableRows.get(j);

                    //В первой строке содержатся ненужные значения, пропускаем:
                    if (j == 0)
                    {
                        continue;
                    }

                    //В последних двух строках также содержатся бесполезные значения.
                    else if (j + 2 >= tableRows.size())
                    {
                        break;
                    }

                    //В иных случаях начинаем итерировать ячейки таблицы:
                    for (Element tableCell : currentRow.children())
                    {
                        //Первая ячейка, видимо для отступа, содержит неразрывный пробел, так что ...
                        //... пропускаем такую итерацию. Кроме того, если 1 число месяца выпадает ...
                        //... не на понедельник, то некоторое количество ячеек также будет пустым.
                        if (tableCell.text().equals(NON_BREAKING_SPACE))
                        {
                            continue;
                        }

                        //В некоторых ячейках (дни без замен) нет содержимого, так что учитываем это.
                        if (tableCell.children().size() < 1)
                        {
                            changes.add(new ChangeElement(i, null, Days.getValueByIndex(dayCounter)));
                        }

                        //В ином случае замены есть и нам нужно получить дочерний элемент.
                        else
                        {
                            Element link = tableCell.children().first();

                            changes.add(new ChangeElement(i, link.attr("href"),
                            Days.getValueByIndex(dayCounter)));
                        }

                        i++;
                        dayCounter++;
                    }
                }
            }
        }

        return monthChanges;
    }
}
