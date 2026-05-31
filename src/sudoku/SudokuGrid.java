package sudoku;

public class SudokuGrid implements ISudokuGrid {

  final int DEFAULT_SIZE = 3;

  int size;
  IGridState grid;

  public SudokuGrid() {
    initialiseAttributes(DEFAULT_SIZE);
  }

  public SudokuGrid(int size) {
    throw new UnsupportedOperationException();
  }

  /* To do - finish constructor
   * create a random puzzle
   * store its solution
   */

  // Convenience method to allow for two constructors with very similar functionality
  private void initialiseAttributes(int size) {
    this.size = size;
    this.grid = new GridState();
  }

  @Override
  public int getValue(int row, int column) {
    return grid.getValue(row, column);
  }

  @Override
  public void setValue(int row, int column, int value) {
    if ((value < 1 || value > 9) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 " +
          "inclusive.");
    } else {
      grid.setValue(row, column, value);;
    }
  }

  @Override
  public boolean isValid() {
    return ISudokuSolver.isGridStateValid(getGrid());
  }

  @Override
  public boolean isSolved() {
    return (isValid() && !hasEmptyCells());
  }

  // Convenience function for isSolved() method to check whether the grid has any empty cells
  private boolean hasEmptyCells() {
    for (int row = 0; row < (size * size); row++) {
      for (int col = 0; col < (size * size); col++) {
        if (getValue(row, col) == -1) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public IGridState resetGrid() {

    for (int row = 0; row < size * 3; row++) {
      for (int col = 0; col < size * 3; col++) {
        grid.setValue(row, col, -1);
      }
    }

    return getGrid();

  }

  // To do - update this to use a GUI in a later version
  @Override
  public void displayGrid() {
    System.out.println(toString());
  }

  // To do - optimise toString() to use cache rather than generate a new string every time.
  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    String separator = "-".repeat(formatRow(0).length());

    for (int row = 0; row < (size * size); row++) {
      if (row > 0) {
        output.append('\n');
      
        if (row % 3 == 0) {
          output.append(separator).append('\n');
        }
      }

      output.append(formatRow(row));
    }

    return output.toString();
  }
  
  // Convenience method to format each row of toString() method
  private String formatRow(int row) {
    StringBuilder rowOutput = new StringBuilder();

    for (int column = 0; column < (size * size); column++) {
      if (column > 0) {
        rowOutput.append(' ');

        if (column % 3 == 0) {
          rowOutput.append("| ");
        }
      }
      int value = grid.getValue(row, column);
      if (value == -1) {
        rowOutput.append('X');
      } else {
        rowOutput.append(value);
      }
    }

    return rowOutput.toString();
  }
  
  @Override
  public int[] getGridSize() {
    return new int[]{size, size};
  }

  @Override
  public IGridState getGrid() {
    return grid.clone();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof SudokuGrid)) {
      return false;
    }
    SudokuGrid casted_other = (SudokuGrid) other;
    return grid.equals(casted_other.getGrid());
  }

}
