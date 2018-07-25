package com.urise.webapp.storage.serializer;


import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(OutputStream os, Resume r) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());
            Map<ContactType, String> contacts = r.getContacts();
            dos.writeInt(contacts.size());
            for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            }

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
                        for (String item : items) {
                            dos.writeUTF(item);
                        }
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
                            for (Organization.Position position : positions) {
                                dos.writeUTF(position.getTitle());
                                dos.writeUTF(position.getDateBegin().toString());
                                dos.writeUTF(position.getDateEnd().toString());
                                dos.writeUTF(position.getText());
                            }
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
            int size = dis.readInt();
            for (int i = 0; i < size; i++) {
                resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF());
            }

            size = dis.readInt();
            for (int i = 0; i < size; i++) {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());

                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        int itemsSize = dis.readInt();
                        ListSection listSection = new ListSection();
                        for (int j = 0; j < itemsSize; j++) {
                            listSection.addItem(dis.readUTF());
                        }
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
                            int positionsSize = dis.readInt();
                            for (int k = 0; k < positionsSize; k++) {
                                String title = dis.readUTF();
                                LocalDate dateBegin = LocalDate.parse(dis.readUTF());
                                LocalDate dateEnd = LocalDate.parse(dis.readUTF());
                                String text = dis.readUTF();
                                positions.add(new Organization.Position(title, dateBegin, dateEnd, text));
                            }
                            organizations.add(new Organization(homePage, positions));
                        }
                        resume.addSection(sectionType, new OrganizationSection(organizations));
                }
            }
            return resume;
        }
    }
}
