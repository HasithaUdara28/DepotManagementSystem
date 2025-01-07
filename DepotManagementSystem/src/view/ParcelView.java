package view;

import model.Parcel;

import javax.swing.*;
import java.util.List;

public class ParcelView {
    private JTextArea outputArea;

    public ParcelView(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    public void displayParcels(List<Parcel> parcels) {
        outputArea.append("Parcel List:\n");
        for (Parcel parcel : parcels) {
            outputArea.append("Parcel ID: " + parcel.getParcelID() + "\n");
            outputArea.append("   Weight: " + parcel.getWeight() + " kg\n");
            outputArea.append("   Dimensions (LxWxH): " + parcel.getLength() + "x" + parcel.getWidth() + "x" + parcel.getHeight() + " cm\n");
            outputArea.append("   Days in Depot: " + parcel.getDaysInDepot() + "\n\n");
        }
    }

    public void displayParcelDetails(Parcel parcel) {
        outputArea.append("Parcel Details:\n");
        outputArea.append("Parcel ID: " + parcel.getParcelID() + "\n");
        outputArea.append("Weight: " + parcel.getWeight() + " kg\n");
        outputArea.append("Dimensions (LxWxH): " + parcel.getLength() + "x" + parcel.getWidth() + "x" + parcel.getHeight() + " cm\n");
        outputArea.append("Days in Depot: " + parcel.getDaysInDepot() + "\n\n");
    }
}
