package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static com.urise.webapp.util.DateUtil.NOW;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final String STORAGE_DIR = "./storage";
    protected Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String DUMMY = "dummy";
    private int countResumesBeforeTest;
    private Resume resume1 = new Resume(UUID_1, "Petrov");
    private Resume resume2 = new Resume(UUID_2, "Ivanov");
    private Resume resume3 = new Resume(UUID_3, "Anjukov");
    private Resume resume4 = new Resume("Anyname");
    private Resume dummyResume = new Resume(DUMMY, "");

    @Before
    public void setUp() {
        try {
            Files.createDirectory(Paths.get(STORAGE_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }

        storage.clear();
        Map<ContactType, String> contacts = new HashMap<>();
        contacts.put(ContactType.TELEFON, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "grigory.kislin");
        contacts.put(ContactType.EMAIL, "gkislin@yandex.ru");
        resume1.setContacts(contacts);
        Map<SectionType, Section> sections = new HashMap<>();
        sections.put(SectionType.OBJECTIVE, new TextSection("Ведущий стажировок и корпоративного обучения по Java Web и Enterprise технологиям"));
        sections.put(SectionType.PERSONAL, new TextSection("Аналитический склад ума,"));
        sections.put(SectionType.ACHIEVEMENT,
                new ListSection(Arrays.asList("С 2013 года: разработка проектов ", "Реализация двухфакторной аутентификации")));
        sections.put(SectionType.QUALIFICATIONS,
                new ListSection(Arrays.asList("JEE AS: GlassFish (v2.1, v3)", "Version control:")));

        Organization.Position organizationPosition1 = new Organization.Position(
                "Автор проекта",
                LocalDate.of(2013, 10, 1),
                NOW, "Создание, организация и проведение Java онлайн проектов и стажировок.");


        Organization workOrganization1 = new Organization("Java Online Projects", "http://javaops.ru/", organizationPosition1);

        Organization.Position organizationPosition2 = new Organization.Position(
                "Java архитектор",
                LocalDate.of(2012, 4, 1),
                LocalDate.of(2014, 10, 1), "Организация процесса разработки системы ERP для разных окружений");
        Organization workOrganization2 = new Organization("RIT Center", null, organizationPosition2);

        OrganizationSection experience = new OrganizationSection(workOrganization1, workOrganization2);
        sections.put(SectionType.EXPERIENCE, experience);

        Organization.Position organizationPosition3 = new Organization.Position(
                "Functional Programming Principles in Scala\" by Martin Odersky",
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1),
                "");

        Organization educationOrganization1 = new Organization("Coursera", "https://www.coursera.org/course/progfun", organizationPosition3);

        Organization.Position organizationPosition4 = new Organization.Position(
                "Аспирантура (программист С, С++)",
                LocalDate.of(1993, 9, 1),
                LocalDate.of(1996, 7, 1),
                "");
        Organization.Position organizationPosition5 = new Organization.Position(
                "Инженер (программист Fortran, C)",
                LocalDate.of(1987, 9, 1),
                LocalDate.of(1993, 7, 1),
                "");
        Organization educationOrganization2 = new Organization("Санкт-Петербургский национальный исследовательский" +
                " университет информационных технологий, механики и оптики", "http://www.ifmo.ru/", organizationPosition4, organizationPosition5);


        OrganizationSection education = new OrganizationSection(educationOrganization1, educationOrganization2);
        sections.put(SectionType.EDUCATION, education);
        resume1.setSections(sections);

        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
        countResumesBeforeTest = storage.size();
    }

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public void save() {
        storage.save(resume4);
        assertEquals(++countResumesBeforeTest, storage.size());
        assertEquals(resume4, storage.get(resume4.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExistResume() {
        storage.save(resume3);
    }

    @Test
    public void get() {
        assertEquals(resume1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(DUMMY);
    }

    @Test
    public void delete() {
        storage.delete(UUID_2);
        assertEquals(--countResumesBeforeTest, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(DUMMY);
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        storage.update(resume1);
        assertEquals(countResumesBeforeTest, storage.size());
        assertEquals(resume1, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(dummyResume);
    }

    @Test
    public void getAllSorted() {
        List<Resume> resumes = Arrays.asList(resume1, resume2, resume3);
        Collections.sort(resumes);
        assertEquals(resumes, storage.getAllSorted());
    }

    @Test
    public void size() {
        assertEquals(countResumesBeforeTest, storage.size());
    }

}