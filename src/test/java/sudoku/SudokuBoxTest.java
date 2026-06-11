package sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/** Tests for {@link SudokuBox}. */
public class SudokuBoxTest {

  private SudokuBox box;

  @Before
  public void setUp() {
    box = new SudokuBox(ISudokuGrid.DEFAULT_SIZE);
  }

  @Test
  public void calculateBoxSize_squareSizes_returnsSquareDimensions() {
    assertEquals(new Tuple2<>(3, 3), SudokuBox.calculateBoxSize(9));
    assertEquals(new Tuple2<>(2, 2), SudokuBox.calculateBoxSize(4));
    assertEquals(new Tuple2<>(4, 4), SudokuBox.calculateBoxSize(16));
  }

  @Test
  public void calculateBoxSize_nonSquareSizes_returnsMostSquareLikePair() {
    assertEquals(new Tuple2<>(2, 3), SudokuBox.calculateBoxSize(6));
    assertEquals(new Tuple2<>(3, 4), SudokuBox.calculateBoxSize(12));
    assertEquals(new Tuple2<>(2, 4), SudokuBox.calculateBoxSize(8));
    assertEquals(new Tuple2<>(2, 5), SudokuBox.calculateBoxSize(10));
    assertEquals(new Tuple2<>(1, 2), SudokuBox.calculateBoxSize(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void calculateBoxSize_invalidSize_throws() {
    SudokuBox.calculateBoxSize(7);
  }

  @Test
  public void isSquare_returnsExpectedResult() {
    assertTrue(SudokuBox.isSquare(9));
    assertTrue(SudokuBox.isSquare(16));
    assertFalse(SudokuBox.isSquare(6));
    assertFalse(SudokuBox.isSquare(2));
  }

  @Test
  public void newBox_allCellsEmpty() {
    Tuple2<Integer, Integer> dimensions = box.getGridDimensions();

    for (int row = 0; row < dimensions.first(); row++) {
      for (int col = 0; col < dimensions.second(); col++) {
        assertEquals(-1, box.getValue(row, col));
      }
    }
  }

  @Test
  public void setValue_validValue_isStored() {
    box.setValue(0, 0, 5);
    assertEquals(5, box.getValue(0, 0));
  }

  @Test
  public void setValue_emptyMarker_isStored() {
    box.setValue(0, 0, 5);
    box.setValue(0, 0, -1);

    assertEquals(-1, box.getValue(0, 0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValue_valueAboveSize_throws() {
    box.setValue(0, 0, ISudokuGrid.DEFAULT_SIZE + 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setValue_valueBelowOne_throws() {
    box.setValue(0, 0, 0);
  }

  @Test
  public void checkForDuplicates_duplicateWithinBox_returnsTrue() {
    box.setValue(0, 0, 5);
    box.setValue(1, 1, 5);

    assertTrue(box.checkForDuplicates(0, 0));
    assertTrue(box.checkForDuplicates(1, 1));
  }

  @Test
  public void checkForDuplicates_noDuplicates_returnsFalse() {
    box.setValue(0, 0, 5);
    box.setValue(1, 1, 6);

    assertFalse(box.checkForDuplicates(0, 0));
    assertFalse(box.checkForDuplicates(1, 1));
  }

  @Test
  public void clone_isIndependentDeepCopy() {
    box.setValue(0, 0, 7);
    SudokuBox clone = box.clone();

    assertEquals(box, clone);

    clone.setValue(0, 0, 1);
    assertNotEquals(box.getValue(0, 0), clone.getValue(0, 0));
  }

  @Test
  public void equals_sameValues_returnsTrue() {
    SudokuBox other = new SudokuBox(ISudokuGrid.DEFAULT_SIZE);

    assertEquals(box, other);
    assertEquals(box.hashCode(), other.hashCode());
  }

  @Test
  public void equals_differentDimensions_returnsFalse() {
    SudokuBox other = new SudokuBox(4);
    assertNotEquals(box, other);
  }

  @Test
  public void equals_differentValuesSameDimensions_returnsFalse() {
    SudokuBox other = new SudokuBox(ISudokuGrid.DEFAULT_SIZE);
    box.setValue(0, 0, 4);
    other.setValue(0, 0, 5);

    assertNotEquals(box, other);
  }

  @Test
  public void equals_nonSudokuBoxOrNull_returnsFalse() {
    assertNotEquals(box, "not a box");
    assertNotEquals(box, null);
  }

  @Test
  public void getSize_returnsConstructorSize() {
    assertEquals(ISudokuGrid.DEFAULT_SIZE, box.getSize());

    SudokuBox small = new SudokuBox(4);
    assertEquals(4, small.getSize());
  }

  @Test
  public void setValue_boundaryValues_areStored() {
    box.setValue(0, 0, 1);
    assertEquals(1, box.getValue(0, 0));

    box.setValue(0, 1, ISudokuGrid.DEFAULT_SIZE);
    assertEquals(ISudokuGrid.DEFAULT_SIZE, box.getValue(0, 1));
  }

  @Test
  public void hashCode_differentValues_differ() {
    SudokuBox other = new SudokuBox(ISudokuGrid.DEFAULT_SIZE);
    box.setValue(0, 0, 4);
    other.setValue(0, 0, 5);

    assertNotEquals(box.hashCode(), other.hashCode());
  }

  @Test
  public void nonSquareSize_hasExpectedDimensionsAndStoresValues() {
    SudokuBox box_of_six = new SudokuBox(6);

    assertEquals(new Tuple2<>(2, 3), box_of_six.getGridDimensions());
    assertEquals(6, box_of_six.getSize());

    box_of_six.setValue(1, 2, 6);
    assertEquals(6, box_of_six.getValue(1, 2));
  }

  @Test
  public void checkForDuplicates_emptyCellWithOtherEmptyCells_returnsTrue() {
    // A freshly-created box has every cell set to -1. Since checkForDuplicates() compares the
    // target cell's value (-1) against every other cell, an empty cell is reported as having a
    // "duplicate" as long as at least one other cell is also empty.
    assertTrue(box.checkForDuplicates(0, 0));
  }

  @Test
  public void clone_preservesSize() {
    SudokuBox small = new SudokuBox(4);
    SudokuBox clone = small.clone();

    assertEquals(small.getSize(), clone.getSize());
    assertEquals(small.getGridDimensions(), clone.getGridDimensions());
  }

}
