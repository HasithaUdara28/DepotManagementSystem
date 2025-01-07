package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private String contact;
    private List<Parcel> parcels;

    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
        this.parcels = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public void addParcel(Parcel parcel) {
        parcels.add(parcel);
    }

    public List<Parcel> getParcels() {
        return parcels;
    }

    @Override
    public String toString() {
        return "Customer[Name=" + name + ", Contact=" + contact + "]";
    }
}
