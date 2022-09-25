package com.origami.forgettingmap;

import java.util.Comparator;

public class ValueTrackerComparator<K> implements Comparator<ValueTracker<K>> {

    @Override
    public int compare(ValueTracker<K> o1, ValueTracker<K> o2) {
        if (o1.getOperationCount() > o2.getOperationCount()) return -1;
        else if (o1.getOperationCount() < o2.getOperationCount()) return 1;
        else return compareLastUpdated(o1, o2);
    }

    /**
     * Compares the epoch distance of the 
     * last updated instant for each object 
     * instance.
     * @param o1 first object to be compared.
     * @param o2 second object to be compared.
     * @return 1 if o1 was updated more recently, 
     * -1 if less recently. Equality resolved by 
     * instant the objects were created.
     */
    private int compareLastUpdated(ValueTracker<K> o1, ValueTracker<K> o2) {
        if (o1.getLastOperation() == null && o2.getLastOperation() != null) return 1;
        if (o1.getLastOperation() != null && o2.getLastOperation() == null) return -1;
        if (o1.getLastOperation() == null && o2.getLastOperation() == null) return compareCreated(o1, o2);
        
        long epochDist1 = o1.getLastOperation().getEpochSecond();
        long epochDist2 = o2.getLastOperation().getEpochSecond();
        if (epochDist1 > epochDist2) return -1;
        else if (epochDist1 < epochDist2) return 1;
        else return compareCreated(o1, o2);
    }


    /**
     * Compares the epoc distance of the 
     * instant the objects were initialised. 
     * 
     * In the unlikely case that the objects 
     * were created at the same time, the 
     * first object is identified as larger 
     * (i.e. more forgetable).
     * @param o1 first object to be compared.
     * @param o2 second object to be compared.
     * @return 1 if o1 was created more recently, 
     * -1 if less recently.
     */
    private int compareCreated(ValueTracker<K> o1, ValueTracker<K> o2) {
        if (o1.getInstantCreated() == null && o2.getInstantCreated() != null) return 1;
        if (o1.getInstantCreated() != null && o2.getInstantCreated() == null) return -1;
        if (o1.getInstantCreated() == null && o2.getInstantCreated() == null) return 1;
        
        long epochDist1 = o1.getInstantCreated().getEpochSecond();
        long epochDist2 = o2.getInstantCreated().getEpochSecond();
        if (epochDist1 > epochDist2) return -1;
        else if (epochDist1 < epochDist2) return 1;
        else return 1; 
    }

}
