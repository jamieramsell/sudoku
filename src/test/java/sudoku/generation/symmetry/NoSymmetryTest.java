package sudoku.generation.symmetry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import sudoku.Tuple2;

/** Tests for {@link NoSymmetry}. */
public class NoSymmetryTest {

  @Test
  public void getSymmetricCells_returnsOnlyTheGivenCell() {
    NoSymmetry symmetry = new NoSymmetry(9);

    List<Tuple2<Integer, Integer>> cells = symmetry.getSymmetricCells(2, 3);

    assertEquals(1, cells.size());
    assertTrue(cells.contains(new Tuple2<>(2, 3)));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getSymmetricCells_outOfBoundsRow_throws() {
    new NoSymmetry(9).getSymmetricCells(9, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getSymmetricCells_outOfBoundsColumn_throws() {
    new NoSymmetry(9).getSymmetricCells(0, -1);
  }

}
