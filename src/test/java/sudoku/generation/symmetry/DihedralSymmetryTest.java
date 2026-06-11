package sudoku.generation.symmetry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import sudoku.Tuple2;

/** Tests for {@link DihedralSymmetry}. */
public class DihedralSymmetryTest {

  @Test
  public void getSymmetricCells_unionsRotationalAndReflectionalCells() {
    DihedralSymmetry symmetry = new DihedralSymmetry(9);

    // (0,0) -> rotational counterpart (8,8), reflectional counterpart (0,8).
    List<Tuple2<Integer, Integer>> cells = symmetry.getSymmetricCells(0, 0);

    assertEquals(3, cells.size());
    assertTrue(cells.contains(new Tuple2<>(0, 0)));
    assertTrue(cells.contains(new Tuple2<>(8, 8)));
    assertTrue(cells.contains(new Tuple2<>(0, 8)));
  }

  @Test
  public void getSymmetricCells_centerCellOfOddGrid_returnsSingleCell() {
    // The center cell maps to itself under both rotation and reflection.
    DihedralSymmetry symmetry = new DihedralSymmetry(9);

    List<Tuple2<Integer, Integer>> cells = symmetry.getSymmetricCells(4, 4);

    assertEquals(1, cells.size());
    assertTrue(cells.contains(new Tuple2<>(4, 4)));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getSymmetricCells_outOfBounds_throws() {
    new DihedralSymmetry(9).getSymmetricCells(0, 9);
  }

}
