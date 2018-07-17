package com.urise.webapp;

import com.urise.webapp.model.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainReflection {

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Resume resume = new Resume("uuid1", "Grigory Kislin");
//        Field field = resume.getClass().getDeclaredFields()[0];
//        field.setAccessible(true);
//        System.out.println(field.getName());
//        System.out.println(field.get(resume));
//
//        Method methodToString = resume.getClass().getMethod("toString");
//        System.out.println(resume.toString());
//        System.out.println(methodToString.invoke(resume));

        Map<ContactType, String> contacts = new HashMap<>();
        contacts.put(ContactType.TELEFON, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "grigory.kislin");
        contacts.put(ContactType.EMAIL, "gkislin@yandex.ru");
        resume.setContact(contacts);
        Map<SectionType, Section> sections = new HashMap<>();
        sections.put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        sections.put(SectionType.PERSONAL, new TextSection("Аналитический склад ума,"));
        sections.put(SectionType.ACHIEVEMENT,
                new ListSection(Arrays.asList("С 2013 года: разработка проектов ", "Реализация двухфакторной аутентификации")));
        sections.put(SectionType.QUALIFICATIONS,
                new ListSection(Arrays.asList("JEE AS: GlassFish (v2.1, v3)", "Version control:")));

        Organization experience1 = new Organization(new Link("Java Online Projects", "http://javaops.ru/"),
                "Автор проекта",
                LocalDate.of(2013, 10, 1),
                null, "Создание, организация и проведение Java онлайн проектов и стажировок.");
        Organization experience2 = new Organization(new Link("Wrike", "https://www.wrike.com/"),
                "Старший разработчик (backend)",
                LocalDate.of(2014, 10, 1),
                LocalDate.of(2016, 1, 1), "Проектирование и разработка онлайн платформы управления проектами Wrike");
        sections.put(SectionType.EXPERIENCE, new OrganizationSection(Arrays.asList(experience1, experience2)));

        Organization education1 = new Organization(new Link("Coursera", "https://www.coursera.org/course/progfun"),
                "Functional Programming Principles in Scala\" by Martin Odersky",
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1),
                "");
        Organization education2 = new Organization(new Link("Luxoft", "http://www.luxoft-training.ru/training/catalog/course.html?ID=22366"),
                "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"",
                LocalDate.of(2011, 3, 1),
                LocalDate.of(2011, 4, 1),
                "");
        sections.put(SectionType.EDUCATION, new OrganizationSection(Arrays.asList(education1, education2)));
        resume.setSections(sections);
        System.out.println(resume);
    }
}
