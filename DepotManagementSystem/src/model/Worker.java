package model;

public class Worker {
    private Customer currentCustomer;
    private Parcel currentParcel;
    private final Log log;
    
    public Worker() {
        log = Log.getInstance();
    }
    
    public double calculateFee() {
        if (currentParcel == null) return 0.0;
        
        // Base rate is $5
        double fee = 5.0;
        
        // Add weight fee ($2 per kg)
        fee += currentParcel.getWeight() * 2;
        
        // Add storage fee ($1 per day after 3 days)
        if (currentParcel.getDaysInDepot() > 3) {
            fee += (currentParcel.getDaysInDepot() - 3) * 1;
        }
        
        // Add size fee (volume in cubic cm / 1000 * $0.5)
        double volume = currentParcel.getLength() * currentParcel.getWidth() * currentParcel.getHeight();
        fee += (volume / 1000) * 0.5;
        
        return Math.round(fee * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    public void processCustomer(Customer customer) {
        this.currentCustomer = customer;
        this.currentParcel = customer.getParcel();
        log.logEvent("Worker started processing customer " + customer.toString());
    }
    
    public void completeProcessing() {
        if (currentCustomer != null) {
            log.logEvent("Completed processing for customer " + currentCustomer.toString() + 
                        " with fee: $" + calculateFee());
            currentCustomer = null;
            currentParcel = null;
        }
    }
    
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }
    
    public Parcel getCurrentParcel() {
        return currentParcel;
    }
}