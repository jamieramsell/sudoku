package sudoku;

import java.util.Set;
import java.util.HashSet;

public class SudokuSolver implements ISudokuSolver {

  private final ISudokuGrid grid;

  public SudokuSolver(ISudokuGrid grid) {
    this.grid = grid;
  }

  public boolean isSolvable() {
    return countSolutions() > 0;
  }

  public boolean hasUniqueSolution() {
    return countSolutions() == 1;
  }

  public int countSolutions() {
    return solveGrid().size();
  }

  public Set<int[][]> solveGrid() {
    int[][] workingGrid = grid.getGrid();
    Set<int[][]> solutions = new HashSet<>();

    if (!grid.isValid() || !isGridStateValid(workingGrid)) {
      return solutions;
    }

    solve(workingGrid, solutions);
    return solutions;
  }

  // NEEDS MOVING TO SudokuGrid: //

  // private int[][] getGrid() {
  //   Tuple2<Integer, Integer> grid_size = grid.getGridSize();
  //   int rowCount = grid_size.first() * 3;
  //   int columnCount = grid_size.second() * 3;
  //   int[][] copiedGrid = new int[rowCount][columnCount];

  //   for (int row = 0; row < rowCount; row++) {
  //     for (int column = 0; column < columnCount; column++) {
  //       copiedGrid[row][column] = grid.getValue(row, column);
  //     }
  //   }

  //   return copiedGrid;
  // }

  public boolean isValidMove(int row, int column, int value) {
    
    // Input Validation //

    if (value == -1) { // Always allow cells to be emptied
      return true;
    } else if (value < 1 || value > 9) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 inclusive.");
    }

    Tuple2<Integer, Integer> grid_size = grid.getGridSize();
    if (row < 0 || row > grid_size.first() * 3 || column < 0 || column > grid_size.second() * 3) {
      throw new IndexOutOfBoundsException("row or column out of bounds");
    }

    // Check for duplicate values in the square
    return isPlacementValid(grid.getGrid(), row, column, value);

  }

  // General purpose method to check whether the placement of a value in a given grid is valid
  private static boolean isPlacementValid(int[][] grid_to_check, int row, int column, int value) {

    // Can use a single for loop here as a grid is always square
    for (int index = 0; index < grid_to_check.length; index++) {
      if (grid_to_check[row][index] == value || grid_to_check[index][column] == value) {
        return false;
      }
    }

    // Check if each cell in the square is a duplicate
    int top_left_row = row - row % 3;
    int top_left_col = column - column % 3;

    for (int current_row = top_left_row; current_row < top_left_row + 3; current_row++) {
      for (int current_col = top_left_col; current_col < top_left_col + 3; current_col++) {

        if (value == grid_to_check[current_row][current_col]) {
          return false;
        }

      }
    }

    return true;
  }
  
  private static void solve(int[][] workingGrid, Set<int[][]> solutions) {
    int[] emptyCell = findNextEmptyCell(workingGrid);
    if (emptyCell == null) {
      solutions.add(copyGrid(workingGrid));
      return;
    }

    int row = emptyCell[0];
    int column = emptyCell[1];
    for (int candidate = 1; candidate <= workingGrid.length; candidate++) {
      if (isPlacementValid(workingGrid, row, column, candidate)) {
        workingGrid[row][column] = candidate;
        solve(workingGrid, solutions);
        workingGrid[row][column] = -1;
      }
    }
  }

  private static int[] findNextEmptyCell(int[][] grid_to_check) {

    for (int row = 0; row < grid_to_check.length; row++) {
      for (int column = 0; column < grid_to_check[row].length; column++) {

        if (grid_to_check[row][column] == -1) {
          return new int[] {row, column};
        }

      }
    }

    return null;
  }

  public static boolean isGridStateValid(int[][] grid_to_check) {

    for (int row = 0; row < grid_to_check.length; row++) {
      for (int column = 0; column < grid_to_check[row].length; column++) {

        int value = grid_to_check[row][column];

        if (value == -1) { // Cells are always allowed to be empty
          continue;
        } else if (value < 1 || value > 9) { // Check for out of bounds values
          return false;
        }

        // Check that the value of the current cell is legal
        grid_to_check[row][column] = -1;
        boolean valid = isPlacementValid(grid_to_check, row, column, value);
        grid_to_check[row][column] = value;

        if (!valid) {
          return false;
        }

      }
    }

    return true;
  }

  private static int[][] copyGrid(int[][] sourceGrid) {
    int[][] gridCopy = new int[sourceGrid.length][];
    for (int row = 0; row < sourceGrid.length; row++) {
      gridCopy[row] = new int[sourceGrid[row].length];
      System.arraycopy(sourceGrid[row], 0, gridCopy[row], 0, sourceGrid[row].length);
    }
    return gridCopy;
  }
}
