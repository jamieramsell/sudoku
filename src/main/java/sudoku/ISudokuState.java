package sudoku;

interface ISudokuState extends Cloneable {

  /**
   * Checks whether duplicates of the given cell value exist within the ISudokuState which contains 
   * it.
   * <p> If this state contains another {@code ISudokuState}, the method must check within the
   * smallest (deepest) {@code ISudokuState} which contains the cell.
   * @param row The row (or y-coordinate) of the target cell.
   * @param column The column (or x-coordinate) of the target cell.
   * @return whether any duplicates of the value at the given coordinates exist within the smallest
   * {@code ISudokuState} which contains it.
   */
  public boolean checkForDuplicates(int row, int column);

  // Getters //
  
  /**
   * Gets the current value of a cell in the grid.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * @author Jamie
   * @param row The row, or y-coordinate, of the cell
   * @param column The column, or x-coordinate, of the cell
   * @return The value currently stored at the cell, or -1 if the cell is empty.
   */
  public int getValue(int row, int column);

  /** @return the size of the grid. */
  public int getSize();

  /**
   * Gets the size of the grid in terms of the number of cells it contains.
   * @author Jamie
   * @return a tuple containing the number of rows and columns in the grid respectively.
   */
  public Tuple2<Integer, Integer> getGridDimensions();

  // Setters //

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

  // Object Overrides //

  public ISudokuState clone();

}
