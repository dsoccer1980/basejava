package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static com.urise.webapp.util.DateUtil.NOW;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final String STORAGE_DIR = Config.get().getStorageDir();
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
        Path dir = Paths.get(STORAGE_DIR);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        storage.clear();
        Map<ContactType, String> contacts = new HashMap<>();
        contacts.put(ContactType.TELEFON, "+7(981) 916-2219");
        contacts.put(ContactType.SKYPE, "dsoccer1980");
        contacts.put(ContactType.EMAIL, "dsoccer1980@gmail.com");
        resume1.setContacts(contacts);
        Map<SectionType, Section> sections = new HashMap<>();
        sections.put(SectionType.OBJECTIVE, new TextSection("Java Developer"));
        sections.put(SectionType.PERSONAL, new TextSection("Трудолюбивый, целеустремленный"));
        sections.put(SectionType.ACHIEVEMENT,
                new ListSection(Arrays.asList("Разработка Spring/JPA Enterprise application", "Разработка basejava application")));
        sections.put(SectionType.QUALIFICATIONS,
                new ListSection(Arrays.asList("Java Core", "PostgreSQL")));

        Organization.Position organizationPosition1 = new Organization.Position(
                "Системный администратор",
                LocalDate.of(2002, 11, 1),
                NOW, "Администрирование сети, поддержка пользователей");


        Organization workOrganization1 = new Organization("Orica Eesti", "http://www.orica.com", organizationPosition1);

        Organization.Position organizationPosition2 = new Organization.Position(
                "Стажер",
                LocalDate.of(2018, 6, 1),
                LocalDate.of(2018, 12, 1), "Стажировка на курсе BaseJava ");
        Organization workOrganization2 = new Organization("JawaWebinar", null, organizationPosition2);

        OrganizationSection experience = new OrganizationSection(workOrganization1, workOrganization2);
        sections.put(SectionType.EXPERIENCE, experience);

        Organization.Position organizationPosition3 = new Organization.Position(
                "Ученик",
                LocalDate.of(2018, 6, 1),
                LocalDate.of(2018, 12, 1),
                "");

        Organization educationOrganization1 = new Organization("Java Online Projects", "http://javaops.ru/", organizationPosition3);

        Organization.Position organizationPosition4 = new Organization.Position(
                "Software and database management",
                LocalDate.of(2011, 9, 1),
                LocalDate.of(2013, 6, 1),
                "");
        Organization.Position organizationPosition5 = new Organization.Position(
                "Bachelor Degree in Informatics",
                LocalDate.of(1998, 9, 1),
                LocalDate.of(2002, 7, 1),
                "");
        Organization educationOrganization2 = new Organization("Tartu University", "https://www.ut.ee/ru", organizationPosition4, organizationPosition5);


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
        Map<ContactType, String> contacts = resume1.getContacts();
        contacts.put(ContactType.SKYPE, "NEW SKYPE");
        resume1.setContacts(contacts);
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