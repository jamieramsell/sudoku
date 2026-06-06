package sudoku.generation.symmetry;

import java.util.ArrayList;
import java.util.List;

import sudoku.Tuple2;

/**
 * The puzzle is to be symmetric both rotationally and reflectionally.
 */
public class DihedralSymmetry extends AbstractSymmetry {

  private RotationalSymmetry rotational;
  private ReflectionalSymmetry reflectional;
  
  public DihedralSymmetry(int grid_cell_size) {
    super(grid_cell_size);
    rotational = new RotationalSymmetry(grid_cell_size);
    reflectional = new ReflectionalSymmetry(grid_cell_size);
  }

  @Override
  public List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col) {
    checkBounds(row, col);

    List<Tuple2<Integer, Integer>> cells = new ArrayList<>();
    cells.addAll(rotational.getSymmetricCells(row, col));
    cells.addAll(reflectional.getSymmetricCells(row, col));

    return cells;
  }

}
