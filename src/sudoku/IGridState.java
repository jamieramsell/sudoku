package sudoku;

public interface IGridState extends Cloneable {

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
   * Gets the size of the grid in terms of the number of sudoku squares it contains
   * @author Jamie
   * @return the number of rows / columns in the grid.
   */
  public int getSize();

  /**
   * Gets the size of the grid in terms of the number of cells it contains
   * @author Jamie
   * @return the number of rows / columns in the grid.
   */
  public int getCellSize();

  /**
   * Creates a copy of this grid state.
   * @author Jamie
   * @return A deep copy of this IGridState
   */
  public IGridState clone();

}
