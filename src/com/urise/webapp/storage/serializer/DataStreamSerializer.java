package com.urise.webapp.storage.serializer;


import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(OutputStream os, Resume r) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            contacts.forEach((k, v) -> {
                writeUTF(dos, k.name());
                writeUTF(dos, v);
            });

            Map<SectionType, Section> sections = r.getSections();
            dos.writeInt(sections.size());
            for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
                SectionType sectionType = entry.getKey();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) entry.getValue()).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> items = ((ListSection) entry.getValue()).getItems();
                        dos.writeInt(items.size());
                        items.forEach(item -> writeUTF(dos, item));
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> organizationList = ((OrganizationSection) entry.getValue()).getSection();
                        dos.writeInt(organizationList.size());
                        for (Organization organization : organizationList) {
                            dos.writeUTF(organization.getHomePage().getName());
                            dos.writeUTF(Optional.ofNullable(organization.getHomePage().getUrl()).orElse("null"));
                            List<Organization.Position> positions = organization.getPositions();
                            dos.writeInt(positions.size());
                            positions.forEach(position -> {
                                writeUTF(dos, position.getTitle());
                                writeUTF(dos, position.getDateBegin().toString());
                                writeUTF(dos, position.getDateEnd().toString());
                                writeUTF(dos, position.getText());
                            });
                        }
                }
            }
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            IntStream.range(0, dis.readInt())
                    .forEach(i -> resume.addContact(ContactType.valueOf(readUTF(dis)), readUTF(dis)));

            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());

                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = new ListSection();
                        IntStream.range(0, dis.readInt()).forEach(j -> listSection.addItem(readUTF(dis)));
                        resume.addSection(sectionType, listSection);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        int organisationSize = dis.readInt();
                        List<Organization> organizations = new ArrayList<>();
                        for (int j = 0; j < organisationSize; j++) {
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            Link homePage = new Link(name, (url.equals("null") ? null : url));
                            List<Organization.Position> positions = new ArrayList<>();
                            IntStream.range(0, dis.readInt())
                                    .forEach(k -> positions.add(new Organization.Position(
                                            readUTF(dis),
                                            LocalDate.parse(readUTF(dis)),
                                            LocalDate.parse(readUTF(dis)),
                                            readUTF(dis)))
                                    );
                            organizations.add(new Organization(homePage, positions));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizations));
                }
            }
            return resume;
        }
    }

    private void writeUTF(DataOutputStream dos, String item) {
        try {
            dos.writeUTF(item);
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    private String readUTF(DataInputStream dis) {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

}
