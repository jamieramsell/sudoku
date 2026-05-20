package sudoku;

public class SudokuGridTest {
  public static void main(String[] args) {
    shouldFormatSingleSquareGrid();
    shouldFormatFourSquareGrid();
    shouldRejectInvalidValues();
    shouldResetGridToEmptyCells();
    shouldReturnCopyWhenResettingGrid();
  }

  private static void shouldFormatSingleSquareGrid() {
    ISudokuGrid grid = new SudokuGrid(1);

    int[][] values = {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9}
    };

    setValues(grid, values);

    String expected = String.join("\n",
      "1 2 3",
      "4 5 6",
      "7 8 9"
    );

    assertEquals(expected, grid.toString(), "single 3x3 square");
  }

  private static void shouldFormatFourSquareGrid() {
    ISudokuGrid grid = new SudokuGrid(2);

    int[][] values = {
      {1, 2, 3, 4, 5, 6},
      {4, 5, 6, 7, 8, 9},
      {7, 8, 9, 1, 2, 3},
      {1, 2, 3, 4, 5, 6},
      {4, 5, 6, 7, 8, 9},
      {7, 8, 9, 1, 2, 3}
    };

    setValues(grid, values);

    String expected = String.join("\n",
      "1 2 3 | 4 5 6",
      "4 5 6 | 7 8 9",
      "7 8 9 | 1 2 3",
      "-------------",
      "1 2 3 | 4 5 6",
      "4 5 6 | 7 8 9",
      "7 8 9 | 1 2 3"
    );

    assertEquals(expected, grid.toString(), "2x2 square grid");
  }

  private static void setValues(ISudokuGrid grid, int[][] values) {
    for (int row = 0; row < values.length; row++) {
      for (int column = 0; column < values[row].length; column++) {
        grid.setValue(row, column, values[row][column]);
      }
    }
  }

  private static void shouldRejectInvalidValues() {
    ISudokuGrid grid = new SudokuGrid(1);
    assertThrows(() -> grid.setValue(0, 0, -2), "negative value outside valid range");
    assertThrows(() -> grid.setValue(0, 0, 0), "value lower than valid range");
    assertThrows(() -> grid.setValue(0, 0, 10), "value higher than valid range");
  }

  private static void shouldResetGridToEmptyCells() {
    ISudokuGrid grid = new SudokuGrid(1);
    grid.setValue(0, 0, 5);
    grid.setValue(1, 1, 6);

    int[][] reset = grid.resetGrid();

    for (int row = 0; row < reset.length; row++) {
      for (int column = 0; column < reset[row].length; column++) {
        assertEquals(-1, reset[row][column], "reset cell value at (" + row + ", " + column + ")");
        assertEquals(-1, grid.getValue(row, column), "internal grid value at (" + row + ", " + column + ")");
      }
    }
  }

  private static void shouldReturnCopyWhenResettingGrid() {
    ISudokuGrid grid = new SudokuGrid(1);

    int[][] reset = grid.resetGrid();
    reset[0][0] = 9;

    assertEquals(-1, grid.getValue(0, 0), "resetGrid should return a copy");
  }

  private static void assertEquals(String expected, String actual, String scenario) {
    if (!expected.equals(actual)) {
      throw new AssertionError(
        "Unexpected output for " + scenario + "\nExpected:\n" + expected + "\nActual:\n" + actual
      );
    }
  }

  private static void assertEquals(int expected, int actual, String scenario) {
    if (expected != actual) {
      throw new AssertionError(
        "Unexpected output for " + scenario + "\nExpected:\n" + expected + "\nActual:\n" + actual
      );
    }
  }

  private static void assertThrows(Runnable action, String scenario) {
    try {
      action.run();
      throw new AssertionError("Expected IllegalArgumentException for " + scenario);
    } catch (IllegalArgumentException expected) {
      // Expected path
    }
  }
}
