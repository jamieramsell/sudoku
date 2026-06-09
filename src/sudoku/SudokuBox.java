package sudoku;

class SudokuBox implements ISudokuState {

  /**
   * Calculates the dimensions of the boxes which make up a sudoku grid of size {@code size}.
   * <p> Note that {@code 2 <= size <= 16}, where {@code size} is either even, square, or both.
   * 
   * @param size The size of the sudoku grid.
   * @returns the dimensions (rows, columns) of the boxes which make up a sudoku grid of the given
   * size.
   */
  public static Tuple2<Integer, Integer> calculateBoxSize(int size) {
    if (size < 2 || size > 16 || (size % 2 != 0 && !isSquare(size))) {
      throw new IllegalArgumentException("size must meet the following conditions:\n"
          + "* 2 <= size <= 16;`n"
          + "* size must be even, square, or both."
      );
    } 

    int n = (int) Math.sqrt(size);

    if (isSquare(size)) { // Square value of size means square boxes.
      return new Tuple2<>(n, n);
    }

    /* Any divisor n <= sqrt(size) gives a valid box.
     * Starting at floor(sqrt(size)), decrementing finds the largest value of n (the most
     * square-like pair), and always terminates at worst at n = 2, as size is always even or square.
     */

    while (size % n != 0) {
      n--;
    }

    Tuple2<Integer, Integer> dimensions = new Tuple2<>(n, size / n);
    return dimensions;
  }

  /** Convenience method which checks whether a given value is square. */
  static boolean isSquare(int value) {
    int sqrt = (int) Math.sqrt(value);
    return (sqrt * sqrt == value);
  }

  // Attributes
  private final int[][] grid;
  private final int size;
  private final int rows;
  private final int columns;

  /**
   * Initialises an empty sudoku box with the given size.
   * <p> A size of 9 means that the sudoku box contains the numbers 1-9.
   * <p> Sizes supported are all values {@code x}, where {@code 2 <= x <= 16}, and {@code x} is
   * either even, square, or both.
   * @param size The size of the sudoku box to generate
   */
  public SudokuBox(int size) {
    // Validate box size
    ISudokuGrid.validateGridSize(size);
    this.size = size;

    // Generate box dimensions
    Tuple2<Integer, Integer> dimensions = calculateBoxSize(size);
    this.rows = dimensions.first();
    this.columns = dimensions.second();

    // Generate grid of box
    int[][] grid = new int[rows][columns];
    for (int row = 0; row < rows; row++) {
      for (int column = 0; column < columns; column++) {
        grid[row][column] = -1;
      }
    }
    this.grid = grid;
  }

  @Override
  public boolean checkForDuplicates(int row, int column) {
    final int value = getValue(row, column);

    for (int current_row = 0; current_row < rows; current_row++) {
      for (int current_col = 0; current_col < columns; current_col++) {
        if (current_row == row && current_col == column) {
          continue; // Ignore target cell
        } else if (getValue(current_row, current_col) == value) {
          return true;
        }
      }
    }

    return false;
  }

  // Getters //

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public Tuple2<Integer, Integer> getGridDimensions() {
    return new Tuple2<>(rows, columns);
  }

  @Override
  public int getValue(int row, int column) {
    return grid[row][column];
  }

  // Setters //

  @Override
  public void setValue(int row, int column, int value) {
    if ((value < 1 || value > size) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and " + size +
          " inclusive.");
    } else {
      grid[row][column] = value;
    }
  }

  // Object Overrides //
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof SudokuBox)) {
      return false;
    }

    SudokuBox casted_other = (SudokuBox) other;

    // Check whether both boxes have the same dimensions
    if (!(getGridDimensions().equals(casted_other.getGridDimensions()))) {
      return false;
    }

    // Check whether each value in both boxes are equal
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < columns; col++) {
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
      for (int value : row) {
        if (value == -1) {
          value = 0;
        }
        hash = 31 * hash + value;
      }
    }
    return hash;
  }

  @Override
  public SudokuBox clone() {
    SudokuBox grid_clone = new SudokuBox(size);
    for (int i = 0; i < rows; i++) {
      grid_clone.grid[i] = this.grid[i].clone();
    }
    return grid_clone;
  }

}
