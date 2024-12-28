package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private static final Log INSTANCE = new Log();
    private final StringBuffer logBuffer;
    private static final String LOG_FILE = "depot_operations.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Log() {
        logBuffer = new StringBuffer();
        logEvent("Log system initialized");
    }

    public static Log getInstance() {
        return INSTANCE;
    }

    public void logEvent(String event) {
        String timestamp = LocalDateTime.now().format(formatter);
        logBuffer.append(timestamp).append(" - ").append(event).append("\n");
    }

    public void logCustomerEvent(Customer customer, String event) {
        logEvent("Customer " + customer.toString() + ": " + event);
    }

    public void logParcelEvent(Parcel parcel, String event) {
        logEvent("Parcel " + parcel.getParcelID() + ": " + event);
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logBuffer.toString());
            writer.flush();
            logBuffer.setLength(0); // Clear buffer after saving
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentLog() {
        return logBuffer.toString();
    }
}