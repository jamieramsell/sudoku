package sudoku.generation.symmetry;

import java.util.ArrayList;
import java.util.List;

import sudoku.Tuple2;

/**
 * There is to be no enforced symmetry in the puzzle.
 */
public class NoSymmetry extends AbstractSymmetry {

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
