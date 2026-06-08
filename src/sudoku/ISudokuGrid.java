package sudoku;

public interface ISudokuGrid {

  // Constants
  public final int DEFAULT_SIZE = 9;
  
  /**
   * Validates the size of a sudoku grid.
   * <p> Sizes supported are all values {@code x}, where {@code 2 <= x <= 16}, and {@code x} is
   * either even, square, or both.
   * @param size The size to check.
   * @throws IllegalArgumentException if the size given is not valid.
   */
  public static void validateGridSize(int size) {
    if (size < 2 || size > 16 || (size % 2 != 0 && size % size != 0)) {
      throw new IllegalArgumentException("size must meet the following conditions:\n"
          + "* 2 <= size <= 16;`n"
          + "* size must be even, square, or both."
      );
    }
  }

  /**
   * Gets the current value of a cell in a sudoku grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * @author Jamie
   * @param row The row, or y-coordinate, of the cell
   * @param column The column, or x-coordinate, of the cell
   * @return The value currently stored at the cell, or -1 if the cell is empty.
   */
  public int getValue(int row, int column);

  /**
   * Sets the value of a cell in a sudoku grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * <p>Note that {@code 1 <= value <= grid size}, or to set the cell as empty, {@code value = -1}
   * @author Jamie
   * @param row The row, or y-coordinate, of the cell
   * @param column The column, or x-coordinate, of the cell
   * @param value The value of the cell
   */
  public void setValue(int row, int column, int value);

  /**
   * Determines whether or not the current state of the sudoku grid is a valid solution so far.
   * @author Jamie
   * @return true if the grid is valid; false if there are any incompatible cells.
   */
  public boolean isValid();

  /**
   * Determines whether or not the sudoku grid has yet been solved.
   * @author Jamie
   * @return true if the grid has been solved; false if there are any incompatible or empty cells.
   */
  public boolean isSolved();

  /**
   * Empties the sudoku grid of values, updates the stored grid, and returns a copy of it.
   * @author Jamie
   * @return the emptied grid.
   */
  public GridState resetGrid();

  /**
   * Outputs the current state of the sudoku grid.
   * @author Jamie
   */
  public void displayGrid();

  /**
   * Gets the size of the grid in terms of the number of sudoku squares it contains
   * @author Jamie
   * @return a list of two integers, representing the number of rows, and the number of columns in
   * the grid respectively.
   */
  public int[] getGridSize();

  /**
   * Returns a copy of the current state of the sudoku grid.
   * @author Jamie
   * @return the current state of the grid.
   */
  public GridState getGrid();

}
