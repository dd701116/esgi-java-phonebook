package fr.dieunelson.esgi.phone;

import java.util.HashMap;
import java.util.Set;

public class Contact {

    private String name;
    private String phoneNumber;
    private HashMap<String, String> data;

    public Contact(String name, String phoneNumber) {
        this.name = name.trim();
        this.phoneNumber = phoneNumber.trim();
        this.data = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim();
    }

    public void add(String name, String value) {
        this.data.put(name.trim(), value.trim());
    }

    public String get(String key) {
        return this.data.get(key);
    }

    public String delete(String key) {
        return this.data.remove(key);
    }

    public Set<String> getDataKey() {
        return this.data.keySet();
    }
}
