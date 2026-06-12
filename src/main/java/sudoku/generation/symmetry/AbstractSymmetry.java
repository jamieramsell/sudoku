package sudoku.generation.symmetry;

import java.util.List;

import sudoku.Tuple2;

/**
 * Represents the traditional symmetric pattern of the Sudoku puzzle to be generated.
 * @see https://www.clarity-media.co.uk/viewblog.php?id=sudoku-and-symmetry
 */
public abstract class AbstractSymmetry {

  protected final int size;

  /**
   * Creates a symmetry strategy for a grid of the given size.
   * @param size The size of the Sudoku grid that cells will be removed from.
   */
  public AbstractSymmetry(int size) {
    this.size = size;
  }

  /**
   * Gets the list of cells that should be removed based on the symmetry pattern.
   * @param row The row of the primary cell
   * @param col The column of the primary cell
   * @return A list of cells to remove, which includes the cell given.
   */
  public abstract List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col);

  /**
   * Verifies that the given {@code (row, col)} coordinate falls within the sudoku grid.
   * @param row The row (or y-coordinate) of the cell to check
   * @param col The column (or x-coordinate) of the cell to check
   * @throws IndexOutOfBoundsException if the given coordinate is out of bounds.
   */
  protected void checkBounds(int row, int col) {
    if (row < 0 || row >= size) {
      throw new IndexOutOfBoundsException("row does not exist within the sudoku grid.");
    } else if (col < 0 || col >= size) {
      throw new IndexOutOfBoundsException("col does not exist within the sudoku grid.");
    }
  }

}
