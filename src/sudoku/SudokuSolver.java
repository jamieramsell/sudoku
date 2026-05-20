package sudoku;

public class SudokuSolver implements ISudokuSolver {

  private final ISudokuGrid grid;

  public SudokuSolver(ISudokuGrid grid) {
    this.grid = grid;
  }

  // To do
  public boolean isSolvable() {return false;}

  // To do
  public boolean hasUniqueSolution() {return false;}

  // To do
  public int countSolutions() {return -1;}

  // To do
  public int[][] solveGrid() {return new int[][]{{-1}};}

  public boolean isValidMove(int row, int column, int value) {
    
    // Input Validation //

    if (value == -1) { // Always allow cells to be emptied
      return true;
    } else if (value < 1 || value > 9) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 inclusive.");
    }

    Tuple2<Integer, Integer> grid_size = grid.getGridSize();
    if (row < 0 || row > grid_size.first() * 3 || column < 0 || column > grid_size.second() * 3) {
      throw new IndexOutOfBoundsException("row or column out of bounds");
    }

    // Check for duplicate values in the row
    for (int current_col = 0; current_col < grid_size.second(); current_col++) {
      if (grid.getValue(row, current_col) == value) {
        return false;
      }
    }

    // Check for duplicates in the column
    for (int current_row = 0; current_row < grid_size.first(); current_row++) {
      if (grid.getValue(current_row, column) == value) {
        return false;
      }
    }

    // Check for duplicate values in the square
    return duplicatesInSquare(row, column, value);

  }

  // Convenience method to check for duplicate values in the sudoku square of the given cell
  private boolean duplicatesInSquare(int row, int column, int value) {
    
    // Find coordinates of top-left cell in the square
    int top_left_row = row / 3;
    int top_left_col = column / 3;

    // Check whether each cell in the square is a duplicate
    for (int current_row = top_left_row; current_row < top_left_row + 2; current_row++) {
      for (int current_col = top_left_col; current_col < top_left_col + 2; current_col++) {

        if (value == grid.getValue(current_row, current_col)) {
          return false;
        }

      }
    }

    return true;

  }

}
