package sudoku;

class GridState implements IGridState {

  private int[][] grid;
  private final int size;

  /**
   * Initialises an empty sudoku grid with the given size.
   * <p>Each row/column will be formed of {@code rows} rows and {@code columns} columns of 3x3
   * sudoku squares.
   * @author Jamie
   * @param rows The number of rows of sudoku squares in the grid.
   * @param columns The number of columns of sudoku squares in the grid.
   * @return The created empty grid.
   */
  public GridState() {

    int[][] new_grid = new int[9][9];

    for (int row = 0; row < new_grid.length; row++) {
      for (int column = 0; column < new_grid[row].length; column++) {
        new_grid[row][column] = -1;
      }
    }

    grid = new_grid;
    size = 3;

  }

  @Override
  public int getValue(int row, int column) {
    return grid[row][column];
  }

  @Override
  public void setValue(int row, int column, int value) {
    if ((value < 1 || value > 9) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 " +
          "inclusive.");
    } else {
      grid[row][column] = value;
    }
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public int getCellSize() {
    return size * size;
  }

  @Override
  public boolean equals(Object other) {
    
    if (!(other instanceof GridState)) {
      return false;
    }

    GridState casted_other = (GridState) other;
    for (int row = 0; row < (size * size); row++) {
      for (int col = 0; col < (size * size); col++) {
        if (getValue(row, col) != casted_other.getValue(row, col)) {
          return false;
        }
      }
    }

    return true;

  }

  @Override
  public int hashCode() {
    int hash = 17;
    for (int[] row : grid) {
      for (int col : row) {
        hash *= col;
      }
    }
    return hash;
  }

  @Override
  public GridState clone() {

    // Create a deep copy
    GridState grid_clone = new GridState();
    for (int i = 0; i < size * size; i++) {
      grid_clone.grid[i] = grid_clone.grid[i].clone();
    }
    return grid_clone;

  }

}
