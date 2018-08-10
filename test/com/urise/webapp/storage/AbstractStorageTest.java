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
        contacts.put(ContactType.TELEFON, "+7(921) 855-0482");
        contacts.put(ContactType.SKYPE, "grigory.kislin");
        contacts.put(ContactType.EMAIL, "gkislin@yandex.ru");
        resume1.setContacts(contacts);
        Map<SectionType, Section> sections = new HashMap<>();
        sections.put(SectionType.OBJECTIVE, new TextSection("objective content"));
        sections.put(SectionType.PERSONAL, new TextSection("personal content"));
        sections.put(SectionType.ACHIEVEMENT,
                new ListSection(Arrays.asList("achievement1", "achievement2")));
        sections.put(SectionType.QUALIFICATIONS,
                new ListSection(Arrays.asList("qualification1", "qualification2")));

        Organization.Position organizationPosition1 = new Organization.Position(
                "organizationPosition1 title",
                LocalDate.of(2013, 10, 1),
                NOW, "position1 text");


        Organization workOrganization1 = new Organization("workOrganization1 name", "http://javaops.ru/", organizationPosition1);

        Organization.Position organizationPosition2 = new Organization.Position(
                "organizationPosition2 title",
                LocalDate.of(2012, 4, 1),
                LocalDate.of(2014, 10, 1), "organizationPosition2 text");
        Organization workOrganization2 = new Organization("workOrganization2 name", null, organizationPosition2);

        OrganizationSection experience = new OrganizationSection(workOrganization1, workOrganization2);
        sections.put(SectionType.EXPERIENCE, experience);

        Organization.Position organizationPosition3 = new Organization.Position(
                "education position1 title",
                LocalDate.of(2013, 3, 1),
                LocalDate.of(2013, 5, 1),
                "");

        Organization educationOrganization1 = new Organization("educationOrganization1 name", "https://www.coursera.org/course/progfun", organizationPosition3);

        Organization.Position organizationPosition4 = new Organization.Position(
                "education position2 title",
                LocalDate.of(1993, 9, 1),
                LocalDate.of(1996, 7, 1),
                "");
        Organization.Position organizationPosition5 = new Organization.Position(
                "education position3 title",
                LocalDate.of(1987, 9, 1),
                LocalDate.of(1993, 7, 1),
                "");
        Organization educationOrganization2 = new Organization("educationOrganization2 name", "http://www.ifmo.ru/", organizationPosition4, organizationPosition5);


        OrganizationSection education = new OrganizationSection(educationOrganization1, educationOrganization2);
        sections.put(SectionType.EDUCATION, education);
   //     resume1.setSections(sections);

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