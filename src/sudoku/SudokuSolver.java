package sudoku;

import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {
  private int[][] grid;

  public SudokuSolver() {
    this.grid = new int[0][0];
  }

  public SudokuSolver(int[][] grid) {
    setGrid(grid);
  }

  public void setGrid(int[][] grid) {
    this.grid = copyGrid(grid);
  }

  public int[][] getGrid() {
    return copyGrid(this.grid);
  }

  public List<int[][]> solveGrid() {
    int[][] workingGrid = copyGrid(this.grid);
    List<int[][]> solutions = new ArrayList<>();
    if (!isGridShapeValid(workingGrid) || !isGridStateValid(workingGrid)) {
      return solutions;
    }
    solve(workingGrid, solutions);
    return solutions;
  }

  public static List<int[][]> solveGrid(int[][] grid) {
    return new SudokuSolver(grid).solveGrid();
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
        workingGrid[row][column] = 0;
      }
    }
  }

  private int[] findNextEmptyCell(int[][] grid) {
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        if (isEmptyCell(grid[row][column])) {
          return new int[] { row, column };
        }
      }
    }
    return null;
  }

  private boolean isPlacementValid(int[][] grid, int row, int column, int candidate) {
    for (int i = 0; i < grid.length; i++) {
      if (grid[row][i] == candidate || grid[i][column] == candidate) {
        return false;
      }
    }

    int boxSize = (int) Math.sqrt(grid.length);
    int boxStartRow = row - (row % boxSize);
    int boxStartColumn = column - (column % boxSize);
    for (int rowOffset = 0; rowOffset < boxSize; rowOffset++) {
      for (int columnOffset = 0; columnOffset < boxSize; columnOffset++) {
        if (grid[boxStartRow + rowOffset][boxStartColumn + columnOffset] == candidate) {
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
        candidateGrid[row][column] = 0;
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

  private int[][] copyGrid(int[][] sourceGrid) {
    if (sourceGrid == null) {
      return new int[0][0];
    }
    int[][] gridCopy = new int[sourceGrid.length][];
    for (int row = 0; row < sourceGrid.length; row++) {
      if (sourceGrid[row] == null) {
        gridCopy[row] = null;
        continue;
      }
      gridCopy[row] = new int[sourceGrid[row].length];
      System.arraycopy(sourceGrid[row], 0, gridCopy[row], 0, sourceGrid[row].length);
    }
    return gridCopy;
  }
}
