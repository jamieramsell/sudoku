package sudoku.generation.symmetry;

import java.util.ArrayList;
import java.util.List;

import sudoku.Tuple2;

/**
 * Two-fold (180 degree) rotation: the grid looks the same if you turn it completely upside
 * down.
 */
public class RotationalSymmetry extends AbstractSymmetry {

  public RotationalSymmetry(int size) {
    super(size);
  }

  @Override
  public List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col) {
    checkBounds(row, col);
    
    List<Tuple2<Integer, Integer>> cells = new ArrayList<>();
    cells.add(new Tuple2<>(row, col));
    
    // 180-degree rotational symmetry
    int sym_row = size - 1 - row;
    int sym_col = size - 1 - col;
    if (sym_row != row || sym_col != col) {
      cells.add(new Tuple2<>(sym_row, sym_col));
    }

    return cells;
  }

}
