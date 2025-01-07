package controller;

import model.*;
import view.CustomerView;
import view.ParcelView;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Manager {
    private QueueofCustomers customerQueue;
    private ParcelMap parcelMap;
    private Log log;
    private Worker worker;

    private CustomerView customerView;
    private ParcelView parcelView;

    public Manager() {
        customerQueue = new QueueofCustomers();
        parcelMap = new ParcelMap();
        log = Log.getInstance();
        worker = new Worker();

        
        try {
            parcelMap.loadParcelsFromFile("Parcels.csv");
            customerQueue.loadCustomersFromFile("Custs.csv", parcelMap);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + e.getMessage());
        }

        
        customerView = new CustomerView(customerQueue);
        parcelView = new ParcelView(parcelMap);
    }

    public void createGUI() {
        JFrame frame = new JFrame("Parcel Management System");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(parcelView.getPanel());
        mainPanel.add(customerView.getPanel());

        
        JButton markAsDoneButton = new JButton("Mark as Done");
        markAsDoneButton.addActionListener(e -> processSelectedCustomer());

        JButton addButton = new JButton("Add Parcel and Customer");
        addButton.addActionListener(e -> openAddParcelCustomerUI());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(markAsDoneButton);
        buttonPanel.add(addButton);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void processSelectedCustomer() {
        String customerName = customerView.getSelectedCustomerName();
        if (customerName == null) {
            JOptionPane.showMessageDialog(null, "Please select a customer to process.");
            return;
        }

        Customer customer = customerQueue.getCustomerByName(customerName);

        if (customer != null) {
            
            double totalFee = 0;
            StringBuilder parcelDetails = new StringBuilder();
            for (Parcel parcel : customer.getParcels()) {
                totalFee += worker.calculateFee(parcel);

                parcelDetails.append("Parcel ID: ").append(parcel.getParcelID())
                             .append(", Weight: ").append(parcel.getWeight())
                             .append("kg, Days in Depot: ").append(parcel.getDaysInDepot())
                             .append(", Dimensions: ").append(parcel.getLength()).append("x")
                             .append(parcel.getWidth()).append("x").append(parcel.getHeight())
                             .append("\n");
            }

            
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Customer: " + customer.getName() +
                            "\nTotal Fee: $" + totalFee +
                            "\n\nDo you want to mark this customer as done?",
                    "Confirm Mark as Done",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                
                for (Parcel parcel : customer.getParcels()) {
                    parcelMap.removeParcel(parcel.getParcelID());
                }

                log.logDeliveredParcelDetails(customer, parcelDetails.toString(), totalFee);
                log.addEvent("Processed customer: " + customer.getName() + ", Total Fee: $" + totalFee);

                customerQueue.removeCustomer(customer);

                
                saveAllParcelsToFile();
                saveAllCustomersToFile();
                parcelView.refresh();
                customerView.refresh();

                
                try {
                    log.saveLogToFile("log.txt");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error saving log to file: " + e.getMessage());
                }

                JOptionPane.showMessageDialog(null, "Customer and associated parcels removed successfully.");
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "Selected customer not found.");
        }
    }


    private void openAddParcelCustomerUI() {
        JFrame addFrame = new JFrame("Add Parcel and Customer");
        addFrame.setSize(400, 400);

        JPanel panel = new JPanel(new GridLayout(8, 2));

        
        JTextField parcelIDField = new JTextField();
        JSpinner weightSpinner = new JSpinner(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1)); 
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 365, 1)); 
        JSpinner lengthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1)); 
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1)); 
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1)); 
        JTextField customerNameField = new JTextField();

        
        panel.add(new JLabel("Parcel ID:"));
        panel.add(parcelIDField);

        panel.add(new JLabel("Weight (kg):"));
        panel.add(weightSpinner);

        panel.add(new JLabel("Days in Depot:"));
        panel.add(daysSpinner);

        panel.add(new JLabel("Length (cm):"));
        panel.add(lengthSpinner);

        panel.add(new JLabel("Width (cm):"));
        panel.add(widthSpinner);

        panel.add(new JLabel("Height (cm):"));
        panel.add(heightSpinner);

        panel.add(new JLabel("Customer Name:"));
        panel.add(customerNameField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                
                String parcelID = parcelIDField.getText().trim();
                double weight = (double) weightSpinner.getValue();
                int daysInDepot = (int) daysSpinner.getValue();
                int length = (int) lengthSpinner.getValue();
                int width = (int) widthSpinner.getValue();
                int height = (int) heightSpinner.getValue();
                String customerName = customerNameField.getText().trim();

                if (parcelID.isEmpty() || customerName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Parcel ID and Customer Name cannot be empty.");
                    return;
                }

                
                Parcel newParcel = new Parcel(parcelID, weight, daysInDepot, length, width, height);
                parcelMap.addParcel(newParcel);

                Customer newCustomer = new Customer(customerName, "");
                newCustomer.addParcel(newParcel);
                customerQueue.addCustomer(newCustomer);

                
                saveAllParcelsToFile();
                saveAllCustomersToFile();
                parcelView.refresh();
                customerView.refresh();

                JOptionPane.showMessageDialog(null, "Parcel and Customer added successfully!");
                addFrame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
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
