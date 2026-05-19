package sudoku;

import java.util.List;

public class SudokuSolverTest {
  public static void main(String[] args) {
    testSolveGridFindsKnownSolution();
    testSolveGridReturnsNoSolutionsForInvalidGrid();
    testSolveGridDoesNotMutateOriginalGrid();
    System.out.println("SudokuSolverTest passed");
  }

  private static void testSolveGridFindsKnownSolution() {
    int[][] puzzle = {
      {5, 3, 0, 0, 7, 0, 0, 0, 0},
      {6, 0, 0, 1, 9, 5, 0, 0, 0},
      {0, 9, 8, 0, 0, 0, 0, 6, 0},
      {8, 0, 0, 0, 6, 0, 0, 0, 3},
      {4, 0, 0, 8, 0, 3, 0, 0, 1},
      {7, 0, 0, 0, 2, 0, 0, 0, 6},
      {0, 6, 0, 0, 0, 0, 2, 8, 0},
      {0, 0, 0, 4, 1, 9, 0, 0, 5},
      {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    List<int[][]> solutions = SudokuSolver.solveGrid(puzzle);
    assertEquals(1, solutions.size(), "Expected exactly one solution for known puzzle");
    assertEquals(5, solutions.get(0)[0][0], "Solution should preserve fixed values");
    assertEquals(1, solutions.get(0)[8][6], "Solution should solve final row correctly");
  }

  private static void testSolveGridReturnsNoSolutionsForInvalidGrid() {
    int[][] invalidPuzzle = {
      {5, 5, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    };

    List<int[][]> solutions = SudokuSolver.solveGrid(invalidPuzzle);
    assertEquals(0, solutions.size(), "Expected no solutions for invalid puzzle");
  }

  private static void testSolveGridDoesNotMutateOriginalGrid() {
    int[][] puzzle = {
      {1, 0, 0, 4},
      {0, 0, 1, 0},
      {0, 1, 0, 0},
      {4, 0, 0, 1}
    };

    int[][] originalCopy = copyGrid(puzzle);
    SudokuSolver.solveGrid(puzzle);
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
}
