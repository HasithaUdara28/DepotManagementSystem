package view;

import model.QueueofCustomers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerView {
    private QueueofCustomers customerQueue;
    private JTable customerTable;
    private DefaultTableModel customerTableModel;

    public CustomerView(QueueofCustomers customerQueue) {
        this.customerQueue = customerQueue;

        customerTableModel = new DefaultTableModel(new String[]{"Customer Name", "Parcel IDs"}, 0);
        customerTable = new JTable(customerTableModel);
        refresh();
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Customers"));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public void refresh() {
        customerTableModel.setRowCount(0);
        customerQueue.getAllCustomers().forEach(customer -> {
            String parcelIDs = String.join(", ", customer.getParcels().stream()
                    .map(parcel -> parcel.getParcelID())
                    .toList());
            customerTableModel.addRow(new Object[]{customer.getName(), parcelIDs});
        });
    }

    public String getSelectedCustomerName() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        return (String) customerTableModel.getValueAt(selectedRow, 0);
    }
}
