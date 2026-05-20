package sudoku;

public class SudokuGrid implements ISudokuGrid {

  final int DEFAULT_SIZE = 3;

  int size;
  int[][] grid;
  int[][] solution;

  public SudokuGrid() {
    initialiseAttributes(DEFAULT_SIZE);
  }

  public SudokuGrid(int size) {
    initialiseAttributes(size);
  }

  /* To do - finish constructor
   * create a random puzzle
   * store its solution
   */

  // Convenience method to allow for two constructors with very similar functionality
  private void initialiseAttributes(int size) {
    this.size = size;
    this.grid = initialiseGrid(size, size);
    this.solution = initialiseGrid(size, size);
  }

  @Override
  public int[][] initialiseGrid(int rows, int columns) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Grid dimensions must be positive");
    } else if (rows > 3 || columns > 3) {
      throw new IllegalArgumentException("Grids of more than 3 sudoku squares in either direction " +
          "are not supported");
    }

    int[][] new_grid = new int[rows * 3][columns * 3];
    for (int row = 0; row < new_grid.length; row++) {
      for (int column = 0; column < new_grid[row].length; column++) {
        new_grid[row][column] = -1;
      }
    }
    
    grid = new_grid.clone();
    return new_grid;
  }

  @Override
  public int getValue(int row, int column) {
    return grid[row][column];
  }

  @Override
  public void setValue(int row, int column, int value) {
    if ((value < 1 || value > 9) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9" +
          "inclusive.");
    } else {
      grid[row][column] = value;
    }
  }

  // To do
  @Override
  public boolean isValid() {
    return false;
  }

  @Override
  public boolean isSolved() {
    return (isValid() && !hasEmptyCells());
  }

  // Convenience function for isSolved() method to check whether the grid has any empty cells
  private boolean hasEmptyCells() {
    for (int[] row : grid) {
      for (int cell : row) {
        if (cell == -1) {
          return true;
        }
      }
    }
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
    StringBuilder output = new StringBuilder();
    String separator = "-".repeat(formatRow(0).length());

    for (int row = 0; row < grid.length; row++) {
      if (row > 0) {
        output.append('\n');
      
        if (row % 3 == 0) {
          output.append(separator).append('\n');
        }
      }

      output.append(formatRow(row));
    }

    return output.toString();
  }
  
  // Convenience method to format each row of toString() method
  private String formatRow(int row) {
    StringBuilder rowOutput = new StringBuilder();

    for (int column = 0; column < grid[row].length; column++) {
      if (column > 0) {
        rowOutput.append(' ');

        if (column % 3 == 0) {
          rowOutput.append("| ");
        }
      }

      rowOutput.append(grid[row][column]);
    }

    return rowOutput.toString();
  }

}
