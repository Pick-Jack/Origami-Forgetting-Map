# Origami Forgetting Map Coding Test
Solution to the Origami "forgetting map" coding test

# Objective  
The objective of this task is design, implement and test a thread-safe 'forgetting map'.  
A 'forgetting' map should hold associations between a ‘key’ and some ‘content’. It should implement at least two methods:  
1.	add (add an association)  
2.	find (find content using the specified key).  
It should hold as many associations as it can, but no more than x associations at any time, with x being a parameter passed to the constructor. The association that is least used is removed from the map when a new entry requires a space and the map is at capacity, where “least used” relates to the number of times each association has been retrieved by the “find” method.  A suitable tie breaker should be used in the case where there are multiple least-used entries. 

# Implementation  
•	Java would be preferable.  
•	You may use standard collections (HashMap, LinkedList, etc.) to build your solution but the use of a pre-existing library solution with the required behaviour, though an option in everyday work, is not what we’re looking for here. 

# Developer Notes
I've attempted to reflect how long was spent on the excerise in the commit history. It was around 3-4 hours, which was slightly longer than I had antcipated.

I had to switch up my strategy about an hour in and therefore lost a bit of time to this. My first approach was to take advantage of the standard features as much as possible by leveraging the features of the TreeMap implementation. I had thought that it might be possible to maintain a sorted map which would simplify the "forgetting" process by incorporating some of the tracking information into the Key. I quickly found this to be a bit convoluted and tricky to implement, and not wanting to spend excessive amounts of time identifying a solution, settled for something different.

I've instead implemented an object which holds two map instances, one holding the stored value, and the other maintaining information surrounding value access. Both maps are initialised using the Collections.synchronisedMap approach and read/write operations are executed via methods protected by the sychronization context. Unit tests have been written to confirm the behaviour of the map. (Note: I think the thread safety test may have issues but I have no time to investigate further unfortunatley.)

A compartor has been implemented to assist the "forgetting" routine. It will identify an entry with more frequent access compared to another object as "less than" the object. When used to sort a list this results in the last element being the one to remove. Draws are resolved based on the time the element was last accessed and created, in that order, favouring those more recently interacted with.