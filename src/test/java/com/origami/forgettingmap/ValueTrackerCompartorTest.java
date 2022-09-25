package com.origami.forgettingmap;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ValueTrackerCompartorTest {
    
    public ValueTrackerComparator<String> comparator;

    @Before
    public void beforeTest() {
        comparator = new ValueTrackerComparator<>();
    }

    @Test
    public void canCompareOperationCounts() throws InterruptedException {
        ValueTracker<String> tracker1 = new ValueTracker<>("KEY_1");
        
        // Delay creation of next tracker
        Thread.sleep(1000);
        ValueTracker<String> tracker2 = new ValueTracker<>("KEY_2");
        tracker2.recordOperation();
        tracker2.recordOperation();
        tracker2.recordOperation();
        
        assertEquals(1, comparator.compare(tracker1, tracker2));
    }
    
    @Test
    public void canCompareLastOperationInstants() throws InterruptedException {
        ValueTracker<String> tracker1 = new ValueTracker<>("KEY_1");
        
        // Delay creation of next tracker
        Thread.sleep(1000);
        ValueTracker<String> tracker2 = new ValueTracker<>("KEY_2");
        tracker2.recordOperation();
        tracker2.recordOperation();
        tracker2.recordOperation();

        Thread.sleep(1000);

        tracker1.recordOperation();
        tracker1.recordOperation();
        tracker1.recordOperation(); // latest update
        
        assertEquals(-1, comparator.compare(tracker1, tracker2));
    }

    @Test
    public void canCompareCreationInstants() throws InterruptedException {
        // Trackers created at the same time
        ValueTracker<String> tracker1 = new ValueTracker<>("KEY_1");
        ValueTracker<String> tracker2 = new ValueTracker<>("KEY_2");
        assertEquals(1, comparator.compare(tracker1, tracker2));

        // Trackers created using a delay
        tracker1 = new ValueTracker<>("KEY_1");
        Thread.sleep(1000);
        tracker2 = new ValueTracker<>("KEY_2");
        
        assertEquals(1, comparator.compare(tracker1, tracker2));
        assertEquals(-1, comparator.compare(tracker2, tracker1));
    }

    @Test
    public void canCompareTrackersWithNoOperations() {
        // Trackers created at the same time
        ValueTracker<String> tracker1 = new ValueTracker<>("KEY_1");
        ValueTracker<String> tracker2 = new ValueTracker<>("KEY_2");
        assertEquals(1, comparator.compare(tracker1, tracker2));
    }
    
}
