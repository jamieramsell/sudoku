package sudoku.generation.symmetry;

import java.util.ArrayList;
import java.util.List;

import sudoku.Tuple2;

/**
 * The givens are mirrored vertically, creating identical left-to-right halves.
 */
public class ReflectionalSymmetry extends AbstractSymmetry {

  public ReflectionalSymmetry(int size) {
    super(size);
  }

  @Override
  public List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col) {
    checkBounds(row, col);
    
    List<Tuple2<Integer, Integer>> cells = new ArrayList<>();
    cells.add(new Tuple2<>(row, col));

    // Vertical mirror symmetry (left-right)
    int sym_col = size - 1 - col;
    if (sym_col != col) {
      cells.add(new Tuple2<>(row, sym_col));
    }

    return cells;
  }

}
