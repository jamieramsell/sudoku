package sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/** Tests for {@link SudokuGridState}. */
public class SudokuGridStateTest {

  private SudokuGridState grid;

  @Before
  public void setUp() {
    grid = new SudokuGridState();
  }

  @Test
  public void defaultConstructor_createsNineByNineGrid() {
    assertEquals(9, grid.getSize());
    assertEquals(new Tuple2<>(9, 9), grid.getGridDimensions());
  }

  @Test
  public void sizedConstructor_createsGridOfRequestedSize() {
    SudokuGridState small = new SudokuGridState(4);

    assertEquals(4, small.getSize());
    assertEquals(new Tuple2<>(4, 4), small.getGridDimensions());
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_invalidSize_throws() {
    new SudokuGridState(7);
  }

  @Test
  public void newGrid_allCellsEmpty() {
    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        assertEquals(-1, grid.getValue(row, col));
      }
    }
  }

  @Test
  public void setValue_andGetValue_roundTrip() {
    grid.setValue(0, 0, 5);
    assertEquals(5, grid.getValue(0, 0));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getValue_outOfBounds_throws() {
    grid.getValue(9, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setValue_outOfBounds_throws() {
    grid.setValue(-1, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValue_invalidValue_throws() {
    grid.setValue(0, 0, 10);
  }

  @Test
  public void checkForDuplicates_onlyChecksWithinContainingBox() {
    // A duplicate within the same 3x3 box is detected.
    grid.setValue(0, 0, 5);
    grid.setValue(1, 1, 5);
    assertTrue(grid.checkForDuplicates(0, 0));

    // The same value in the same row but a different box is NOT detected by
    // this method - row/column checks are handled elsewhere (ISudokuSolver).
    SudokuGridState fresh = new SudokuGridState();
    fresh.setValue(0, 0, 5);
    fresh.setValue(0, 4, 5);
    assertFalse(fresh.checkForDuplicates(0, 0));
  }

  @Test
  public void clone_isIndependentDeepCopy() {
    grid.setValue(0, 0, 5);
    SudokuGridState clone = grid.clone();

    assertEquals(grid, clone);

    clone.setValue(0, 0, 1);
    assertNotEquals(grid.getValue(0, 0), clone.getValue(0, 0));
  }

  @Test
  public void equals_sameValues_returnsTrue() {
    SudokuGridState other = new SudokuGridState();

    assertEquals(grid, other);
    assertEquals(grid.hashCode(), other.hashCode());

    grid.setValue(0, 0, 3);
    other.setValue(0, 0, 3);
    assertEquals(grid, other);
  }

  @Test
  public void equals_differentValues_returnsFalse() {
    SudokuGridState other = new SudokuGridState();
    grid.setValue(0, 0, 3);

    assertNotEquals(grid, other);
  }

  @Test
  public void equals_differentSize_returnsFalse() {
    SudokuGridState other = new SudokuGridState(4);
    assertNotEquals(grid, other);
  }

  @Test
  public void equals_nonSudokuGridStateOrNull_returnsFalse() {
    assertNotEquals(grid, "not a grid");
    assertNotEquals(grid, null);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getValue_columnOutOfBounds_throws() {
    grid.getValue(0, 9);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setValue_columnOutOfBounds_throws() {
    grid.setValue(0, -1, 1);
  }

  @Test
  public void nonSquareBoxes_constructorProducesSizeBySizeGrid() {
    // size 6 -> 2x3 boxes, arranged 3 box-rows by 2 box-columns.
    SudokuGridState size_six = new SudokuGridState(6);

    assertEquals(6, size_six.getSize());
    assertEquals(new Tuple2<>(6, 6), size_six.getGridDimensions());
  }

  @Test
  public void checkForDuplicates_nonSquareBox_detectsDuplicateWithinSameBox() {
    // For size 6, boxes are 2 rows x 3 columns. Cells (0,0) and (1,2) both fall within the
    // top-left box.
    SudokuGridState size_six = new SudokuGridState(6);
    size_six.setValue(0, 0, 5);
    size_six.setValue(1, 2, 5);

    assertTrue(size_six.checkForDuplicates(0, 0));
    assertTrue(size_six.checkForDuplicates(1, 2));
  }

  @Test
  public void checkForDuplicates_nonSquareBox_ignoresCellsInDifferentBox() {
    // Cell (2,0) is in a different box-row from (0,0) (box rows span 2 cells), even though it
    // shares the same column.
    SudokuGridState size_six = new SudokuGridState(6);
    size_six.setValue(0, 0, 5);
    size_six.setValue(2, 0, 5);

    assertFalse(size_six.checkForDuplicates(0, 0));
    assertFalse(size_six.checkForDuplicates(2, 0));
  }

}
