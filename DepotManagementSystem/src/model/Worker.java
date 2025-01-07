package model;

import javax.swing.JTextArea;

public class Worker {

    public double calculateFee(Parcel parcel) {
        double baseFee = 5.0;
        double weightFee = parcel.getWeight() * 0.1;
        double timeFee = parcel.getDaysInDepot() * 0.5;
        return baseFee + weightFee + timeFee;
    }

    public void processCustomer(Customer customer, ParcelMap parcelMap, Log log, JTextArea outputArea) {
        outputArea.append("Processing customer: " + customer.getName() + "\n");
        log.addEvent("Started processing customer: " + customer.getName());

        for (Parcel parcel : customer.getParcels()) {
            if (parcelMap.containsParcel(parcel.getParcelID())) {
                double fee = calculateFee(parcel);
                log.addEvent("Parcel ID: " + parcel.getParcelID() + " processed. Fee: $" + fee);
                outputArea.append("Parcel ID: " + parcel.getParcelID() + ", Fee: $" + fee + "\n");
            } else {
                log.addEvent("Parcel ID: " + parcel.getParcelID() + " not found in depot!");
                outputArea.append("Parcel ID: " + parcel.getParcelID() + " not found in depot!\n");
            }
        }

        log.addEvent("Finished processing customer: " + customer.getName());
        outputArea.append("Finished processing customer: " + customer.getName() + "\n\n");
    }
}
