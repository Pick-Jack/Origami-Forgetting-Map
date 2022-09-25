package com.origami.forgettingmap;

import java.time.Instant;

public class ValueTracker<T> {

    private T key;
    private Instant created;
    private Instant lastOperation;
    private long operationCount;

    public ValueTracker(T key) {
        this.key = key;
        this.created = Instant.now();
    }

    /**
     * Returns the value of the key that is 
     * being tracked by the object isntance.
     * @return
     */
    public T getKey() {
        return this.key;
    }

    /**
     * Returns the UTC Instant captured when 
     * the object was first initialised.
     * @return
     */
    public Instant getInstantCreated() {
        return this.created;
    }

    /**
     * Returns the total number of operations 
     * that have been executed on the targeted 
     * entry instance.
     * @return number of operations executed.
     */
    public long getOperationCount() {
        return operationCount;
    }

    /**
     * Returns the UTC Instant the last 
     * operation was executed for the 
     * entry instance.
     * @return UTC Instant object instance.
     */
    public Instant getLastOperation() {
        return lastOperation;
    }

    /**
     * Updates the relevant class memembers 
     * tracking operations against instances 
     * of this object.
     */
    public void recordOperation() {
        this.lastOperation = Instant.now();
        this.operationCount++;
    }

}


