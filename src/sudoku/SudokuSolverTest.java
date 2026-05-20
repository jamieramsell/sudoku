package sudoku;

import java.util.Set;

public class SudokuSolverTest {
  public static void main(String[] args) {
    testSolveGridFindsKnownSolution();
    testSolveGridReturnsNoSolutionsForInvalidGrid();
    testSolveGridDoesNotMutateOriginalGrid();
    System.out.println("SudokuSolverTest passed");
  }

  private static void testSolveGridFindsKnownSolution() {
    int[][] puzzle = {
      {5, 3, -1, -1, 7, -1, -1, -1, -1},
      {6, -1, -1, 1, 9, 5, -1, -1, -1},
      {-1, 9, 8, -1, -1, -1, -1, 6, -1},
      {8, -1, -1, -1, 6, -1, -1, -1, 3},
      {4, -1, -1, 8, -1, 3, -1, -1, 1},
      {7, -1, -1, -1, 2, -1, -1, -1, 6},
      {-1, 6, -1, -1, -1, -1, 2, 8, -1},
      {-1, -1, -1, 4, 1, 9, -1, -1, 5},
      {-1, -1, -1, -1, 8, -1, -1, 7, 9}
    };

    SudokuSolver solver = new SudokuSolver(new MockSudokuGrid(puzzle, true));
    assertEquals(1, solver.countSolutions(), "Expected exactly one solution for known puzzle");
    Set<int[][]> solutions = solver.solveGrid();
    assertEquals(1, solutions.size(), "Expected one solution in the solution set");

    for (int[][] solution : solutions) {
      assertEquals(5, solution[0][0], "Solution should preserve fixed values");
      assertEquals(1, solution[8][6], "Solution should solve final row correctly");
    }
    
  }

  private static void testSolveGridReturnsNoSolutionsForInvalidGrid() {
    int[][] invalidPuzzle = {
      {5, 5, -1, -1},
      {-1, -1, -1, -1},
      {-1, -1, -1, -1},
      {-1, -1, -1, -1}
    };

    SudokuSolver solver = new SudokuSolver(new MockSudokuGrid(invalidPuzzle, false));
    assertEquals(0, solver.countSolutions(), "Expected no solutions for invalid puzzle");
    assertEquals(0, solver.solveGrid().size(), "Expected empty solution set for invalid puzzle");
  }

  private static void testSolveGridDoesNotMutateOriginalGrid() {
    int[][] puzzle = {
      {1, -1, -1, 4},
      {-1, -1, 1, -1},
      {-1, 1, -1, -1},
      {4, -1, -1, 1}
    };

    int[][] originalCopy = copyGrid(puzzle);
    SudokuSolver solver = new SudokuSolver(new MockSudokuGrid(puzzle, true));
    solver.solveGrid();
    assertGridEquals(originalCopy, puzzle, "solveGrid should not mutate input grid");
  }

  private static void assertEquals(int expected, int actual, String message) {
    if (expected != actual) {
      throw new AssertionError(message + " (expected " + expected + " but was " + actual + ")");
    }
  }

  private static void assertGridEquals(int[][] expected, int[][] actual, String message) {
    if (expected.length != actual.length) {
      throw new AssertionError(message + " (row count mismatch)");
    }

    for (int row = 0; row < expected.length; row++) {
      if (expected[row].length != actual[row].length) {
        throw new AssertionError(message + " (column count mismatch at row " + row + ")");
      }
      for (int column = 0; column < expected[row].length; column++) {
        if (expected[row][column] != actual[row][column]) {
          throw new AssertionError(message + " (mismatch at [" + row + "," + column + "])");
        }
      }
    }
  }

  private static int[][] copyGrid(int[][] sourceGrid) {
    int[][] gridCopy = new int[sourceGrid.length][];
    for (int row = 0; row < sourceGrid.length; row++) {
      gridCopy[row] = new int[sourceGrid[row].length];
      System.arraycopy(sourceGrid[row], 0, gridCopy[row], 0, sourceGrid[row].length);
    }
    return gridCopy;
  }

  private static final class MockSudokuGrid implements ISudokuGrid {
    private final int[][] values;
    private final boolean valid;

    private MockSudokuGrid(int[][] values, boolean valid) {
      this.values = values;
      this.valid = valid;
    }

    public int[][] initialiseGrid(int rows, int columns) {
      throw new UnsupportedOperationException();
    }

    public int getValue(int row, int column) {
      return values[row][column];
    }

    public void setValue(int row, int column) {
      throw new UnsupportedOperationException();
    }

    public boolean isValid() {
      return valid;
    }

    public boolean isSolved() {
      throw new UnsupportedOperationException();
    }

    public int[][] resetGrid() {
      throw new UnsupportedOperationException();
    }

    public void displayGrid() {
      throw new UnsupportedOperationException();
    }

    public Tuple2<Integer, Integer> getGridSize() {
      return new Tuple2<>(values.length / 3, values[0].length / 3);
    }

    public int[][] getGrid() {
      throw new UnsupportedOperationException();
    }

  }
}
