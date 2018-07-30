package com.urise.webapp.storage.serializer;


import com.urise.webapp.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class DataStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(OutputStream os, Resume r) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(os)) {
            dos.writeUTF(r.getUuid());
            dos.writeUTF(r.getFullName());

            writeCollection(dos, r.getContacts().entrySet(), entry -> {
                dos.writeUTF(entry.getKey().name());
                dos.writeUTF(entry.getValue());
            });

            writeCollection(dos, r.getSections().entrySet(), entry -> {
                SectionType sectionType = entry.getKey();
                dos.writeUTF(sectionType.name());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        dos.writeUTF(((TextSection) entry.getValue()).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        writeCollection(dos, ((ListSection) entry.getValue()).getItems(), dos::writeUTF);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        writeCollection(dos, ((OrganizationSection) entry.getValue()).getSection(), organization -> {
                            dos.writeUTF(organization.getHomePage().getName());
                            dos.writeUTF(Optional.ofNullable(organization.getHomePage().getUrl()).orElse("null"));
                            writeCollection(dos, organization.getPositions(), position -> {
                                dos.writeUTF(position.getTitle());
                                dos.writeUTF(position.getDateBegin().toString());
                                dos.writeUTF(position.getDateEnd().toString());
                                dos.writeUTF(position.getText());
                            });
                        });
                }
            });
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try (DataInputStream dis = new DataInputStream(is)) {
            String uuid = dis.readUTF();
            String fullName = dis.readUTF();
            Resume resume = new Resume(uuid, fullName);
            readItems(dis, () -> resume.addContact(ContactType.valueOf(dis.readUTF()), dis.readUTF()));

            readItems(dis, () -> {
                SectionType sectionType = SectionType.valueOf(dis.readUTF());
                switch (sectionType) {
                    case PERSONAL:
                    case OBJECTIVE:
                        resume.addSection(sectionType, new TextSection(dis.readUTF()));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        ListSection listSection = new ListSection();
                        readItems(dis, () -> listSection.addItem(dis.readUTF()));
                        resume.addSection(sectionType, listSection);
                        break;
                    case EDUCATION:
                    case EXPERIENCE:
                        List<Organization> organizations = new ArrayList<>();
                        readItems(dis, () -> {
                            String name = dis.readUTF();
                            String url = dis.readUTF();
                            Link homePage = new Link(name, (url.equals("null") ? null : url));
                            List<Organization.Position> positions = new ArrayList<>();
                            readItems(dis, () -> positions.add(new Organization.Position(
                                    dis.readUTF(),
                                    LocalDate.parse(dis.readUTF()),
                                    LocalDate.parse(dis.readUTF()),
                                    dis.readUTF())));
                            organizations.add(new Organization(homePage, positions));
                        });
                        resume.addSection(sectionType, new OrganizationSection(organizations));
                }
            });
            return resume;
        }
    }

    interface Writer<T> {
        void write(T t) throws IOException;
    }

    interface Reader {
        void read() throws IOException;
    }

    private <T> void writeCollection(DataOutputStream dos, Collection<T> collection, Writer<T> writer) throws IOException {
        dos.writeInt(collection.size());
        for (T t : collection) {
            writer.write(t);
        }
    }

    private void readItems(DataInputStream dis, Reader reader) throws IOException {
        int size = dis.readInt();
        for (int i = 0; i < size; i++) {
            reader.read();
        }
    }

}
