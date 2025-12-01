# Advent of Code 

This project provides a simple framework to solve Advent of Code challenges in Java.

## Project Structure

```
src/main/java/fr/corentin/
├── utils/
│   ├── Day.java          # Abstract base class for each day
│   └── FileReader.java   # Utility to read input files
└── dayXX/
    ├── DayXX.java        # Solution for day XX
    └── input.txt         # Puzzle input for day XX
```

## How It Works

### 1. The `Day` Abstract Class

The `Day` class provides a framework for solving daily puzzles. It:
- Automatically reads your input file using the day number
- Provides a structured way to implement both parts of each puzzle
- Measures and displays execution time for each part

**Key features:**
- `dayNumber`: The day number (1-25)
- `input`: The raw input text (automatically loaded)
- `getInputLines()`: Helper method to split input into lines
- `solve()`: Executes both parts and displays results with timing

**Abstract methods to implement:**
- `solvePart1()`: Implement the solution for Part 1
- `solvePart2()`: Implement the solution for Part 2

### 2. The `FileReader` Utility Class

The `FileReader` class handles all file operations. It provides:

- `readFile(packagePath, filename)`: Reads a file as a single String
- `readLines(packagePath, filename)`: Reads a file as a List of lines
- `readDayInput(day)`: Automatically reads `input.txt` for a specific day
- `readDayInputLines(day)`: Same as above, but returns a List of lines

The class automatically constructs the correct file path based on the package structure.

## Creating a New Day Solution

### Step 1: Create the Package Structure

Create a new package for the day:
```
src/main/java/fr/corentin/dayXX/
```
(Replace `XX` with the day number, e.g., `day02`, `day03`, etc.)

### Step 2: Add Your Input File

Create an `input.txt` file in the day's package and paste your puzzle input:
```
src/main/java/fr/corentin/dayXX/input.txt
```

### Step 3: Create Your Day Class

Create a new Java class that extends `Day`:

```java
package fr.corentin.dayXX;

import fr.corentin.utils.Day;

public class DayXX extends Day {
    private static final int DAY = XX; // Replace with actual day number

    public DayXX() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        // Your solution for Part 1
        // Access input via: this.input (raw text)
        // Or use: getInputLines() (array of lines)
        
        String[] lines = getInputLines();
        // Your logic here...
        
        return "Your answer";
    }

    @Override
    public String solvePart2() {
        // Your solution for Part 2
        
        return "Your answer";
    }

    public static void main(String[] args) {
        new DayXX().solve();
    }
}
```

## Happy Coding! 🎄⭐

