package sudoku;

import java.util.ArrayList;
import java.util.List;

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

  public List<int[][]> solveGrid() {
    return findSolutions();
  }

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

    // Check for duplicate values in the row
    for (int current_col = 0; current_col < grid_size.second(); current_col++) {
      if (grid.getValue(row, current_col) == value) {
        return false;
      }
    }

    // Check for duplicates in the column
    for (int current_row = 0; current_row < grid_size.first(); current_row++) {
      if (grid.getValue(current_row, column) == value) {
        return false;
      }
    }

    // Check for duplicate values in the square
    return duplicatesInSquare(row, column, value);

  }

  // Convenience method to check for duplicate values in the sudoku square of the given cell
  private boolean duplicatesInSquare(int row, int column, int value) {
    
    // Find coordinates of top-left cell in the square
    int top_left_row = row / 3;
    int top_left_col = column / 3;

    // Check whether each cell in the square is a duplicate
    for (int current_row = top_left_row; current_row < top_left_row + 2; current_row++) {
      for (int current_col = top_left_col; current_col < top_left_col + 2; current_col++) {

        if (value == grid.getValue(current_row, current_col)) {
          return false;
        }

      }
    }

    return true;

  }

  private List<int[][]> findSolutions() {
    int[][] workingGrid = readGrid();
    List<int[][]> solutions = new ArrayList<>();

    if (!grid.isValid() || !isGridShapeValid(workingGrid) || !isGridStateValid(workingGrid)) {
      return solutions;
    }

    solve(workingGrid, solutions);
    return solutions;
  }

  private void solve(int[][] workingGrid, List<int[][]> solutions) {
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

  private int[] findNextEmptyCell(int[][] candidateGrid) {
    for (int row = 0; row < candidateGrid.length; row++) {
      for (int column = 0; column < candidateGrid[row].length; column++) {
        if (isEmptyCell(candidateGrid[row][column])) {
          return new int[] {row, column};
        }
      }
    }
    return null;
  }

  private boolean isPlacementValid(int[][] candidateGrid, int row, int column, int value) {
    for (int index = 0; index < candidateGrid.length; index++) {
      if (candidateGrid[row][index] == value || candidateGrid[index][column] == value) {
        return false;
      }
    }

    int boxSize = (int) Math.sqrt(candidateGrid.length);
    int boxStartRow = row - (row % boxSize);
    int boxStartColumn = column - (column % boxSize);
    for (int rowOffset = 0; rowOffset < boxSize; rowOffset++) {
      for (int columnOffset = 0; columnOffset < boxSize; columnOffset++) {
        if (candidateGrid[boxStartRow + rowOffset][boxStartColumn + columnOffset] == value) {
          return false;
        }
      }
    }

    return true;
  }

  private boolean isGridShapeValid(int[][] candidateGrid) {
    if (candidateGrid == null || candidateGrid.length == 0) {
      return false;
    }

    int size = candidateGrid.length;
    int boxSize = (int) Math.sqrt(size);
    if (boxSize * boxSize != size) {
      return false;
    }

    for (int[] row : candidateGrid) {
      if (row == null || row.length != size) {
        return false;
      }
    }

    return true;
  }

  private boolean isGridStateValid(int[][] candidateGrid) {
    for (int row = 0; row < candidateGrid.length; row++) {
      for (int column = 0; column < candidateGrid[row].length; column++) {
        int value = candidateGrid[row][column];
        if (isEmptyCell(value)) {
          continue;
        }
        if (value < 1 || value > candidateGrid.length) {
          return false;
        }
        candidateGrid[row][column] = -1;
        boolean valid = isPlacementValid(candidateGrid, row, column, value);
        candidateGrid[row][column] = value;
        if (!valid) {
          return false;
        }
      }
    }

    return true;
  }

  private boolean isEmptyCell(int value) {
    return value <= 0;
  }

  private int[][] readGrid() {
    Tuple2<Integer, Integer> grid_size = grid.getGridSize();
    int rowCount = grid_size.first() * 3;
    int columnCount = grid_size.second() * 3;
    int[][] copiedGrid = new int[rowCount][columnCount];

    for (int row = 0; row < rowCount; row++) {
      for (int column = 0; column < columnCount; column++) {
        copiedGrid[row][column] = grid.getValue(row, column);
      }
    }

    return copiedGrid;
  }

  private int[][] copyGrid(int[][] sourceGrid) {
    int[][] gridCopy = new int[sourceGrid.length][];
    for (int row = 0; row < sourceGrid.length; row++) {
      gridCopy[row] = new int[sourceGrid[row].length];
      System.arraycopy(sourceGrid[row], 0, gridCopy[row], 0, sourceGrid[row].length);
    }
    return gridCopy;
  }
}
