package sudoku;

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

    private StringBuilder stringBuilderCache;
    private String cache;
    private int[] rowStartIndices;
    private int[] rowEndIndices;
    private boolean[] dirtyRows;

    public StringCache() {

      // Initialise attributes
      int rows = size * size; // Number of rows in sudoku grid
      stringBuilderCache = new StringBuilder();
      rowStartIndices = new int[rows];
      rowEndIndices = new int[rows];
      dirtyRows = new boolean[rows];

      // Create a string representation of the grid
      for (int row = 0; row < rows; row++) {

        if (row != 0) {
          stringBuilderCache.append("\n\n");

          // In every row n, where n is a multiple of 3:
          if (row % 3 == 0) {

            // 5 characters per square; 3 characters per seperator
            // Num seperators = num squares - 1
            int num_columns = (size * 5) + ((size - 1) * 3);

            for (int i = 0; i < num_columns; i++) {
              stringBuilderCache.append("-");
            }

            stringBuilderCache.append("\n\n");

          }
        }

        // Build each row of values
        rowStartIndices[row] = stringBuilderCache.length();
        stringBuilderCache.append(formatRow(row));
        rowEndIndices[row] = stringBuilderCache.length();

      }

      // Generate string representation
      this.cache = stringBuilderCache.toString();
    }

    // Convenience method to build the given row of the string
    private String formatRow(int row) {
      StringBuilder rowOutput = new StringBuilder();

      for (int column = 0; column < (size * size); column++) {
        if (column > 0) {
          rowOutput.append(' ');

          if (column % 3 == 0) {
            rowOutput.append("| ");
          }
        }
        int value = grid.getValue(row, column);
        if (value == -1) {
          rowOutput.append('X'); // Represent empty cells with an X
        } else {
          rowOutput.append(value);
        }
      }

      return rowOutput.toString();
    }

    /**
     * Marks a given row as 'dirty', meaning one or more of its values have been changed.
     * <p> This method must be called every time a cell value is updated.
     * @param row The row to mark as 'dirty'
     */
    public void markRowDirty(int row) {
      if (dirtyRows == null) {
        throw new IllegalStateException("StringCache has not yet been initialised.");
      } else if (row < 0 || row > size * size - 1) {
        throw new IndexOutOfBoundsException("row provided is not a valid row within the sudoku"
            + " grid.");
      } else { // Mark row as dirty & reset cache
        dirtyRows[row] = true; 
        cache = null;
      }
    }
    
    public String getCache() {
      updateDirtyRowsInCache();
      if (cache == null) { // If cache has been updated, regenerate it
        cache = stringBuilderCache.toString();
      }
      return cache;
    }

    /**
     * Convenience method to update cache to reflect any changes since the last update.
     * <p> This method must always be called before StringCache.cache is accessed.
     */
    private void updateDirtyRowsInCache() {
      if (dirtyRows == null) { // If no rows are dirty then there is nothing to update
        return;
      }

      for (int row = 0; row < dirtyRows.length; row++) {
        if (dirtyRows[row]) { // Rebuild all rows with dirty marker
          replaceRowInCache(row, formatRow(row));
          dirtyRows[row] = false;
        }
      }
    }

    // Convenience function to replace an outdated row in cache.
    private void replaceRowInCache(int row, String newRowString) {
      int rowStart = rowStartIndices[row];
      int rowEnd = rowEndIndices[row];
      int originalLength = rowEnd - rowStart;
      int replacementLength = newRowString.length();
      int delta = replacementLength - originalLength;

      // Update stringBuilderCache
      stringBuilderCache.replace(rowStart, rowEnd, newRowString);
      rowEndIndices[row] = rowStart + replacementLength;

      // Update pointers if necessary
      if (delta != 0) {
        for (int remainingRow = row + 1; remainingRow < rowStartIndices.length; remainingRow++) {
          rowStartIndices[remainingRow] += delta;
          rowEndIndices[remainingRow] += delta;
        }
      }
    }
  }

  public SudokuGrid() {
    initialiseAttributes(DEFAULT_SIZE);
  }

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