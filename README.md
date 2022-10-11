# Admin Tools for Uksivt Schedule

This project targets autocompletion support for college schedule updating. \
Project has been originally designed in October 2021.
\
Navigation:

1. [Admin Tools for Uksivt Schedule](#admin-tools-for-uksivt-schedule)
    1. [General Information](#general-information)
        1. [Before Using](#before-using)
        2. [Prepare the Documents](#prepare-the-documents)
        3. [Using Instructions](#using-instructions)
            1. [Automatic Mode](#automatic-mode)
            2. [Semi-Manual Mode](#semi-manual-mode)
            3. [Manual Mode](#manual-mode)
        4. [Output Documents](#output-documents)
    2. [Authors](#authors)

## General Information

This article divided into few sections, to simplify navigation. \
Remember: this application can't cover all use cases and require check input and output information.

### Before Using

Before using this application you must have to install Java runtime. \
Application targets to Java 17 runtime, so prior versions may not work or work properly.
\
If you want to use redesigned version of application (work in progress), you need to install Java runtime 19. \

### Prepare the Documents

Original documents with schedule (that can be downloaded from college website) ain't normalized. \
Inside documents lies many data anomalies. Also, there is a lot of empty lines and cells, that
will disturb parse algorithm.
\
So, before using the program, you must have to normalize documents.
Base normalize algorithm can be described in few steps:

1. Remove unused columns (commonly they appear between schedule and day blocks);
2. Replace all anomaly placed schedule elements in one order (commonly appear in first course schedule document);
3. Remove final list with all-in-one schedules, if it presents in document. \
   It will disturb base parser logic that define base groups names by list name;
4. Move all "ч-з общежитие" in one cell (cause parser will parse value inside one cell and then replace).

### Using Instructions

Application can parse schedule information in 3 modes:

1. Automatic Mode — create a new *.json-file, with fully automatic filling. \
   All you need to do, is targets file, that will be parsed and set final destination,
   where schedule files will be created.
   \
   **WARNING: This mode can't be applied to [General](https://bit.ly/3SBx67W) and [Tech](https://bit.ly/3SmPZvX) branches
   ("Общеобразовательное" и "Отделение вычислительной техники"). \
   For this branches, you'll have to use semi-automatic or manual modes**;
2. Semi-Manual Mode — need to create a new *.json-file with manual setting target group name. \
   Allow to ignore other schedule elements, except group, that you set;
3. Manual Mode — Allow you to manually fill all schedule objects, that will be serialized. \
   Must be used with schedule elements, that cannot be parsed in other mods (like first course with subgroups dividing).

#### Automatic Mode

To create a new *.json-file download document, normalize and analyze it.
If you see, that document can be parsed automatically, open program, set proper values and run it. \
As output you'll get full set of schedule elements, that placed inside target document.  \
*Don't forget to check values to correct.*

#### Semi-Manual Mode

If you see, that document have unsupported values, but also have parse-able values too, you'll have to use this mod.
Open program, set target group name and set other values. Run program. \
*Don't forget to check values to correct.*

#### Manual Mode

If you see, that current element (or document) can't be parsed automatically, so you need to write values manually.
This mode will help you in this process and relief it. \
Open program and expand last method inside Main class. There will be templates of the schedule elements.
You need to fill it with values inside of document.
\
In process you want to ask some questions, so I answer them right here:

1. If you met subgroups lessons, so you need to create few lesson elements with same number and
   write what subgroup will learn on this;
2. If you met a lesson, that divided by '/' or '\\' symbols, so you have met week based schedule elements.
   First value will apply in non-even weeks, second in even weeks. Mark it in lesson name;
3. If you met a lesson without teacher or place — don't worry, it's normal. Just leave this values '*NULL*'.

### Output Documents

Output documents will contain all data about lessons. \
Also, they will contain '*NULL*' lessons, so if you want to hide it, you'll have to remove them later.
All documents will indent and prettified, to more ergonomic human use (like checking or something else).

## Authors

Created by [Locked15](https://github.com/Locked15) to '*Uksivt Schedule*' system. \
2021 — 2022.
