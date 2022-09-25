package com.origami.forgettingmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of a map which holds a configurable number of entries. 
 * When the entry limit is reached, an element is "forgotten" and removed 
 * from the map. The element selected to be removed is the one with the 
 * least number of operations. Draws are resolved using the time last 
 * updated and created, in that order. 
 * 
 * Thread safety has been ensured via the use of Collections.sychronizedMap
 * and use of the sycnhronization context when performing any operation on
 * the map.
 */
public class ForgettingMap<K,V> {

    private long maxEntries;
    private Map<K, V> valueMap;
    private Map<K, ValueTracker<K>> trackingMap;
    private ValueTrackerComparator<K> trackerComparator;
    
    
    public ForgettingMap(long maxEntries) {
        this.maxEntries = maxEntries;
        this.valueMap = Collections.synchronizedMap(new HashMap<K,V>());
        this.trackingMap = Collections.synchronizedMap(new HashMap<K,ValueTracker<K>>());
        this.trackerComparator = new ValueTrackerComparator<>();
    }

    /**
     * Returns the value associated with the given key. 
     * If the key does not exist then returns null.
     * @param key value of the Key.
     * @return
     */
    public synchronized V getValue(K key) {
        if (!valueMap.containsKey(key)) return null;
        trackingMap.get(key).recordOperation();
        return valueMap.get(key);
    }

    /**
     * Puts a new value in the map with the given key. 
     * If the entry already exists then the value 
     * associated with the key is updated.
     * 
     * If the map has reached it's maximum number of 
     * entries, an entry is forgotten.
     * 
     * @param key value to use for the key.
     * @param value the value to store.
     * @return the stored value.
     */
    public synchronized V put(K key, V value) {        
        if (!valueMap.containsKey(key)) 
            if (valueMap.keySet().size() >= this.maxEntries) this.forgetValue();
            trackingMap.put(key, new ValueTracker<K>(key));

        trackingMap.get(key).recordOperation();
        return valueMap.put(key, value);
    }

    /**
     * Initialise a list of tracker instances and 
     * sort using the compator initialised at 
     * initialisation of the map. The last element 
     * in the list represents the key/value that 
     * should be forgotten.
     */
    private synchronized void forgetValue() {
        List<ValueTracker<K>> trackers = new ArrayList<>(trackingMap.values());
        trackers.sort(trackerComparator);
        
        ValueTracker<K> lastTracker = trackers.get(trackers.size()-1);
        valueMap.remove(lastTracker.getKey());
        trackingMap.remove(lastTracker.getKey());
    }

    /**
     * Removes the value associated with the key 
     * from the map.
     * @param key key to remove.
     * @return value associated with the removed key.
     */
    public synchronized V remove(K key) {
        trackingMap.remove(key);
        return valueMap.remove(key);
    }

    /**
     * Returns the number of key-value pairs 
     * stored by the map.
     * @return
     */
    public int size() {
        return valueMap.size();
    }

}
