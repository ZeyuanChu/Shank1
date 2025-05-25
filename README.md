# Shank Interpreter

A Java-based interpreter implementation that parses and executes a custom programming language. This project demonstrates the implementation of a complete interpreter pipeline, from lexical analysis to execution.

## Project Overview

Shank is a simple programming language interpreter that supports various programming constructs and features. The implementation follows a traditional interpreter architecture with distinct phases for lexical analysis, parsing, and execution.

## Features

- **Lexical Analysis**: Tokenization of source code
- **Syntax Parsing**: Building an Abstract Syntax Tree (AST)
- **Interpreter**: Execution of the AST
- **Language Features**:
  - Variables and assignments
  - Control structures (if, while, for, do-while)
  - Functions and function calls
  - Arrays and array operations
  - Basic arithmetic and logical operations
  - Pattern matching
  - Ternary operations
  - Break and continue statements
  - Return statements

## Project Structure

### Core Components

- `Awk.java` - Main program entry point
- `Lexer.java` - Lexical analyzer implementation
- `Parser.java` - Syntax parser implementation
- `Interpreter.java` - Interpreter implementation
- `Token.java` - Token definition and handling
- `TokenHandler.java` - Token processing utilities

### AST Nodes

- `Node.java` - Base node class
- `ProgramNode.java` - Program root node
- `StatementNode.java` - Statement node
- `BlockNode.java` - Code block node
- `AssignmentNode.java` - Variable assignment
- `OperationNode.java` - Operations and expressions
- `ConstantNode.java` - Constants
- `VariableReferenceNode.java` - Variable references
- `FunctionCallNode.java` - Function calls
- `FunctionDefinitionNode.java` - Function definitions
- `BuiltInFunctionDefinitionNode.java` - Built-in functions
- `IfNode.java` - If statements
- `WhileNode.java` - While loops
- `ForNode.java` - For loops
- `DoWhileNode.java` - Do-while loops
- `ForEachNode.java` - For-each loops
- `BreakNode.java` - Break statements
- `ContinueNode.java` - Continue statements
- `ReturnNode.java` - Return statements
- `TernaryNode.java` - Ternary operations
- `PatternNode.java` - Pattern matching
- `DeleteNode.java` - Delete operations

### Data Types

- `InterpreterDataType.java` - Base data type
- `InterpreterArrayDataType.java` - Array data type
- `ReturnType.java` - Return value handling

### Utilities

- `StringHandler.java` - String manipulation utilities

## Building and Running

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- JUnit 4 for testing

### Compilation

```bash
javac -cp "lib/*:bin" -d bin src/assignment1/*.java
```

### Running

```bash
java -cp "lib/*:bin" assignment1.Awk
```

### Running Tests

```bash
java -cp "lib/*:bin" org.junit.runner.JUnitCore assignment1.AwkTest
```

## Testing

The project includes comprehensive test coverage with JUnit tests for:
- Lexical analysis
- Parsing
- Interpreter functionality
- Individual node types
- Data type handling
- Pattern matching
- Control structures

## Implementation Details

### Lexical Analysis
The lexer (`Lexer.java`) breaks down source code into tokens, identifying:
- Keywords
- Identifiers
- Operators
- Literals
- Separators

### Parsing
The parser (`Parser.java`) constructs an AST from tokens, implementing:
- Expression parsing
- Statement parsing
- Control structure parsing
- Function definition parsing

### Interpretation
The interpreter (`Interpreter.java`) executes the AST, handling:
- Variable scope
- Function calls
- Control flow
- Data type operations

## Contributing

Feel free to submit issues and enhancement requests! 