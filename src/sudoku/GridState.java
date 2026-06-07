package sudoku;

public class GridState {

  private int[][] grid;
  private final int size;

  /**
   * Initialises an empty 9x9 sudoku grid.
   * <p>Empty cells are represented by {@code -1}.
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

  public int getValue(int row, int column) {
    return grid[row][column];
  }

  public void setValue(int row, int column, int value) {
    if ((value < 1 || value > 9) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 " +
          "inclusive.");
    } else {
      grid[row][column] = value;
    }
  }

  public int getSize() {
    return size;
  }

  public int getCellSize() {
    return size * size;
  }

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

  public int hashCode() {
    int hash = 17;
    for (int[] row : grid) {
      for (int col : row) {
        if (col == -1) {
          col = 0;
        }
        hash = 31 * hash + col;
      }
    }
    return hash;
  }

  public GridState clone() {

    // Create a deep copy
    GridState grid_clone = new GridState();
    for (int i = 0; i < size * size; i++) {
      grid_clone.grid[i] = this.grid[i].clone();
    }
    return grid_clone;

  }

}
