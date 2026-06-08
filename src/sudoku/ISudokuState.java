package sudoku;

interface ISudokuState extends Cloneable {

  /**
   * Gets the current value of a cell in the grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * @author Jamie
   * @param row The row, or y-coordinate, of the cell
   * @param column The column, or x-coordinate, of the cell
   * @return The value currently stored at the cell, or -1 if the cell is empty.
   */
  public int getValue(int row, int column);

  /**
   * Sets the value of a cell in the grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * <p>Note that {@code 1 <= value <= grid size}, or to set the cell as empty, {@code value = -1}
   * @author Jamie
   * @param row The row, or y-coordinate, of the cell
   * @param column The column, or x-coordinate, of the cell
   * @param value The value of the cell
   */
  public void setValue(int row, int column, int value);

  /** @return the size of the grid. */
  public int getSize();

  /**
   * Gets the size of the grid in terms of the number of cells it contains.
   * @author Jamie
   * @return a tuple containing the number of rows and columns in the grid respectively.
   */
  public Tuple2<Integer, Integer> getGridDimensions();

  public ISudokuState clone();

}
