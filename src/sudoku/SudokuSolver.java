package sudoku;

public class SudokuSolver implements ISudokuSolver {

  @Override
  public int[][] solve(int[][] puzzle) {
    validatePuzzleShape(puzzle);

    int[][] workingGrid = copyGrid(puzzle);
    validateInitialState(workingGrid);

    if (!solveFrom(workingGrid, 0, 0)) {
      throw new IllegalArgumentException("Puzzle is unsolvable.");
    }

    return workingGrid;
  }

  private void validatePuzzleShape(int[][] puzzle) {
    if (puzzle == null || puzzle.length != 9) {
      throw new IllegalArgumentException("Puzzle must have 9 rows.");
    }

    for (int row = 0; row < 9; row++) {
      if (puzzle[row] == null || puzzle[row].length != 9) {
        throw new IllegalArgumentException("Puzzle must have 9 columns per row.");
      }
      for (int column = 0; column < 9; column++) {
        int value = puzzle[row][column];
        if (value < 0 || value > 9) {
          throw new IllegalArgumentException("Puzzle values must be in range 0-9.");
        }
      }
    }
  }

  private void validateInitialState(int[][] puzzle) {
    for (int row = 0; row < 9; row++) {
      for (int column = 0; column < 9; column++) {
        int value = puzzle[row][column];
        if (value != 0) {
          puzzle[row][column] = 0;
          if (!isValidPlacement(puzzle, row, column, value)) {
            throw new IllegalArgumentException("Puzzle contains contradictory givens.");
          }
          puzzle[row][column] = value;
        }
      }
    }
  }

  private boolean solveFrom(int[][] grid, int row, int column) {
    if (row == 9) {
      return true;
    }

    int nextRow = column == 8 ? row + 1 : row;
    int nextColumn = column == 8 ? 0 : column + 1;

    if (grid[row][column] != 0) {
      return solveFrom(grid, nextRow, nextColumn);
    }

    for (int candidate = 1; candidate <= 9; candidate++) {
      if (isValidPlacement(grid, row, column, candidate)) {
        grid[row][column] = candidate;
        if (solveFrom(grid, nextRow, nextColumn)) {
          return true;
        }
      }
    }

    grid[row][column] = 0;
    return false;
  }

  private boolean isValidPlacement(int[][] grid, int row, int column, int value) {
    for (int i = 0; i < 9; i++) {
      if (grid[row][i] == value || grid[i][column] == value) {
        return false;
      }
    }

    int boxRowStart = (row / 3) * 3;
    int boxColumnStart = (column / 3) * 3;

    for (int r = boxRowStart; r < boxRowStart + 3; r++) {
      for (int c = boxColumnStart; c < boxColumnStart + 3; c++) {
        if (grid[r][c] == value) {
          return false;
        }
      }
    }

    return true;
  }

  private int[][] copyGrid(int[][] source) {
    int[][] copy = new int[9][9];
    for (int row = 0; row < 9; row++) {
      for (int column = 0; column < 9; column++) {
        copy[row][column] = source[row][column];
      }
    }
    return copy;
  }
}
