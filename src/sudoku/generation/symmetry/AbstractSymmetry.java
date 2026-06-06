package sudoku.generation.symmetry;

import java.util.List;

import sudoku.Tuple2;

/**
 * Represents the traditional symmetric pattern of the Sudoku puzzle to be generated.
 * @see https://www.clarity-media.co.uk/viewblog.php?id=sudoku-and-symmetry
 */
public abstract class AbstractSymmetry {

  protected final int grid_cell_size;

  public AbstractSymmetry(int grid_cell_size) {
    this.grid_cell_size = grid_cell_size;
  }

  /**
   * Gets the list of cells that should be removed based on the symmetry pattern.
   * @param row The row of the primary cell
   * @param col The column of the primary cell
   * @return A list of cells to remove, which includes the cell given.
   */
  public abstract List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col);

  protected void checkBounds(int row, int col) {
    if (row < 0 || row >= grid_cell_size) {
      throw new IndexOutOfBoundsException("row does not exist within the sudoku grid.");
    } else if (col < 0 || col >= grid_cell_size) {
      throw new IndexOutOfBoundsException("row does not exist within the sudoku grid.");
    }
  }

}
