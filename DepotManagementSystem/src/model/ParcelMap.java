package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParcelMap {
    private Map<String, Parcel> parcelMap;

    public ParcelMap() {
        parcelMap = new HashMap<>();
    }

    public void addParcel(Parcel parcel) {
        parcelMap.put(parcel.getParcelID(), parcel);
    }

    public Parcel getParcel(String parcelID) {
        return parcelMap.get(parcelID);
    }

    public List<Parcel> getAllParcels() {
        return new ArrayList<>(parcelMap.values());
    }

    public boolean containsParcel(String parcelID) {
        return parcelMap.containsKey(parcelID);
    }

    public void removeParcel(String parcelID) {
        parcelMap.remove(parcelID);
    }

    public void loadParcelsFromFile(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String parcelID = parts[0];
                int daysInDepot = Integer.parseInt(parts[1]);
                double weight = Double.parseDouble(parts[2]);
                int length = Integer.parseInt(parts[3]);
                int width = Integer.parseInt(parts[4]);
                int height = Integer.parseInt(parts[5]);

                
                Parcel parcel = new Parcel(parcelID, weight, daysInDepot, length, width, height);
                addParcel(parcel);
            }
        }
    }

}
