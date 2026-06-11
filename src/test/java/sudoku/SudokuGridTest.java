package sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/** Tests for {@link SudokuGrid}. */
public class SudokuGridTest {

  private SudokuGrid grid;

  @Before
  public void setUp() {
    grid = new SudokuGrid();
  }

  @Test
  public void defaultConstructor_createsNineByNineGrid() {
    assertEquals(9, grid.getSize());
    assertEquals(new Tuple2<>(9, 9), grid.getGridDimensions());
  }

  @Test
  public void constructor_supportsCustomSizes() {
    for (int size : new int[] {2, 4, 6, 8, 9, 10, 12, 14, 16}) {
      SudokuGrid custom = new SudokuGrid(size);
      assertEquals(size, custom.getSize());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructor_invalidSize_throws() {
    new SudokuGrid(7);
  }

  @Test
  public void validateGridSize_validSizes_doNotThrow() {
    for (int size : new int[] {2, 4, 6, 8, 9, 10, 12, 14, 16}) {
      ISudokuGrid.validateGridSize(size);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateGridSize_tooSmall_throws() {
    ISudokuGrid.validateGridSize(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateGridSize_tooLarge_throws() {
    ISudokuGrid.validateGridSize(17);
  }

  @Test(expected = IllegalArgumentException.class)
  public void validateGridSize_oddNonSquare_throws() {
    ISudokuGrid.validateGridSize(11);
  }

  @Test
  public void newGrid_isValidButNotSolved() {
    assertTrue(grid.isValid());
    assertFalse(grid.isSolved());
  }

  @Test
  public void setValue_andGetValue_roundTrip() {
    grid.setValue(0, 0, 7);
    assertEquals(7, grid.getValue(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValue_invalidValue_throws() {
    grid.setValue(0, 0, 10);
  }

  @Test
  public void isValid_detectsRowDuplicate() {
    grid.setValue(0, 0, 5);
    grid.setValue(0, 1, 5);

    assertFalse(grid.isValid());
  }

  @Test
  public void resetGrid_clearsAllCells() {
    grid.setValue(0, 0, 5);
    grid.setValue(8, 8, 9);

    grid.resetGrid();

    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        assertEquals(-1, grid.getValue(row, col));
      }
    }
  }

  @Test
  public void isSolved_trueOnlyForCompleteValidGrid() {
    SudokuGrid small = new SudokuGrid(4);
    SudokuSolver solver = new SudokuSolver(small);

    assertFalse(small.isSolved());

    Set<ISudokuGrid> solutions = solver.solveGrid(1);
    ISudokuGrid solved = solutions.iterator().next();

    assertTrue(solved.isSolved());
  }

  @Test
  public void toString_reflectsCurrentGridStateAfterEdits() {
    SudokuGrid small = new SudokuGrid(4);
    String empty_repr = small.toString();
    assertTrue(empty_repr.contains("X"));

    small.setValue(0, 0, 4);
    String updated_repr = small.toString();

    assertNotEquals(empty_repr, updated_repr);
    assertFalse(updated_repr.startsWith("X"));
  }

  @Test
  public void clone_isIndependentDeepCopy() {
    grid.setValue(0, 0, 4);
    SudokuGrid clone = grid.clone();

    assertEquals(grid, clone);

    clone.setValue(0, 0, 5);
    assertNotEquals(grid.getValue(0, 0), clone.getValue(0, 0));
  }

  @Test
  public void equals_sameValues_returnsTrue() {
    SudokuGrid other = new SudokuGrid();

    assertEquals(grid, other);
    assertEquals(grid.hashCode(), other.hashCode());
  }

  @Test
  public void equals_differentValues_returnsFalse() {
    SudokuGrid other = new SudokuGrid();
    grid.setValue(0, 0, 4);

    assertNotEquals(grid, other);
  }

  @Test
  public void equals_nonSudokuGridOrNull_returnsFalse() {
    assertNotEquals(grid, "not a grid");
    assertNotEquals(grid, null);
  }

  @Test
  public void checkForDuplicates_delegatesToUnderlyingGridState() {
    grid.setValue(0, 0, 5);
    grid.setValue(1, 1, 5);

    assertTrue(grid.checkForDuplicates(0, 0));
    assertTrue(grid.checkForDuplicates(1, 1));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void getValue_outOfBounds_throws() {
    grid.getValue(9, 0);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void setValue_outOfBoundsCoordinates_throws() {
    grid.setValue(0, 9, 5);
  }

  @Test
  public void toString_emptyFourByFourGrid_matchesExpectedFormat() {
    SudokuGrid small = new SudokuGrid(4);

    String expected = "X X | X X"
        + "\n\n"
        + "X X | X X"
        + "\n\n"
        + "---------"
        + "\n\n"
        + "X X | X X"
        + "\n\n"
        + "X X | X X";

    assertEquals(expected, small.toString());
  }

  @Test
  public void toString_afterEdit_onlyAffectedRowChanges() {
    SudokuGrid small = new SudokuGrid(4);
    String[] original_lines = small.toString().split("\n\n");

    small.setValue(2, 0, 3);
    String[] updated_lines = small.toString().split("\n\n");

    // Lines correspond to: row0, row1, separator, row2, row3.
    assertEquals(original_lines[0], updated_lines[0]);
    assertEquals(original_lines[1], updated_lines[1]);
    assertEquals(original_lines[2], updated_lines[2]); // dash separator unchanged
    assertNotEquals(original_lines[3], updated_lines[3]); // row 2 changed
    assertEquals("3 X | X X", updated_lines[3]);
    assertEquals(original_lines[4], updated_lines[4]);
  }

  @Test
  public void toString_multipleEditsAcrossRows_allReflected() {
    SudokuGrid small = new SudokuGrid(4);

    small.setValue(0, 0, 1);
    small.setValue(1, 1, 2);
    small.setValue(3, 3, 4);

    String repr = small.toString();
    String[] lines = repr.split("\n\n");

    assertEquals("1 X | X X", lines[0]);
    assertEquals("X 2 | X X", lines[1]);
    assertEquals("X X | X 4", lines[4]);
  }

  @Test
  public void resetGrid_restoresAllXRepresentation() {
    SudokuGrid small = new SudokuGrid(4);
    small.setValue(0, 0, 1);
    small.setValue(3, 3, 4);

    small.resetGrid();

    String expected = "X X | X X"
        + "\n\n"
        + "X X | X X"
        + "\n\n"
        + "---------"
        + "\n\n"
        + "X X | X X"
        + "\n\n"
        + "X X | X X";

    assertEquals(expected, small.toString());
  }

  @Test
  public void toString_largeGrid_usesBase17DigitsAboveNine() {
    SudokuGrid large = new SudokuGrid(16);

    // 16 -> 'G' (16 - 10 = 6 -> 'G' is index 6 of "ABCDEFG")
    // 10 -> 'A' (10 - 10 = 0 -> 'A' is index 0 of "ABCDEFG")
    large.setValue(0, 0, 16);
    large.setValue(0, 1, 10);

    String first_row = large.toString().split("\n\n")[0];

    assertTrue(first_row.startsWith("G"));
    assertTrue(first_row.contains("A"));
  }

}
