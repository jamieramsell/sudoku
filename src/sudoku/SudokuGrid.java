package sudoku;

import java.util.List;
import java.util.ArrayList;

public class SudokuGrid implements ISudokuGrid {

  // Attributes
  private final ISudokuState grid;
  private final int size;
  private final Tuple2<Integer, Integer> box_dimensions;
  private StringCache string_cache;

  /**
   * The cache used to optimise the generation of a String representation of a Sudoku grid.
   * <p> The cache's implementation means that unnecessary string generation can be avoided,
   * improving performance.
   */
  private class StringCache {

    private StringBuilder string_builder_cache;
    private String cache;
    private List<Tuple2<Integer, Integer>> row_indices;
    private boolean[] dirty_rows;

    public StringCache() {

      // Initialise attributes
      string_builder_cache = new StringBuilder();
      row_indices = new ArrayList<>(size);
      dirty_rows = new boolean[size];

      // Create a string representation of the grid
      for (int row = 0; row < size; row++) {

        if (row != 0) {
          string_builder_cache.append("\n\n");

          // Underneath every box, add a line of dashes
          int rows_per_box = box_dimensions.first();
          if (row % rows_per_box == 0) {

            // Calculate num characters per box; add 3 characters per seperator
            // Num seperators = num boxes per row - 1
            int num_cols_in_box = box_dimensions.second();
            int chars_per_box_row = (num_cols_in_box * 2) - 1;
            int num_boxes_per_row = size / box_dimensions.second();
            int num_columns = chars_per_box_row * num_boxes_per_row + (num_boxes_per_row - 1) * 3;

            for (int i = 0; i < num_columns; i++) {
              string_builder_cache.append("-");
            }

            string_builder_cache.append("\n\n");

          }
        }

        // Build each row & update row_indices values
        int row_start_index = string_builder_cache.length();
        string_builder_cache.append(formatRow(row));
        int row_end_index = string_builder_cache.length();
        row_indices.add(row, new Tuple2<>(row_start_index, row_end_index));

      }

      // Generate string representation
      this.cache = string_builder_cache.toString();
    }

    // Convenience method to build the given row of the string
    private String formatRow(int row) {
      StringBuilder row_output = new StringBuilder();

      for (int column = 0; column < size; column++) {
        if (column > 0) {
          row_output.append(' ');

          if (column % 3 == 0) {
            row_output.append("| ");
          }
        }
        int value = grid.getValue(row, column);
        if (value == -1) { // Represent empty cells with an X
          row_output.append('X'); 
        } else if (value > 9) { // Represent values above 9 using base-17
          value -= 10;
          String base_17 = "ABCDEFG";
          row_output.append(base_17.charAt(value));
        } else {
          row_output.append(value);
        }
      }

      return row_output.toString();
    }

    /**
     * Marks a given row as 'dirty', meaning one or more of its values have been changed.
     * <p> This method must be called every time a cell value is updated.
     * @param row The row to mark as 'dirty'
     */
    public void markRowDirty(int row) {
      if (dirty_rows == null) {
        throw new IllegalStateException("StringCache has not yet been initialised.");
      } else if (row < 0 || row > size - 1) {
        throw new IndexOutOfBoundsException("row provided is not a valid row within the sudoku"
            + " grid.");
      } else { // Mark row as dirty & reset cache
        dirty_rows[row] = true; 
        cache = null;
      }
    }
    
    public String getCache() {
      updateDirtyRowsInCache();
      if (cache == null) { // If cache has been updated, regenerate it
        cache = string_builder_cache.toString();
      }
      return cache;
    }

    /**
     * Convenience method to update cache to reflect any changes since the last update.
     * <p> This method must always be called before StringCache.cache is accessed.
     */
    private void updateDirtyRowsInCache() {
      if (dirty_rows == null) { // If no rows are dirty then there is nothing to update
        return;
      }

      for (int row = 0; row < dirty_rows.length; row++) {
        if (dirty_rows[row]) { // Rebuild all rows with dirty marker
          replaceRowInCache(row, formatRow(row));
          dirty_rows[row] = false;
        }
      }
    }

    // Convenience function to replace an outdated row in cache.
    private void replaceRowInCache(int row, String new_row_string) {
      int row_start = row_indices.get(row).first();
      int row_end = row_indices.get(row).second();

      // Update string_builder_cache. Note that lines always remain the same size.
      string_builder_cache.replace(row_start, row_end, new_row_string);
    }
  }

  /** Initialises an empty 9x9 sudoku grid. */
  public SudokuGrid() {
    this(DEFAULT_SIZE);
  }

  /**
   * Initialises an empty sudoku grid with the given size.
   * <p> A size of 9 means that each box in the sudoku grid contains the numbers 1-9.
   * <p> Sizes supported are all values {@code x}, where {@code 2 <= x <= 16}, and {@code x} is
   * either even, square, or both.
   * @param size The size of the sudoku grid to generate
   */
  public SudokuGrid(int size) {
    ISudokuGrid.validateGridSize(size);

    this.size = size;
    this.grid = new SudokuGridState(size);
    this.box_dimensions = SudokuBox.calculateBoxSize(size);
    this.string_cache = new StringCache();
  }

  @Override
  public boolean isValid() {
    return ISudokuSolver.isGridStateValid(grid);
  }

  @Override
  public boolean isSolved() {
    return (isValid() && !hasEmptyCells());
  }

  @Override
  public void resetGrid() {
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        grid.setValue(row, col, -1);
      }
      string_cache.markRowDirty(row);
    }
  }

  @Override
  public boolean checkForDuplicates(int row, int column) {
    return grid.checkForDuplicates(row, column);
  }  

  // Getters //

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public Tuple2<Integer, Integer> getGridDimensions() {
    return new Tuple2<>(size, size);
  }

  @Override
  public int getValue(int row, int column) {
    return grid.getValue(row, column);
  }

  // Setters //

  @Override
  public void setValue(int row, int column, int value) {
    if ((value < 1 || value > size) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and " + size
          + " inclusive.");
    } 
    grid.setValue(row, column, value);
    string_cache.markRowDirty(row);
  }

  // Object Overrides //

  @Override
  public String toString() {
    return string_cache.getCache();
  }
  
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof SudokuGrid)) {
      return false;
    }
    SudokuGrid casted_other = (SudokuGrid) other;
    return grid.equals(casted_other.grid);
  }

  @Override
  public int hashCode() {
    return grid.hashCode();
  }

  @Override
  public SudokuGrid clone() {
    SudokuGrid clone = new SudokuGrid(size);

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        clone.setValue(row, col, getValue(row, col));
      }
    }

    return clone;
  }

  // Convenience Methods //

  // Checks whether the grid has any empty cells
  private boolean hasEmptyCells() {
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (getValue(row, col) == -1) {
          return true;
        }
      }
    }
    return false;
  }

}