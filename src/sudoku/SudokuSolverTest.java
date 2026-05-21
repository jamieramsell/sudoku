package sudoku;

import java.util.Set;

public class SudokuSolverTest {
  public static void main(String[] args) {
    shouldValidatePlacements();
    shouldValidateGridState();
    shouldSolveGridWithUniqueSolution();
    shouldReportMultipleSolutions();
    shouldRejectInvalidGridAsUnsolvable();
    shouldValidateMovesAndInput();
  }

  private static void shouldValidatePlacements() {
    int[][] grid = {
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

    assertTrue(ISudokuSolver.isPlacementValid(grid, 0, 2, 4), "valid candidate should be allowed");
    assertFalse(ISudokuSolver.isPlacementValid(grid, 0, 2, 5), "row duplicate should be rejected");
    assertFalse(ISudokuSolver.isPlacementValid(grid, 0, 2, 8), "column duplicate should be rejected");
    assertFalse(ISudokuSolver.isPlacementValid(grid, 0, 2, 9), "square duplicate should be rejected");
  }

  private static void shouldValidateGridState() {
    int[][] validPartial = {
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

    int[][] duplicateInRow = ISudokuGrid.copyGrid(validPartial);
    duplicateInRow[0][2] = 5;

    int[][] outOfRangeValue = ISudokuGrid.copyGrid(validPartial);
    outOfRangeValue[0][2] = 10;

    assertTrue(ISudokuSolver.isGridStateValid(validPartial), "valid partial grid state");
    assertFalse(ISudokuSolver.isGridStateValid(duplicateInRow), "duplicate value should make state invalid");
    assertFalse(ISudokuSolver.isGridStateValid(outOfRangeValue), "out-of-range value should make state invalid");
  }

  private static void shouldSolveGridWithUniqueSolution() {
    ISudokuGrid grid = new SudokuGrid(1);
    int[][] nearlySolved = {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, -1}
    };
    setValues(grid, nearlySolved);

    SudokuSolver solver = new SudokuSolver(grid);
    Set<int[][]> solutions = solver.solveGrid();

    int[][] expected = {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9}
    };

    assertTrue(solver.isSolvable(), "grid with one empty cell should be solvable");
    assertTrue(solver.hasUniqueSolution(), "grid should have one solution");
    assertEquals(1, solver.countSolutions(), "exactly one solution expected");
    assertEquals(1, solutions.size(), "solveGrid should return one solution");
    assertTrue(containsGrid(solutions, expected), "solveGrid should contain expected solution");
  }

  private static void shouldReportMultipleSolutions() {
    ISudokuGrid grid = new SudokuGrid(1);
    int[][] manySolutions = {
      {1, 2, 3},
      {4, 5, 6},
      {-1, -1, -1}
    };
    setValues(grid, manySolutions);

    SudokuSolver solver = new SudokuSolver(grid);

    assertTrue(solver.isSolvable(), "partially filled 3x3 should be solvable");
    assertFalse(solver.hasUniqueSolution(), "grid should not have a unique solution");
    assertEquals(6, solver.countSolutions(), "expected six permutations for final row");
  }

  private static void shouldRejectInvalidGridAsUnsolvable() {
    ISudokuGrid grid = new SudokuGrid(1);
    int[][] invalid = {
      {1, 1, 3},
      {4, 5, 6},
      {7, 8, 9}
    };
    setValues(grid, invalid);

    SudokuSolver solver = new SudokuSolver(grid);

    assertFalse(solver.isSolvable(), "invalid grid should not be solvable");
    assertFalse(solver.hasUniqueSolution(), "invalid grid should not have a unique solution");
    assertEquals(0, solver.countSolutions(), "invalid grid should have zero solutions");
    assertEquals(0, solver.solveGrid().size(), "solveGrid should return empty set for invalid grid");
  }

  private static void shouldValidateMovesAndInput() {
    ISudokuGrid grid = new SudokuGrid(1);
    int[][] base = {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, -1}
    };
    setValues(grid, base);

    SudokuSolver solver = new SudokuSolver(grid);

    assertTrue(solver.isValidMove(2, 2, -1), "clearing a cell should always be valid");
    assertTrue(solver.isValidMove(2, 2, 9), "placing 9 should be valid in final cell");
    assertFalse(solver.isValidMove(2, 2, 1), "row duplicate should be invalid move");

    assertThrows(IllegalArgumentException.class, () -> solver.isValidMove(2, 2, 0),
        "out-of-range value should throw");
    assertThrows(IndexOutOfBoundsException.class, () -> solver.isValidMove(3, 2, 9),
        "out-of-bounds row should throw");
    assertThrows(IndexOutOfBoundsException.class, () -> solver.isValidMove(2, 3, 9),
        "out-of-bounds column should throw");
  }

  private static void setValues(ISudokuGrid grid, int[][] values) {
    for (int row = 0; row < values.length; row++) {
      for (int column = 0; column < values[row].length; column++) {
        grid.setValue(row, column, values[row][column]);
      }
    }
  }

  private static boolean containsGrid(Set<int[][]> grids, int[][] expected) {
    for (int[][] grid : grids) {
      if (gridsEqual(grid, expected)) {
        return true;
      }
    }
    return false;
  }

  private static boolean gridsEqual(int[][] left, int[][] right) {
    if (left.length != right.length) {
      return false;
    }

    for (int row = 0; row < left.length; row++) {
      if (left[row].length != right[row].length) {
        return false;
      }

      for (int column = 0; column < left[row].length; column++) {
        if (left[row][column] != right[row][column]) {
          return false;
        }
      }
    }

    return true;
  }

  private static void assertTrue(boolean condition, String scenario) {
    if (!condition) {
      throw new AssertionError("Expected true for " + scenario);
    }
  }

  private static void assertFalse(boolean condition, String scenario) {
    if (condition) {
      throw new AssertionError("Expected false for " + scenario);
    }
  }

  private static void assertEquals(int expected, int actual, String scenario) {
    if (expected != actual) {
      throw new AssertionError(
          "Unexpected output for " + scenario + "\nExpected:\n" + expected + "\nActual:\n" + actual);
    }
  }

  private static void assertThrows(Class<? extends Throwable> expectedType, Runnable action,
      String scenario) {
    try {
      action.run();
      throw new AssertionError("Expected " + expectedType.getSimpleName() + " for " + scenario);
    } catch (Throwable actual) {
      if (!expectedType.isInstance(actual)) {
        throw new AssertionError(
            "Expected " + expectedType.getSimpleName() + " for " + scenario + " but got "
                + actual.getClass().getSimpleName());
      }
    }
  }
}
