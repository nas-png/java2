import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistrationForm {
    private JFrame frame;
    private JTextField nameField, mobileField;
    private JRadioButton male, female;
    private JTextField dobField;
    private JTextArea addressArea;
    private JButton submitBtn, resetBtn;

    public RegistrationForm() {
        frame = new JFrame("Registration Form");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(7, 2));

        frame.add(new JLabel("Name:"));
        nameField = new JTextField();
        frame.add(nameField);

        frame.add(new JLabel("Mobile:"));
        mobileField = new JTextField();
        frame.add(mobileField);

        frame.add(new JLabel("Gender:"));
        JPanel genderPanel = new JPanel();
        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);
        genderPanel.add(male);
        genderPanel.add(female);
        frame.add(genderPanel);

        frame.add(new JLabel("DOB (YYYY-MM-DD):"));
        dobField = new JTextField();
        frame.add(dobField);

        frame.add(new JLabel("Address:"));
        addressArea = new JTextArea(2, 20);
        frame.add(addressArea);

        submitBtn = new JButton("Submit");
        resetBtn = new JButton("Reset");
        frame.add(submitBtn);
        frame.add(resetBtn);

        submitBtn.addActionListener(e -> saveData());
        resetBtn.addActionListener(e -> resetFields());

        frame.setVisible(true);
    }

    private void saveData() {
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String gender = male.isSelected() ? "Male" : (female.isSelected() ? "Female" : "");
        String dob = dobField.getText();
        String address = addressArea.getText();

        if (name.isEmpty() || mobile.isEmpty() || gender.isEmpty() || dob.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (name, mobile, gender, dob, address) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, mobile);
            stmt.setString(3, gender);
            stmt.setString(4, dob);
            stmt.setString(5, address);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Registration Successful!");
            resetFields();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error saving data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetFields() {
        nameField.setText("");
        mobileField.setText("");
        genderGroup.clearSelection();
        dobField.setText("");
        addressArea.setText("");
    }

    public static void main(String[] args) {
        new RegistrationForm();
    }
}
