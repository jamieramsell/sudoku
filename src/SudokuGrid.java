public class SudokuGrid implements ISudokuGrid {

  int size;
  int[][] grid;
  int[][] solution;

  /* To do - finish constructor
   * create a random puzzle
   * store its solution
   */
  public SudokuGrid(int size) {
    this.size = size;
    this.grid = initialiseGrid(size, size);
    this.solution = initialiseGrid(size, size);
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
      grid[row][column] = value;
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
    }

    return grid.clone();

  }

  // To do - update this to use a GUI in a later version
  @Override
  public void displayGrid() {
    System.out.println(toString());
  }

  // To do - optimise toString() to use cache rather than generate a new string every time.
  @Override
  public String toString() {

    StringBuffer string_to_return = new StringBuffer();

    // Generate each row as a line, with an empty line between rows, and a line of dashes between
    // squares.

    for (int row = 0; row < size * 3; row++) {

      if (row != 0) {
        string_to_return.append("\n\n");
        if (row / 3 == 0) {

          // 5 characters per square; 3 characters per seperator
          // Num seperators = num squares - 1
          int num_columns = (size * 5) + ((size - 1) * 3);

          for (int i = 0; i < num_columns; i++) {
            string_to_return.append("-");
          }

          string_to_return.append("\n\n");

        }
      }

      // Generate each column with a space in between, with a | between squares.

      for (int col = 0; col < size * 3; col++) {

        if (col != 0) {
          string_to_return.append(" ");
          if (col / 3 == 0) {
            string_to_return.append("| ");
          }
        }

        string_to_return.append(grid[row][col]);

      }
    }

    return string_to_return.toString();

  }

}
