package fr.dieunelson.esgi.phone;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class Contact {

    private String name;
    private String phoneNumber;
    private Optional<String> data;

    public Contact(String name, String phoneNumber) {
        this(name,phoneNumber, Optional.empty());
    }

    public Contact(String name, String phoneNumber, Optional data) {
        this.name = name.trim();
        this.phoneNumber = phoneNumber.trim();
        this.data = data;
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

    public void setData(Optional<String> data) {
        this.data = data;
    }

    public Optional<String> getData() {
        return data;
    }
}
