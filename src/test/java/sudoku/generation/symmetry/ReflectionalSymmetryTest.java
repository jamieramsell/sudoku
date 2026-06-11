package sudoku.generation.symmetry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import sudoku.Tuple2;

/** Tests for {@link ReflectionalSymmetry}. */
public class ReflectionalSymmetryTest {

  @Test
  public void getSymmetricCells_returnsCellAndItsMirror() {
    ReflectionalSymmetry symmetry = new ReflectionalSymmetry(9);

    List<Tuple2<Integer, Integer>> cells = symmetry.getSymmetricCells(0, 0);

    assertEquals(2, cells.size());
    assertTrue(cells.contains(new Tuple2<>(0, 0)));
    assertTrue(cells.contains(new Tuple2<>(0, 8)));
  }

  @Test
  public void getSymmetricCells_centerColumnOfOddGrid_returnsSingleCell() {
    ReflectionalSymmetry symmetry = new ReflectionalSymmetry(9);

    List<Tuple2<Integer, Integer>> cells = symmetry.getSymmetricCells(0, 4);

    assertEquals(1, cells.size());
    assertTrue(cells.contains(new Tuple2<>(0, 4)));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getSymmetricCells_outOfBounds_throws() {
    new ReflectionalSymmetry(9).getSymmetricCells(0, 9);
  }

}
