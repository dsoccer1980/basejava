package com.urise.webapp;

import com.urise.webapp.model.*;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

        Organization organization1 = new Organization("Java Online Projects", "http://javaops.ru/");
        OrganizationPosition experience1 = new OrganizationPosition(
                "Автор проекта",
                LocalDate.of(2013, 10, 1),
                null, "Создание, организация и проведение Java онлайн проектов и стажировок.");
        OrganizationPosition experience2 = new OrganizationPosition(
                "Старший разработчик (backend)",
                LocalDate.of(2014, 10, 1),
                LocalDate.of(2016, 1, 1), "Проектирование и разработка онлайн платформы управления проектами Wrike");

        List<OrganizationPosition> organizationPositions = Arrays.asList(experience1, experience2);
        Map<Organization, List<OrganizationPosition>> organizationSection1 = new HashMap<>();
        organizationSection1.put(organization1, organizationPositions);

        sections.put(SectionType.EXPERIENCE, new OrganizationSection(organizationSection1));

        Organization workOrganization = new Organization("RIT Center", null);
        OrganizationPosition experience3 = new OrganizationPosition(
                "Java архитектор",
                LocalDate.of(2012, 4, 1),
                LocalDate.of(2014, 10, 1), "Организация процесса разработки системы ERP для разных окружений");
        organizationSection1.put(workOrganization, Arrays.asList(experience3));


        Organization educationOrganization = new Organization("Coursera", "https://www.coursera.org/course/progfun");


        OrganizationPosition education1 = new OrganizationPosition(
                "Functional Programming Principles in Scala\" by Martin Odersky",
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1),
                "");
        OrganizationPosition education2 = new OrganizationPosition(
                "Курс \"Объектно-ориентированный анализ ИС. Концептуальное моделирование на UML.\"",
                LocalDate.of(2011, 3, 1),
                LocalDate.of(2011, 4, 1),
                "");

        List<OrganizationPosition> organizationPositions2 = Arrays.asList(education1, education2);
        Map<Organization, List<OrganizationPosition>> organizationSection2 = new HashMap<>();
        organizationSection2.put(educationOrganization, organizationPositions);
        sections.put(SectionType.EDUCATION, new OrganizationSection(organizationSection2));
        resume.setSections(sections);
        System.out.println(resume);
    }
}
