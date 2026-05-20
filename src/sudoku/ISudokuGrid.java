package sudoku;

public interface ISudokuGrid {

  /**
   * Initialises an empty sudoku grid with the given size.
   * <p>Each row/column will be formed of {@code rows} rows and {@code columns} columns of 3x3
   * sudoku squares.
   * @author Jamie
   * @param rows The number of rows of sudoku squares in the grid.
   * @param columns The number of columns of sudoku squares in the grid.
   * @return A copy of the created sudoku grid.
   */
  public int[][] initialiseGrid(int rows, int columns);

  /**
   * Gets the current value of a cell in a sudoku grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * @author Jamie
   * @param row The row, or x-coordinate, of the cell
   * @param column The column, or y-coordinate, of the cell
   * @return The value currently stored at the cell, or -1 if the cell is empty.
   */
  public int getValue(int row, int column);

  /**
   * Sets the value of a cell in a sudoku grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * <p>Note that {@code 1 <= value <= 9}, or to set the cell as empty, {@code value = -1}
   * @author Jamie
   * @param row The row, or x-coordinate, of the cell
   * @param column The column, or y-coordinate, of the cell
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
  public int[][] resetGrid();

  /**
   * Outputs the current state of the sudoku grid.
   * @author Jamie
   */
  public void displayGrid();

  /**
   * Gets the size of the grid in terms of the number of sudoku squares it contains
   * @author Jamie
   * @return a tuple of two integers, representing the number of rows, and the number of columns in
   * the grid respectively.
   */
  public Tuple2<Integer, Integer> getGridSize();

}
