package fr.dieunelson.esgi.phone;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Stack;

public class PhoneBook implements ActionListener, ListSelectionListener {
    private ContactList contacts;
    private JPanel rootPan;
    private JFrame frame;
    private JList list1;
    private JButton deleteButton;
    private JTextArea textAreaNote;
    private JTextField textFieldName;
    private JTextField textFieldPhoneNumber;
    private JButton saveButton;
    private JButton newButton;
    private JButton importButton;
    private JButton exportButton;
    private JButton filterButton;
    private boolean newContact;
    private Stack<String> history;

    public PhoneBook() {
        this.contacts = new ContactList();
        this.history = new Stack<>();
        newContact = false;
        deleteButton.addActionListener(this);
        saveButton.addActionListener(this);
        newButton.addActionListener(this);
        list1.addListSelectionListener(this);
        list1.setListData(this.contacts.getKeys().toArray(new String[0]));
    }

    public PhoneBook(String path) throws IOException {
        this();
        String data = this.readFile(path);
        if (data!=null){
            this.contacts.build(data);
        }

    }

    public void start() {
        this.frame = new JFrame("Phone Book");
        this.frame.setContentPane(this.rootPan);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.pack();
        this.frame.setVisible(true);
        this.frame.setResizable(false);
        this.frame.setSize(1000,600);
    }

    public static void main(String[] args) {

        try {
            if (args.length>2){
                new PhoneBook(args[1]).start();
            }else{
                new PhoneBook().start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewContact() {
        this.newButton.setEnabled(false);
        this.deleteButton.setEnabled(false);
        this.newContact = true;
        this.textFieldName.setText("New Contact");
        this.textFieldPhoneNumber.setText("+33");
        this.textAreaNote.setText("");
        this.saveButton.setEnabled(true);
    }

    private void create() {
        String name = this.textFieldName.getText();
        String phoneNumber = this.textFieldPhoneNumber.getText();
        String note = this.textAreaNote.getText();
        this.contacts.add(name, phoneNumber, note);
        try {
            list1.setListData(this.contacts.getKeys().toArray());
        }catch (NullPointerException e){
            //  Nothing
        }
        this.list1.setSelectedValue(name, true);
        this.newButton.setEnabled(true);
        this.newContact = false;
    }

    private void read(String name) {
        Contact contact = this.contacts.get(name);
        this.history.push(name);
        this.textFieldName.setText(contact.getName());
        this.textFieldPhoneNumber.setText(contact.getPhoneNumber());
        this.textAreaNote.setText(contact.getData().get());
        this.deleteButton.setEnabled(true);
        this.saveButton.setEnabled(true);
    }

    private void update() {
        String name = this.textFieldName.getText();
        String phoneNumber = this.textFieldPhoneNumber.getText();
        String note = this.textAreaNote.getText();
        this.contacts.replace(this.history.peek(), name, phoneNumber, note);
        try {
            list1.setListData(this.contacts.getKeys().toArray());
        }catch (NullPointerException e){
            //  Nothing
        }
        this.list1.setSelectedValue(name, true);
        this.newButton.setEnabled(true);
        this.newContact = false;
    }

    private void delete() {
        Contact contact = this.contacts.get(this.history.peek());
        this.textFieldName.setText("");
        this.textFieldPhoneNumber.setText("");
        this.textAreaNote.setText("");
        this.contacts.delete(this.history.pop());
        try {
            list1.setListData(this.contacts.getKeys().toArray());
        }catch (NullPointerException e){
            //  Nothing
        }
        this.deleteButton.setEnabled(false);
        this.saveButton.setEnabled(false);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.newButton)) {
            this.createNewContact();
        }else if (e.getSource().equals(this.saveButton) && this.newContact) {
            this.create();
        }else if (e.getSource().equals(this.saveButton) && !this.newContact) {
            this.update();
        }else if (e.getSource().equals(this.deleteButton)) {
            this.delete();
        }
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        this.newContact = false;
        JList list = (JList)e.getSource();
        this.read(list.getSelectedValue().toString());
    }

    private String readFile(String filepath) throws IOException {
        File fp = new File(filepath);

        if (fp.exists()) {
            FileReader fr = new FileReader(fp);
            BufferedReader br = new BufferedReader(fr);

            StringBuilder builder = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) { builder.append(line); }

            fr.close();
            return builder.toString();
        }else{
            return null;
        }
    }
}
