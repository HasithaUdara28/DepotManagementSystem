package view;

import model.Customer;
import model.Parcel;

import javax.swing.*;
import java.util.List;

public class CustomerView {
    private JTextArea outputArea;

    public CustomerView(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    public void displayCustomers(List<Customer> customers) {
        outputArea.append("Customer List:\n");
        for (Customer customer : customers) {
            outputArea.append("Customer: " + customer.getName() + ", Contact: " + customer.getContact() + "\n");
            outputArea.append("Parcels:\n");
            for (Parcel parcel : customer.getParcels()) {
                outputArea.append("   - Parcel ID: " + parcel.getParcelID() + "\n");
            }
        }
        outputArea.append("\n");
    }

    public void displayCustomerDetails(Customer customer) {
        outputArea.append("Customer Details:\n");
        outputArea.append("Name: " + customer.getName() + "\n");
        outputArea.append("Contact: " + customer.getContact() + "\n");
        outputArea.append("Parcels:\n");
        for (Parcel parcel : customer.getParcels()) {
            outputArea.append("   - Parcel ID: " + parcel.getParcelID() + "\n");
        }
        outputArea.append("\n");
    }
}
