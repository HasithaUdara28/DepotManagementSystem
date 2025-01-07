package controller;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {
    private QueueofCustomers customerQueue;
    private ParcelMap parcelMap;
    private Log log;
    private Worker worker;

    private JTable parcelTable;
    private JTable customerTable;
    private DefaultTableModel parcelTableModel;
    private DefaultTableModel customerTableModel;

    public Manager() {
        customerQueue = new QueueofCustomers();
        parcelMap = new ParcelMap();
        log = Log.getInstance();
        worker = new Worker();

        // Load data
        try {
            parcelMap.loadParcelsFromFile("Parcels.csv");
            customerQueue.loadCustomersFromFile("Custs.csv", parcelMap);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }
    }

    public void createGUI() {
        JFrame frame = new JFrame("Parcel Management System");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Parcel Table
        parcelTableModel = new DefaultTableModel(new String[]{"Parcel ID", "Weight", "Days in Depot", "Dimensions (LxWxH)"}, 0);
        parcelTable = new JTable(parcelTableModel);
        loadParcelTable();

        JScrollPane parcelScrollPane = new JScrollPane(parcelTable);
        parcelScrollPane.setBorder(BorderFactory.createTitledBorder("Parcels"));

        // Customer Table
        customerTableModel = new DefaultTableModel(new String[]{"Customer Name", "Parcel IDs"}, 0);
        customerTable = new JTable(customerTableModel);
        loadCustomerTable();

        JScrollPane customerScrollPane = new JScrollPane(customerTable);
        customerScrollPane.setBorder(BorderFactory.createTitledBorder("Customers"));

        // Buttons
        JButton markAsDoneButton = new JButton("Mark as Done");
        markAsDoneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSelectedCustomer();
            }
        });

        JButton addButton = new JButton("Add Parcel and Customer");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddParcelCustomerUI();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(markAsDoneButton);
        buttonPanel.add(addButton);

        // Layout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(parcelScrollPane);
        mainPanel.add(customerScrollPane);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadParcelTable() {
        parcelTableModel.setRowCount(0); // Clear existing rows
        for (Parcel parcel : parcelMap.getAllParcels()) {
            parcelTableModel.addRow(new Object[]{
                    parcel.getParcelID(),
                    parcel.getWeight(),
                    parcel.getDaysInDepot(),
                    parcel.getLength() + "x" + parcel.getWidth() + "x" + parcel.getHeight() // Dimensions
            });
        }
    }

    private void loadCustomerTable() {
        customerTableModel.setRowCount(0); // Clear existing rows
        for (Customer customer : customerQueue.getAllCustomers()) {
            StringBuilder parcelIDs = new StringBuilder();
            for (Parcel parcel : customer.getParcels()) {
                if (parcelIDs.length() > 0) {
                    parcelIDs.append(", ");
                }
                parcelIDs.append(parcel.getParcelID());
            }
            customerTableModel.addRow(new Object[]{
                    customer.getName(),
                    parcelIDs.toString()
            });
        }
    }

    private void processSelectedCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a customer to process.");
            return;
        }

        // Retrieve selected customer
        String customerName = (String) customerTableModel.getValueAt(selectedRow, 0);
        Customer customer = customerQueue.getCustomerByName(customerName);

        if (customer != null) {
            // Calculate total fee for the customer
            double totalFee = 0;
            StringBuilder parcelDetails = new StringBuilder();
            for (Parcel parcel : customer.getParcels()) {
                totalFee += worker.calculateFee(parcel);

                // Add parcel details to log entry
                parcelDetails.append("Parcel ID: ").append(parcel.getParcelID())
                             .append(", Weight: ").append(parcel.getWeight())
                             .append("kg, Days in Depot: ").append(parcel.getDaysInDepot())
                             .append(", Dimensions: ").append(parcel.getLength()).append("x")
                             .append(parcel.getWidth()).append("x").append(parcel.getHeight())
                             .append("\n");

                // Remove the parcel from ParcelMap
                parcelMap.removeParcel(parcel.getParcelID());
            }

            // Log the delivery details
            log.logDeliveredParcelDetails(customer, parcelDetails.toString(), totalFee);

            // Log event and remove the customer
            log.addEvent("Processed customer: " + customer.getName() + ", Total Fee: $" + totalFee);
            customerQueue.removeCustomer(customer);

            // Save updated state to CSV files
            saveAllParcelsToFile();
            saveAllCustomersToFile();

            // Refresh tables
            loadParcelTable();
            loadCustomerTable();

            // Save log to file after processing
            try {
                log.saveLogToFile("log.txt");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving log to file: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(null, "Customer and associated parcels removed successfully. Total Fee: $" + totalFee);
        } else {
            JOptionPane.showMessageDialog(null, "Selected customer not found.");
        }
    }

    private void openAddParcelCustomerUI() {
        JFrame addFrame = new JFrame("Add Parcel and Customer");
        addFrame.setSize(400, 400);

        JPanel panel = new JPanel(new GridLayout(8, 2));
        panel.add(new JLabel("Parcel ID:"));
        JTextField parcelIDField = new JTextField();
        panel.add(parcelIDField);

        panel.add(new JLabel("Weight (kg):"));
        JTextField weightField = new JTextField();
        panel.add(weightField);

        panel.add(new JLabel("Days in Depot:"));
        JTextField daysField = new JTextField();
        panel.add(daysField);

        panel.add(new JLabel("Length (cm):"));
        JTextField lengthField = new JTextField();
        panel.add(lengthField);

        panel.add(new JLabel("Width (cm):"));
        JTextField widthField = new JTextField();
        panel.add(widthField);

        panel.add(new JLabel("Height (cm):"));
        JTextField heightField = new JTextField();
        panel.add(heightField);

        panel.add(new JLabel("Customer Name:"));
        JTextField customerNameField = new JTextField();
        panel.add(customerNameField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get input data
                    String parcelID = parcelIDField.getText().trim();
                    double weight = Double.parseDouble(weightField.getText().trim());
                    int daysInDepot = Integer.parseInt(daysField.getText().trim());
                    int length = Integer.parseInt(lengthField.getText().trim());
                    int width = Integer.parseInt(widthField.getText().trim());
                    int height = Integer.parseInt(heightField.getText().trim());
                    String customerName = customerNameField.getText().trim();

                    // Validate inputs
                    if (parcelID.isEmpty() || customerName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Parcel ID and Customer Name cannot be empty.");
                        return;
                    }

                    // Create new parcel and customer
                    Parcel newParcel = new Parcel(parcelID, weight, daysInDepot, length, width, height);
                    parcelMap.addParcel(newParcel);

                    Customer newCustomer = new Customer(customerName, "");
                    newCustomer.addParcel(newParcel);
                    customerQueue.addCustomer(newCustomer);

                    // Save to CSV files
                    saveAllParcelsToFile();
                    saveAllCustomersToFile();

                    // Refresh tables
                    loadParcelTable();
                    loadCustomerTable();

                    JOptionPane.showMessageDialog(null, "Parcel and Customer added successfully!");
                    addFrame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numeric values for weight, days, and dimensions.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error adding parcel and customer: " + ex.getMessage());
                }
            }
        });

        panel.add(saveButton);
        addFrame.add(panel);
        addFrame.setVisible(true);
    }

    private void saveAllParcelsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Parcels.csv"))) {
            for (Parcel parcel : parcelMap.getAllParcels()) {
                writer.write(parcel.getParcelID() + "," +
                        parcel.getDaysInDepot() + "," +
                        parcel.getWeight() + "," +
                        parcel.getLength() + "," +
                        parcel.getWidth() + "," +
                        parcel.getHeight() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving parcels to file: " + e.getMessage());
        }
    }

    private void saveAllCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Custs.csv"))) {
            for (Customer customer : customerQueue.getAllCustomers()) {
                StringBuilder parcelIDs = new StringBuilder();
                for (Parcel parcel : customer.getParcels()) {
                    if (parcelIDs.length() > 0) {
                        parcelIDs.append(",");
                    }
                    parcelIDs.append(parcel.getParcelID());
                }
                writer.write(customer.getName() + "," + parcelIDs.toString() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving customers to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Manager().createGUI();
    }
}
