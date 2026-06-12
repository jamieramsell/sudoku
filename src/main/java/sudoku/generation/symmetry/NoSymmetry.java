package sudoku.generation.symmetry;

import java.util.ArrayList;
import java.util.List;

import sudoku.Tuple2;

/**
 * There is to be no enforced symmetry in the puzzle.
 */
public class NoSymmetry extends AbstractSymmetry {

  /**
   * Creates a no-symmetry strategy for a grid of the given size.
   * @param size The size of the Sudoku grid that cells will be removed from.
   */
  public NoSymmetry(int size) {
    super(size);
  }

  @Override
  public List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col) {
    checkBounds(row, col);

    List<Tuple2<Integer, Integer>> cells = new ArrayList<>();
    cells.add(new Tuple2<>(row, col));

    return cells;
  }

}
