package com.uksivt;

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
        ArrayList<String> groups;
        DataReader readValues = new DataReader();

        groups = readValues.getGroups();

        for (int i = 0; i < groups.size(); i++)
        {
            System.out.println(i + " " + groups.get(i));
        }

        System.out.println("\nКонец.");
    }
}
