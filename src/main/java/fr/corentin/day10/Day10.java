package fr.corentin.day10;

import fr.corentin.utils.Day;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.*;
import java.util.regex.*;
import java.util.stream.*;

public class Day10 extends Day {

    private static final int DAY = 10;
    private static final Pattern TARGET_PATTERN = Pattern.compile("\\[([.#]+)\\]");
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\(([0-9,]+)\\)");

    public Day10() {
        super(DAY);
    }

    @Override
    public String solvePart1() {
        return String.valueOf(solve(this::parseMachineLights, this::solveLights));
    }

    @Override
    public String solvePart2() {
        return String.valueOf(solve(this::parseMachineJoltage, this::solveJoltageWithCommonsMath));
    }

    private int solve(java.util.function.Function<String, Machine> parser, 
                      java.util.function.ToIntFunction<Machine> solver) {
        return Arrays.stream(getInputLines())
            .filter(line -> !line.trim().isEmpty())
            .map(parser)
            .mapToInt(solver)
            .sum();
    }

    // Part 1: Lights problem - BFS
    private Machine parseMachineLights(String line) {
        Matcher m = TARGET_PATTERN.matcher(line);
        if (!m.find()) throw new IllegalStateException("No target found");
        
        int target = 0;
        String targetStr = m.group(1);
        for (int i = 0; i < targetStr.length(); i++) {
            if (targetStr.charAt(i) == '#') target |= (1 << i);
        }

        List<int[]> buttons = parseButtons(line);
        return new Machine(new int[]{target}, buttons, targetStr.length());
    }

    private int solveLights(Machine machine) {
        int target = machine.targets[0];
        int numLights = machine.extra;
        
        Map<Integer, Integer> visited = new HashMap<>();
        Deque<State> queue = new ArrayDeque<>();
        queue.add(new State(0, 0));
        visited.put(0, 0);

        while (!queue.isEmpty()) {
            State curr = queue.poll();
            if (curr.state == target) return curr.presses;

            for (int[] button : machine.buttons) {
                int nextState = curr.state;
                for (int light : button) {
                    if (light < numLights) nextState ^= (1 << light);
                }

                int nextPresses = curr.presses + 1;
                if (!visited.containsKey(nextState) || visited.get(nextState) > nextPresses) {
                    visited.put(nextState, nextPresses);
                    queue.add(new State(nextState, nextPresses));
                }
            }
        }

        return 0;
    }

    // Part 2: Joltage problem - Using Apache Commons Math for Linear Programming
    private Machine parseMachineJoltage(String line) {
        int start = line.indexOf('{');
        int end = line.indexOf('}');
        int[] targets = Arrays.stream(line.substring(start + 1, end).split(","))
            .mapToInt(s -> Integer.parseInt(s.trim()))
            .toArray();

        return new Machine(targets, parseButtons(line), 0);
    }

    private List<int[]> parseButtons(String line) {
        List<int[]> buttons = new ArrayList<>();
        Matcher m = BUTTON_PATTERN.matcher(line);
        while (m.find()) {
            buttons.add(Arrays.stream(m.group(1).split(","))
                .mapToInt(s -> Integer.parseInt(s.trim()))
                .toArray());
        }
        return buttons;
    }

    private int solveJoltageWithCommonsMath(Machine machine) {
        int rows = machine.targets.length;
        int cols = machine.buttons.size();

        // Build coefficient matrix using Apache Commons Math
        double[][] coefficients = new double[rows][cols];
        for (int b = 0; b < cols; b++) {
            for (int c : machine.buttons.get(b)) {
                if (c < rows) coefficients[c][b] = 1.0;
            }
        }

        // Convert targets to double array
        double[] targets = Arrays.stream(machine.targets).asDoubleStream().toArray();

        try {
            // Try to solve using Linear Programming (Simplex)
            // Objective: minimize sum of all button presses
            LinearObjectiveFunction objective = new LinearObjectiveFunction(
                IntStream.range(0, cols).mapToDouble(i -> 1.0).toArray(), 0
            );

            // Constraints: Ax = b (equality constraints)
            Collection<LinearConstraint> constraints = new ArrayList<>();
            for (int i = 0; i < rows; i++) {
                constraints.add(new LinearConstraint(
                    coefficients[i], 
                    Relationship.EQ, 
                    targets[i]
                ));
            }

            // Non-negativity constraints: x >= 0
            for (int j = 0; j < cols; j++) {
                double[] constraint = new double[cols];
                constraint[j] = 1.0;
                constraints.add(new LinearConstraint(constraint, Relationship.GEQ, 0));
            }

            // Solve using Simplex algorithm
            SimplexSolver solver = new SimplexSolver();
            PointValuePair solution = solver.optimize(
                new MaxIter(10000),
                objective,
                new LinearConstraintSet(constraints),
                GoalType.MINIMIZE,
                new NonNegativeConstraint(true)
            );

            // Round and sum the solution
            return (int) Math.round(Arrays.stream(solution.getPoint()).sum());

        } catch (Exception e) {
            // Fallback to Gaussian elimination if Simplex fails
            return solveWithGaussian(coefficients, targets, cols);
        }
    }

    private int solveWithGaussian(double[][] A, double[] b, int cols) {
        // Fallback using our custom Gaussian elimination
        int rows = b.length;
        RealMatrix matrix = MatrixUtils.createRealMatrix(A);
        RealVector vector = MatrixUtils.createRealVector(b);

        try {
            // Use QR decomposition for more stable solution
            DecompositionSolver solver = new QRDecomposition(matrix).getSolver();
            RealVector solution = solver.solve(vector);

            // Round to integers and return sum
            return (int) Math.round(Arrays.stream(solution.toArray())
                .map(Math::abs)
                .sum());
        } catch (Exception e) {
            // If that fails too, return a basic solution
            return (int) Arrays.stream(b).sum();
        }
    }


    private record Machine(int[] targets, List<int[]> buttons, int extra) { }

    private record State(int state, int presses) { }

    public static void main(String[] args) {
        new Day10().solve();
    }
}
