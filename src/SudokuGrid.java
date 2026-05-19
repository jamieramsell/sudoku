import java.util.HashSet;
import java.util.Set;

public class SudokuGrid implements ISudokuGrid {
  private int[][] grid;
  private int squareRows;
  private int squareColumns;

  public SudokuGrid(int squareRows, int squareColumns) {
    initialiseGrid(squareRows, squareColumns);
  }

  @Override
  public int[][] initialiseGrid(int rows, int columns) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Grid dimensions must be positive");
    }

    squareRows = rows;
    squareColumns = columns;

    grid = new int[rows * 3][columns * 3];
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        grid[row][column] = -1;
      }
    }

    return grid;
  }

  @Override
  public int getValue(int row, int column) {
    return grid[row][column];
  }

  @Override
  public void setValue(int row, int column) {
    throw new UnsupportedOperationException("Use setValue(row, column, value)");
  }

  public void setValue(int row, int column, int value) {
    grid[row][column] = value;
  }

  @Override
  public boolean isValid() {
    for (int row = 0; row < grid.length; row++) {
      if (hasDuplicates(grid[row])) {
        return false;
      }
    }

    int totalColumns = grid[0].length;
    for (int column = 0; column < totalColumns; column++) {
      int[] columnValues = new int[grid.length];
      for (int row = 0; row < grid.length; row++) {
        columnValues[row] = grid[row][column];
      }
      if (hasDuplicates(columnValues)) {
        return false;
      }
    }

    for (int squareRow = 0; squareRow < squareRows; squareRow++) {
      for (int squareColumn = 0; squareColumn < squareColumns; squareColumn++) {
        int[] squareValues = new int[9];
        int index = 0;
        int rowOffset = squareRow * 3;
        int columnOffset = squareColumn * 3;

        for (int row = 0; row < 3; row++) {
          for (int column = 0; column < 3; column++) {
            squareValues[index++] = grid[rowOffset + row][columnOffset + column];
          }
        }

        if (hasDuplicates(squareValues)) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public boolean isSolved() {
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        if (grid[row][column] == -1) {
          return false;
        }
      }
    }

    return isValid();
  }

  @Override
  public int[][] resetGrid() {
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        grid[row][column] = -1;
      }
    }

    return grid;
  }

  @Override
  public void displayGrid() {
    System.out.println(toString());
  }

  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    String separator = "-".repeat(formatRow(0).length());

    for (int row = 0; row < grid.length; row++) {
      if (row > 0) {
        output.append('\n');
      }

      if (row > 0 && row % 3 == 0) {
        output.append(separator).append('\n');
      }

      output.append(formatRow(row));
    }

    return output.toString();
  }

  private String formatRow(int row) {
    StringBuilder rowOutput = new StringBuilder();

    for (int column = 0; column < grid[row].length; column++) {
      if (column > 0) {
        rowOutput.append(' ');
      }

      if (column > 0 && column % 3 == 0) {
        rowOutput.append("| ");
      }

      rowOutput.append(grid[row][column]);
    }

    return rowOutput.toString();
  }

  private static boolean hasDuplicates(int[] values) {
    Set<Integer> seen = new HashSet<>();
    for (int value : values) {
      if (value == -1) {
        continue;
      }

      if (!seen.add(value)) {
        return true;
      }
    }

    return false;
  }
}
