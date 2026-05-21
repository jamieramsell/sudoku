package sudoku;

public class SudokuSolverTest {

  public static void main(String[] args) {
    testImplementationMatchesInterface();
    testSolveReturnsSolvedGridForValidPuzzle();
    testSolveDoesNotMutateInput();
    testAlreadySolvedPuzzleIsReturned();
    testInvalidPuzzleShapeThrows();
    testInvalidPuzzleValueThrows();
    testContradictoryPuzzleThrows();
    testUnsolvablePuzzleThrows();

    System.out.println("All SudokuSolver tests passed.");
  }

  private static void testImplementationMatchesInterface() {
    ISudokuSolver solver = new SudokuSolver();
    assertTrue(solver instanceof ISudokuSolver, "SudokuSolver must implement ISudokuSolver.");
  }

  private static void testSolveReturnsSolvedGridForValidPuzzle() {
    ISudokuSolver solver = new SudokuSolver();
    int[][] puzzle = createPuzzle();

    int[][] solved = solver.solve(puzzle);

    assertGridEquals(createExpectedSolution(), solved,
        "Solver should produce the expected solved grid.");
  }

  private static void testSolveDoesNotMutateInput() {
    ISudokuSolver solver = new SudokuSolver();
    int[][] puzzle = createPuzzle();
    int[][] original = deepCopy(puzzle);

    solver.solve(puzzle);

    assertGridEquals(original, puzzle,
        "Solver should not mutate the input grid.");
  }

  private static void testAlreadySolvedPuzzleIsReturned() {
    ISudokuSolver solver = new SudokuSolver();
    int[][] solvedPuzzle = createExpectedSolution();

    int[][] solved = solver.solve(solvedPuzzle);

    assertGridEquals(createExpectedSolution(), solved,
        "Already solved puzzles should remain solved.");
  }

  private static void testInvalidPuzzleShapeThrows() {
    ISudokuSolver solver = new SudokuSolver();

    assertThrows(() -> solver.solve(new int[8][9]),
        "Puzzle with invalid shape should throw IllegalArgumentException.");
    assertThrows(() -> solver.solve(new int[9][]),
        "Puzzle with missing row data should throw IllegalArgumentException.");
  }

  private static void testInvalidPuzzleValueThrows() {
    ISudokuSolver solver = new SudokuSolver();
    int[][] puzzle = createPuzzle();
    puzzle[0][0] = 10;

    assertThrows(() -> solver.solve(puzzle),
        "Puzzle with invalid values should throw IllegalArgumentException.");
  }

  private static void testContradictoryPuzzleThrows() {
    ISudokuSolver solver = new SudokuSolver();
    int[][] puzzle = createPuzzle();
    puzzle[0][1] = 5;

    assertThrows(() -> solver.solve(puzzle),
        "Puzzle with contradictory givens should throw IllegalArgumentException.");
  }

  private static void testUnsolvablePuzzleThrows() {
    ISudokuSolver solver = new SudokuSolver();
    int[][] puzzle = {
        {1, 0, 0, 0, 0, 7, 0, 9, 0},
        {0, 3, 0, 0, 2, 0, 0, 0, 8},
        {0, 0, 9, 6, 0, 0, 5, 0, 0},
        {0, 0, 5, 3, 0, 0, 9, 0, 0},
        {0, 1, 0, 0, 8, 0, 0, 0, 2},
        {6, 0, 0, 0, 0, 4, 0, 0, 0},
        {3, 0, 0, 0, 0, 0, 0, 1, 0},
        {0, 4, 0, 0, 0, 0, 0, 0, 7},
        {0, 0, 7, 0, 0, 0, 3, 0, 0}
    };
    puzzle[8][8] = 9;

    assertThrows(() -> solver.solve(puzzle),
        "Unsolvable puzzle should throw IllegalArgumentException.");
  }

  private static int[][] createPuzzle() {
    return new int[][] {
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
  }

  private static int[][] createExpectedSolution() {
    return new int[][] {
        {5, 3, 4, 6, 7, 8, 9, 1, 2},
        {6, 7, 2, 1, 9, 5, 3, 4, 8},
        {1, 9, 8, 3, 4, 2, 5, 6, 7},
        {8, 5, 9, 7, 6, 1, 4, 2, 3},
        {4, 2, 6, 8, 5, 3, 7, 9, 1},
        {7, 1, 3, 9, 2, 4, 8, 5, 6},
        {9, 6, 1, 5, 3, 7, 2, 8, 4},
        {2, 8, 7, 4, 1, 9, 6, 3, 5},
        {3, 4, 5, 2, 8, 6, 1, 7, 9}
    };
  }

  private static int[][] deepCopy(int[][] source) {
    int[][] copy = new int[source.length][];
    for (int i = 0; i < source.length; i++) {
      copy[i] = new int[source[i].length];
      for (int j = 0; j < source[i].length; j++) {
        copy[i][j] = source[i][j];
      }
    }
    return copy;
  }

  private static void assertGridEquals(int[][] expected, int[][] actual, String message) {
    if (expected.length != actual.length) {
      throw new AssertionError(message + " Expected " + expected.length + " rows but found "
          + actual.length + ".");
    }

    for (int row = 0; row < expected.length; row++) {
      if (expected[row].length != actual[row].length) {
        throw new AssertionError(message + " Expected " + expected[row].length + " columns but found "
            + actual[row].length + " on row " + row + ".");
      }
      for (int column = 0; column < expected[row].length; column++) {
        if (expected[row][column] != actual[row][column]) {
          throw new AssertionError(message + " Mismatch at [" + row + "][" + column + "]: expected "
              + expected[row][column] + " but found " + actual[row][column] + ".");
        }
      }
    }
  }

  private static void assertThrows(TestAction action, String message) {
    try {
      action.run();
    } catch (IllegalArgumentException ex) {
      return;
    }
    throw new AssertionError(message);
  }

  private static void assertTrue(boolean condition, String message) {
    if (!condition) {
      throw new AssertionError(message);
    }
  }

  @FunctionalInterface
  private interface TestAction {
    void run();
  }
}
