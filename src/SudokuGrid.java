public class SudokuGrid implements ISudokuGrid {

  int size;
  int[][] grid;
  int[][] solution;
  StringBuilder gridStringBuilderCache;
  String gridStringCache;
  int[] rowStartIndices;
  int[] rowEndIndices;
  boolean[] dirtyRows;
  boolean stringCacheInitialised;

  /* To do - finish constructor
   * create a random puzzle
   * store its solution
   */
  public SudokuGrid(int size) {
    this.size = size;
    this.grid = initialiseGrid(size, size);
    this.solution = initialiseGrid(size, size);
    this.stringCacheInitialised = false;
  }

  @Override
  public int[][] initialiseGrid(int rows, int columns) {

    int[][] grid = new int[rows * 3][columns * 3];
    // Initialise all squares as empty
    for (int row = 0; row < rows * 3; row++) {
      for (int col = 0; col < columns * 3; col++) {
        grid[row][col] = -1;
      }
    }
    return grid;

  }

  @Override
  public int getValue(int row, int column) {
    return grid[row][column];
  }

  @Override
  public void setValue(int row, int column, int value) {
    if (value < 1 || value > 9) {
      throw new IllegalArgumentException("value must be >0 and <10.");
    } else {
      if (grid[row][column] != value) {
        grid[row][column] = value;
        markRowAsDirty(row);
      }
    }
  }

  // To do
  @Override
  public boolean isValid() {
    return false;
  }

  // To do
  @Override
  public boolean isSolved() {
    return false;
  }

  @Override
  public int[][] resetGrid() {

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        grid[row][col] = -1;
      }
      markRowAsDirty(row);
    }

    return grid.clone();

  }

  // To do - update this to use a GUI in a later version
  @Override
  public void displayGrid() {
    System.out.println(toString());
  }

  @Override
  public String toString() {
    ensureStringCacheInitialised();
    updateDirtyRowsInCache();
    if (gridStringCache == null) {
      gridStringCache = gridStringBuilderCache.toString();
    }
    return gridStringCache;

  }

  private void ensureStringCacheInitialised() {
    if (stringCacheInitialised) {
      return;
    }

    int gridRows = size * 3;
    this.gridStringBuilderCache = new StringBuilder();
    this.rowStartIndices = new int[gridRows];
    this.rowEndIndices = new int[gridRows];
    this.dirtyRows = new boolean[gridRows];

    for (int row = 0; row < gridRows; row++) {

      if (row != 0) {
        gridStringBuilderCache.append("\n\n");
        if (row / 3 == 0) {

          // 5 characters per square; 3 characters per seperator
          // Num seperators = num squares - 1
          int num_columns = (size * 5) + ((size - 1) * 3);

          for (int i = 0; i < num_columns; i++) {
            gridStringBuilderCache.append("-");
          }

          gridStringBuilderCache.append("\n\n");

        }
      }

      rowStartIndices[row] = gridStringBuilderCache.length();
      gridStringBuilderCache.append(buildRowString(row));
      rowEndIndices[row] = gridStringBuilderCache.length();

    }

    this.gridStringCache = gridStringBuilderCache.toString();
    this.stringCacheInitialised = true;
  }

  private String buildRowString(int row) {
    StringBuilder rowString = new StringBuilder();
    for (int col = 0; col < size * 3; col++) {

      if (col != 0) {
        rowString.append(" ");
        if (col / 3 == 0) {
          rowString.append("| ");
        }
      }

      rowString.append(grid[row][col]);
    }
    return rowString.toString();
  }

  private void markRowAsDirty(int row) {
    if (dirtyRows != null && row >= 0 && row < dirtyRows.length) {
      dirtyRows[row] = true;
      gridStringCache = null;
    }
  }

  private void updateDirtyRowsInCache() {
    if (dirtyRows == null) {
      return;
    }

    for (int row = 0; row < dirtyRows.length; row++) {
      if (dirtyRows[row]) {
        replaceRowInCache(row, buildRowString(row));
        dirtyRows[row] = false;
      }
    }
  }

  private void replaceRowInCache(int row, String newRowString) {
    int rowStart = rowStartIndices[row];
    int rowEnd = rowEndIndices[row];
    int originalLength = rowEnd - rowStart;
    int replacementLength = newRowString.length();
    int delta = replacementLength - originalLength;

    gridStringBuilderCache.replace(rowStart, rowEnd, newRowString);
    rowEndIndices[row] = rowStart + replacementLength;

    if (delta != 0) {
      for (int remainingRow = row + 1; remainingRow < rowStartIndices.length; remainingRow++) {
        rowStartIndices[remainingRow] += delta;
        rowEndIndices[remainingRow] += delta;
      }
    }
  }

}
