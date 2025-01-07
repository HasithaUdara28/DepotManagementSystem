package model;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class QueueofCustomers {
    private Queue<Customer> customerQueue;

    public QueueofCustomers() {
        customerQueue = new LinkedList<>();
    }

    public void addCustomer(Customer customer) {
        customerQueue.add(customer);
    }

    public Customer processCustomer() {
        return customerQueue.poll();
    }

    public boolean isEmpty() {
        return customerQueue.isEmpty();
    }

    public List<Customer> getAllCustomers() {
        return new LinkedList<>(customerQueue);
    }

    public Customer getCustomerByName(String name) {
        for (Customer customer : customerQueue) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    public void removeCustomer(Customer customer) {
        customerQueue.remove(customer);
    }

    public void loadCustomersFromFile(String fileName, ParcelMap parcelMap) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String customerName = parts[0];
                Customer customer = new Customer(customerName, "");

                for (int i = 1; i < parts.length; i++) {
                    Parcel parcel = parcelMap.getParcel(parts[i]);
                    if (parcel != null) {
                        customer.addParcel(parcel);
                    }
                }
                addCustomer(customer);
            }
        }
    }
}
