package sudoku.generation.symmetry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import sudoku.Tuple2;

/** Tests for {@link RotationalSymmetry}. */
public class RotationalSymmetryTest {

  @Test
  public void getSymmetricCells_returnsCellAndItsRotationalCounterpart() {
    RotationalSymmetry symmetry = new RotationalSymmetry(9);

    List<Tuple2<Integer, Integer>> cells = symmetry.getSymmetricCells(0, 0);

    assertEquals(2, cells.size());
    assertTrue(cells.contains(new Tuple2<>(0, 0)));
    assertTrue(cells.contains(new Tuple2<>(8, 8)));
  }

  @Test
  public void getSymmetricCells_centerCellOfOddGrid_returnsSingleCell() {
    // The center cell of an odd-sized grid maps to itself under 180-degree rotation.
    RotationalSymmetry symmetry = new RotationalSymmetry(9);

    List<Tuple2<Integer, Integer>> cells = symmetry.getSymmetricCells(4, 4);

    assertEquals(1, cells.size());
    assertTrue(cells.contains(new Tuple2<>(4, 4)));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getSymmetricCells_outOfBounds_throws() {
    new RotationalSymmetry(9).getSymmetricCells(9, 0);
  }

}
