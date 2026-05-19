package sudoku;

public class SudokuGridTest {
  public static void main(String[] args) {
    shouldFormatSingleSquareGrid();
    shouldFormatFourSquareGrid();
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

  private static void assertEquals(String expected, String actual, String scenario) {
    if (!expected.equals(actual)) {
      throw new AssertionError(
        "Unexpected output for " + scenario + "\nExpected:\n" + expected + "\nActual:\n" + actual
      );
    }
  }
}
