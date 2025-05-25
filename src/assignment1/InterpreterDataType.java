
package assignment1;

public class InterpreterDataType {
    private String value;

    // Constructor without initial value
    public InterpreterDataType() {
        this.value = "";
    }

    // Constructor with an initial value supplied
    public InterpreterDataType(String value) {
        this.value = value;
    }

    // Getter method for value
    public String getValue() {
        return value;
    }

    // Setter method for value
    public void setValue(String value) {
        this.value = value;
    }
   
}






