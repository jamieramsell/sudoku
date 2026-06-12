package sudoku.generation.symmetry;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import sudoku.Tuple2;

/**
 * The puzzle is to be symmetric both rotationally and reflectionally.
 */
public class DihedralSymmetry extends AbstractSymmetry {

  private RotationalSymmetry rotational;
  private ReflectionalSymmetry reflectional;

  /**
   * Creates a dihedral (rotational and reflectional) symmetry strategy for a grid of the given
   * size.
   * @param size The size of the Sudoku grid that cells will be removed from.
   */
  public DihedralSymmetry(int size) {
    super(size);
    rotational = new RotationalSymmetry(size);
    reflectional = new ReflectionalSymmetry(size);
  }

  @Override
  public List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col) {
    checkBounds(row, col);

    // Use a set to avoid any duplicates
    Set<Tuple2<Integer, Integer>> cell_set = new HashSet<>();
    cell_set.addAll(rotational.getSymmetricCells(row, col));
    cell_set.addAll(reflectional.getSymmetricCells(row, col));

    List<Tuple2<Integer, Integer>> cells = new ArrayList<>(cell_set);

    return cells;
  }

}
