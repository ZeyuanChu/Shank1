package assignment1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

// BlockNode class, derived from Node
public class BlockNode extends Node { 
    private List<Node> statements;
    private Optional<Node> condition;

    public BlockNode() {
        this.statements = new LinkedList<>();
        this.condition = Optional.empty();
    }

    public void addStatement(Node statement) {
        statements.add(statement);
    }

    // Getter for statements 
    public List<Node> getStatements() {
        return statements;
    }

    // Getter and Setter for condition 
    public Optional<Node> getCondition() {
        return condition;
    }

    public void setCondition(Optional<Node> condition) {
        this.condition = condition;
    }
}

