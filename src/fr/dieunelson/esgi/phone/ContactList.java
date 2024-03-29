package fr.dieunelson.esgi.phone;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

public class ContactList implements Exportable{
    private Map<String, Contact> contacts;

    public ContactList() {
        this.contacts = new TreeMap<>();
    }

    public void add(String name, String phoneNumber, String note) {
        this.addContact(name, new Contact(name, phoneNumber, Optional.ofNullable(note)));
    }

    private void addContact(String name, Contact contact) {
        this.contacts.put(name, contact);
    }

    public void delete(String name) {
        this.contacts.remove(name);
    }

    public void replace(String name, String phoneNumber, String note) {
        this.delete(name);
        this.add(name, phoneNumber, note);
    }

    public void replace(String target, String name, String phoneNumber, String note) {
        this.delete(target);
        this.add(name, phoneNumber, note);
    }

    public Contact get(String name) {
        return this.contacts.get(name);
    }

    public Set<String> getKeys() {
        return this.contacts.keySet();
    }

    @Override
    public String export() {
        StringBuilder builder = new StringBuilder();
        Contact contact;
        for (String name: this.contacts.keySet()) {
            contact = this.contacts.get(name);
            builder.append(contact.getName()+"SEPARATOR/PARAM");
            builder.append(contact.getPhoneNumber()+"SEPARATOR/PARAM");
            builder.append(contact.getData().get()+"SEPARATOR/ITEM");
        }
        return builder.toString();
    }

    @Override
    public void build(String data) {
        this.reset();
        this.buildAppend(data);
    }

    public void buildAppend(String data) {
        System.out.println(data);
        String[] contacts = data.split("SEPARATOR/ITEM");
        String[] contactBuilder;
        Contact contact;
        String note;
        System.out.println(contacts.length);
        for (String data1 : contacts) {
            note = null;
            contactBuilder = data1.split("SEPARATOR/PARAM");
            System.out.println(contactBuilder.length);
            if (contactBuilder.length>2) {
               note  = contactBuilder[2];
            }
            contact = new Contact(contactBuilder[0], contactBuilder[1], Optional.ofNullable(note));
            this.addContact(contact.getName(), contact);
        }
    }

    private void reset() {
        this.contacts = new TreeMap<>();
    }
}
