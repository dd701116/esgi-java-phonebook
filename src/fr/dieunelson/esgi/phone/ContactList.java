package fr.dieunelson.esgi.phone;

import java.util.Map;
import java.util.TreeMap;

public class ContactList implements Exportable{
    private Map<String, Contact> contacts;

    public ContactList() {
        this.contacts = new TreeMap<>();
    }

    public void add(String name, String phoneNumber) {
        this.addContact(name, new Contact(name, phoneNumber));
    }

    private void addContact(String name, Contact contact) {
        this.contacts.put(name, contact);
    }

    public void delete(String name) {
        this.contacts.remove(name);
    }

    public void replace(String name, String phoneNumber) {
        this.delete(name);
        this.add(name, phoneNumber);
    }

    public Contact get(String name) {
        return this.contacts.get(name);
    }

    @Override
    public String export() {
        StringBuilder builder = new StringBuilder();
        StringBuilder contactBuilder = new StringBuilder();
        String data;
        Contact contact;
        boolean haveData = false;
        for (String name: this.contacts.keySet()) {
            haveData = false;
            contact = this.contacts.get(name);
            contactBuilder.append(contact.getName()+",");
            contactBuilder.append(contact.getPhoneNumber()+",");
            for(String key : contact.getDataKey()) {
                contactBuilder.append(key+"|-|"+contact.get(key)+"/+/");
                haveData = true;
            }
            data = contactBuilder.toString();
            if (haveData) {
                builder.append(data.substring(0,data.length()-3)+";");
            }else{
                builder.append(data.substring(0,data.length()-1)+";");
            }
        }
        return builder.toString();
    }

    @Override
    public void build(String data) {
        this.reset();
        this.buildAppend(data);
    }

    public void buildAppend(String data) {
        String[] contacts = data.split(";");
        String[] contactBuilder;
        String[] contactDataBuilder;
        String[] contactDataBuilder2;
        Contact contact;
        for (String data1 : contacts) {
            contactBuilder = data1.split(",");
            contact = new Contact(contactBuilder[0], contactBuilder[1]);
            if (contactBuilder.length>2) {
                contactDataBuilder = contactBuilder[0].split("/+/");
                for (String data2 : contactDataBuilder) {
                    contactDataBuilder2 = data2.split("|-|");
                    contact.add(contactDataBuilder2[0], contactDataBuilder2[1]);
                }
            }
            this.addContact(contact.getName(), contact);
        }
    }

    private void reset() {
        this.contacts = new TreeMap<>();
    }
}
