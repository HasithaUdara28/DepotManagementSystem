package model;

import java.io.FileWriter;
import java.io.IOException;

public class Log {
    private static Log instance;
    private StringBuffer logBuffer;

    private Log() {
        logBuffer = new StringBuffer();
    }

    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    public void addEvent(String event) {
        logBuffer.append(event).append("\n");
    }

    public void logDeliveredParcelDetails(Customer customer, String parcelDetails, double totalFee) {
        logBuffer.append("===== Delivered Parcel Details =====\n")
                 .append("Customer Name: ").append(customer.getName()).append("\n")
                 .append("Parcels:\n").append(parcelDetails)
                 .append("Total Fee: $").append(totalFee).append("\n")
                 .append("====================================\n\n");
    }

    public void saveLogToFile(String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(logBuffer.toString());
        }
    }
}
