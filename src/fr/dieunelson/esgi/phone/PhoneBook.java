package fr.dieunelson.esgi.phone;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

public class PhoneBook extends Component implements ActionListener, ListSelectionListener {
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
    private JTextPane textPane1;
    private JButton pathChooser;
    private boolean newContact;
    private Stack<String> history;
    private String path;

    public PhoneBook() {
        this.contacts = new ContactList();
        this.history = new Stack<>();
        newContact = false;
        deleteButton.addActionListener(this);
        saveButton.addActionListener(this);
        newButton.addActionListener(this);
        importButton.addActionListener(this);
        exportButton.addActionListener(this);
        filterButton.addActionListener(this);
        list1.addListSelectionListener(this);
        list1.setListData(this.contacts.getKeys().toArray(new String[0]));
    }

    public PhoneBook(String path) throws IOException {
        this();
        this.path = Paths.get(path).toAbsolutePath().toString();
        String data = this.readFile();
        this.contacts.build(data);
        this.textPane1.setText(this.path);
        this.list1.setListData(this.contacts.getKeys().toArray());
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
            if (args.length>=1){
                System.out.println(args[0]);
                new PhoneBook(args[0]).start();
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

    private void chooseFilePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select File...");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .phone-book files", "phone-book");
        chooser.addChoosableFileFilter(restrict);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): "
                    +  chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : "
                    +  chooser.getSelectedFile());

            this.path = chooser.getSelectedFile().getAbsolutePath();
            if (!this.path.substring(this.path.length()-10, this.path.length()).equals("phone-book")){
                this.path+=".phone-book";
            }
            this.textPane1.setText(this.path);
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource().equals(this.newButton)) {
                this.createNewContact();
                this.save();
            }else if (e.getSource().equals(this.saveButton) && this.newContact) {
                this.create();
                this.save();
            }else if (e.getSource().equals(this.saveButton) && !this.newContact) {
                this.update();
                this.save();
            }else if (e.getSource().equals(this.deleteButton)) {
                this.delete();
                this.save();
            }else if (e.getSource().equals(this.exportButton)) {
                    this.chooseFilePath();
                    this.save();
            }else if (e.getSource().equals(this.importButton)) {
                    this.chooseFilePath();
                    String data = this.readFile();
                    this.contacts.build(data);
                    this.list1.setListData(this.contacts.getKeys().toArray());
            }
        } catch (IOException ioException) {
            //ioException.printStackTrace();
        } catch (NullPointerException ioException) {
            //ioException.printStackTrace();
        }
    }

    private void save() throws IOException {
        if (this.path!=null) {
            this.writeFile();
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

    private String readFile() throws IOException, NullPointerException {
        List<String> lines = Files.readAllLines(Paths.get(this.path));
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line);
        }
        return builder.toString();
    }

    private void writeFile() throws IOException {
        Files.write(Paths.get(this.path), this.contacts.export().getBytes());
    }
}
