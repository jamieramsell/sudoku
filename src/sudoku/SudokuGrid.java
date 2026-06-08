package sudoku;

import java.util.List;
import java.util.ArrayList;

public class SudokuGrid implements ISudokuGrid {

  // Constants
  final int DEFAULT_SIZE = 3;

  // Attributes
  int size;
  GridState grid;
  StringCache string_cache;

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
      int rows = size * size; // Number of rows in sudoku grid
      string_builder_cache = new StringBuilder();
      row_indices = new ArrayList<>(rows);
      dirty_rows = new boolean[rows];

      // Create a string representation of the grid
      for (int row = 0; row < rows; row++) {

        if (row != 0) {
          string_builder_cache.append("\n\n");

          // In every row n, where n is a multiple of 3:
          if (row % 3 == 0) {

            // 5 characters per square; 3 characters per seperator
            // Num seperators = num squares - 1
            int num_columns = (size * 5) + ((size - 1) * 3);

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
        row_indices.set(row, new Tuple2<>(row_start_index, row_end_index));

      }

      // Generate string representation
      this.cache = string_builder_cache.toString();
    }

    // Convenience method to build the given row of the string
    private String formatRow(int row) {
      StringBuilder row_output = new StringBuilder();

      for (int column = 0; column < (size * size); column++) {
        if (column > 0) {
          row_output.append(' ');

          if (column % 3 == 0) {
            row_output.append("| ");
          }
        }
        int value = grid.getValue(row, column);
        if (value == -1) {
          row_output.append('X'); // Represent empty cells with an X
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
      } else if (row < 0 || row > size * size - 1) {
        throw new IndexOutOfBoundsException("row provided is not a valid row within the sudoku"
            + " grid.");
      } else { // Mark row as dirty & reset cache
        dirty_rows[row] = true; 
        cache = null;
      }
    }
    
    public String getCache() {
      updatedirty_rowsInCache();
      if (cache == null) { // If cache has been updated, regenerate it
        cache = string_builder_cache.toString();
      }
      return cache;
    }

    /**
     * Convenience method to update cache to reflect any changes since the last update.
     * <p> This method must always be called before StringCache.cache is accessed.
     */
    private void updatedirty_rowsInCache() {
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
    private void replaceRowInCache(int row, String newRowString) {
      int row_start = row_indices.get(row).first();
      int row_end = row_indices.get(row).second();

      // Update string_builder_cache
      string_builder_cache.replace(row_start, row_end, newRowString);
    }
  }

  /** Initialises an empty 9x9 sudoku grid. */
  public SudokuGrid() {
    initialiseAttributes(DEFAULT_SIZE);
  }

  /**
   * Initialises an empty sudoku grid with the given size.
   * <p> A size of 9 means that each square in the sudoku grid contains the numbers 1-9.
   * <p> Sizes supported are all values {@code x}, where {@code 2 <= x <= 16}, and {@code x} is
   * either even, square, or both.
   * @param size The size of the sudoku grid to generate
   */
  public SudokuGrid(int size) {
    throw new UnsupportedOperationException();
  }

  /* To do - finish constructor
   * create a random puzzle
   * store its solution
   */

  // Convenience method to allow for two constructors with very similar functionality
  private void initialiseAttributes(int size) {
    this.size = size;
    this.grid = new GridState();
    this.string_cache = new StringCache();
  }

  @Override
  public int getValue(int row, int column) {
    return grid.getValue(row, column);
  }

  @Override
  public void setValue(int row, int column, int value) {
    if ((value < 1 || value > 9) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 " +
          "inclusive.");
    } else {
      grid.setValue(row, column, value);
      string_cache.markRowDirty(row);
    }
  }

  @Override
  public boolean isValid() {
    return ISudokuSolver.isGridStateValid(getGrid());
  }

  @Override
  public boolean isSolved() {
    return (isValid() && !hasEmptyCells());
  }

  // Convenience function for isSolved() method to check whether the grid has any empty cells
  private boolean hasEmptyCells() {
    for (int row = 0; row < (size * size); row++) {
      for (int col = 0; col < (size * size); col++) {
        if (getValue(row, col) == -1) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public GridState resetGrid() {

    for (int row = 0; row < size * 3; row++) {
      for (int col = 0; col < size * 3; col++) {
        grid.setValue(row, col, -1);
      }
      string_cache.markRowDirty(row);
    }

    return getGrid();

  }

  // To do - update this to use a GUI in a later version
  @Override
  public void displayGrid() {
    System.out.println(toString());
  }

  @Override
  public String toString() {
    return string_cache.getCache();
  }
  
  @Override
  public int[] getGridSize() {
    return new int[]{size, size};
  }

  @Override
  public GridState getGrid() {
    return grid.clone();
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof SudokuGrid)) {
      return false;
    }
    SudokuGrid casted_other = (SudokuGrid) other;
    return grid.equals(casted_other.getGrid());
  }

}