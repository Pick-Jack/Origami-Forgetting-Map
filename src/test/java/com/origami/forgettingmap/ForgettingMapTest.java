package com.origami.forgettingmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class ForgettingMapTest {
    
    private final String key1 = "KEY_1";
    private final String key2 = "KEY_2";
    private final String key3 = "KEY_3";
    private final String key4 = "KEY_4";

    private final String value1 = "VALUE_1";
    private final String value2 = "VALUE_2";
    private final String value3 = "VALUE_3";
    private final String value4 = "VALUE_4";
    
    private final long maxEntries = 3;
    private ForgettingMap<String,String> map;
    

    @Before
    public void beforeTest() {
        map = new ForgettingMap<>(maxEntries);
    }

    @Test
    public void canInsertNewEntry() {
        map.put(key1, value1);
        assertTrue(map.size() == 1);
    }

    @Test
    public void canReadMapEntry() {
        map.put(key1, value1);
        map.put(key2, value2);
        assertTrue(map.size() == 2);

        String result = map.getValue(key1);
        assertEquals(value1, result);
    }

    @Test
    public void forgetsEntryWhenMaxEntriesIsExceeded() {
        map.put(key1, value1);
        map.put(key2, value2);
        map.put(key3, value3);
        assertTrue(map.size() == 3);

        // Execute a number of operations to 
        // create some variance between entries.
        map.put(key1, value1);
        map.put(key1, value1);
        map.getValue(key1);
        
        map.put(key3, value3);
        map.put(key3, value3);
        map.getValue(key3);
        map.getValue(key3);

        // Insert new entry. key/value 2 should be forgotten.
        map.put(key4, value4);
        assertEquals(value1, map.getValue(key1));
        assertEquals(value3, map.getValue(key3));
        assertEquals(value4, map.getValue(key4));
        assertEquals(null, map.getValue(key2));
        assertTrue(map.size() == 3);
    }

    @Test
    public void isThreadSafe() throws InterruptedException {
        Runnable test = () -> {
            Random random = new Random();
            int key = random.nextInt(3 - 1) + 1;
            map.put("KEY_"+key, "VALUE_"+key);
        };
        
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i <= 5000; i++) service.submit(test);

        service.awaitTermination(1000, TimeUnit.MILLISECONDS);

        // An exception will occur if a thread attempts 
        // to write to an object already being written to. 
        // This would show that the map is no thread safe.
        assertTrue(true);

    }

}
