package view;

import model.ParcelMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ParcelView {
    private ParcelMap parcelMap;
    private JTable parcelTable;
    private DefaultTableModel parcelTableModel;

    public ParcelView(ParcelMap parcelMap) {
        this.parcelMap = parcelMap;

        parcelTableModel = new DefaultTableModel(new String[]{"Parcel ID", "Weight", "Days in Depot", "Dimensions (LxWxH)"}, 0);
        parcelTable = new JTable(parcelTableModel);
        refresh();
    }

    public JPanel getPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(parcelTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Parcels"));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public void refresh() {
        parcelTableModel.setRowCount(0);
        parcelMap.getAllParcels().forEach(parcel -> parcelTableModel.addRow(new Object[]{
                parcel.getParcelID(),
                parcel.getWeight(),
                parcel.getDaysInDepot(),
                parcel.getLength() + "x" + parcel.getWidth() + "x" + parcel.getHeight()
        }));
    }
}
