package model;

public class Parcel {
    private String parcelID;
    private int daysInDepot;
    private double weight;
    private int width;
    private int length;
    private int height;

    // Constructor
    public Parcel(String parcelID, double weight, int daysInDepot, int length, int width, int height) {
        this.parcelID = parcelID;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.length = length;
        this.width = width;
        this.height = height;
    }

    // Getters and Setters
    public String getParcelID() {
        return parcelID;
    }

    public void setParcelID(String parcelID) {
        this.parcelID = parcelID;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    public void setDaysInDepot(int daysInDepot) {
        this.daysInDepot = daysInDepot;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Parcel[ID=" + parcelID + ", Weight=" + weight + ", DaysInDepot=" + daysInDepot + "]";
    }
}
