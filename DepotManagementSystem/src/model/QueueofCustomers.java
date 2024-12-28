package model;

import java.util.LinkedList;
import java.util.Queue;

public class QueueofCustomers {
    private final Queue<Customer> customerQueue;
    private final Log log;

    public QueueofCustomers() {
        customerQueue = new LinkedList<>();
        log = Log.getInstance();
    }

    public void addCustomer(Customer customer) {
        customerQueue.offer(customer);
        log.logCustomerEvent(customer, "joined the queue");
    }

    public Customer removeCustomer() {
        Customer customer = customerQueue.poll();
        if (customer != null) {
            log.logCustomerEvent(customer, "left the queue");
        }
        return customer;
    }

    public Customer peekCustomer() {
        return customerQueue.peek();
    }

    public boolean isEmpty() {
        return customerQueue.isEmpty();
    }

    public int size() {
        return customerQueue.size();
    }

    public Queue<Customer> getQueue() {
        return new LinkedList<>(customerQueue); // Return a copy to maintain encapsulation
    }
}