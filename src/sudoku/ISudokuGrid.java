package sudoku;

public interface ISudokuGrid {

  /**
   * Initialises an empty sudoku grid with the given size.
   * <p>Each row/column will be formed of {@code rows} rows and {@code columns} columns of 3x3
   * sudoku squares.
   * @author Jamie
   * @param rows The number of rows of sudoku squares in the grid.
   * @param columns The number of columns of sudoku squares in the grid.
   * @return The created sudoku grid.
   */
  public int[][] initialiseGrid(int rows, int columns);

  /**
   * Gets the current value of a square in a sudoku grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most square in the grid.
   * @author Jamie
   * @param row The row, or x-coordinate, of the square
   * @param column The column, or y-coordinate, of the square
   * @return The value currently stored at the square, or -1 if the square is empty.
   */
  public int getValue(int row, int column);

  /**
   * Sets the value of a square in a sudoku grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most square in the grid.
   * @author Jamie
   * @param row The row, or x-coordinate, of the square
   * @param column The column, or y-coordinate, of the square
   */
  public void setValue(int row, int column);

  /**
   * Determines whether or not the current state of the sudoku grid is a valid solution so far.
   * @author Jamie
   * @return true if the grid is valid; false if there are any incompatible squares.
   */
  public boolean isValid();

  /**
   * Determines whether or not the sudoku grid has yet been solved.
   * @author Jamie
   * @return true if the grid has been solved; false if there are any incompatible or empty squares.
   */
  public boolean isSolved();

  /**
   * Empties the sudoku grid of values.
   * @author Jamie
   * @return the emptied grid.
   */
  public int[][] resetGrid();

  /**
   * Outputs the current state of the sudoku grid.
   * @author Jamie
   */
  public void displayGrid();

}
