package assignment1;

public class StringHandler {
    private String document;  // The input document to be processed
    private int index;  // The current index position in the document

    public StringHandler(String document) {
        this.document = document;  // Initialize the document to be processed
        this.index = 0;  // Start processing from the beginning of the document
    }

    // Peeks at a character at a certain offset 'i' from the current index
    public char peek(int i) {
        if (index + i < document.length()) {  // Check if index + i is within the document's length
            return document.charAt(index + i);  // Return the character at the specified index
        }
        return '\0';  // Return a null character if the index is out of bounds
    }

    // Gets the next character from the document and advances the index
    public char getNextChar() {
        if (index < document.length()) {  // Check if the index is within the document's length
            return document.charAt(index++);  // Return the current character and increment the index
        }
        return '\0';  // Return a null character if the end of the document is reached
    }

    // Checks if the end of the document has been reached
    public boolean isEndOfDocument() {
        return index >= document.length();  // Returns true if the index is at or beyond the document's length
    }
}


