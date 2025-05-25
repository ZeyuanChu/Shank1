package assignment1;

import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType {
	private HashMap<String, InterpreterDataType> hashMap;

	
	// Method to clear the array
    public void clear() {
        hashMap.clear();
    }

    // Method to remove an element by key
    public void remove(String key) {
        hashMap.remove(key);
    }
    
    // Constructor: initialize the hash map
    public InterpreterArrayDataType() {
        this.hashMap = new HashMap<>();
    }

    // Method to add or update a key-value pair in the hash map
    public void set(String key, InterpreterDataType value) {
        hashMap.put(key, value);
    }

    // Method to retrieve a value using a key from the hash map
    public InterpreterDataType get(String key) {
        return hashMap.get(key);
    }

}

