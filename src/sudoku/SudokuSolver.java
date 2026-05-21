package sudoku;

import java.util.Set;
import java.util.HashSet;

public class SudokuSolver implements ISudokuSolver {

  private final ISudokuGrid grid;

  public SudokuSolver(ISudokuGrid grid) {
    this.grid = grid;
  }

  @Override
  public boolean isSolvable() {
    return countSolutions() > 0;
  }

  @Override
  public boolean hasUniqueSolution() {
    return countSolutions() == 1;
  }

  @Override
  public int countSolutions() {
    return solveGrid().size();
  }

  @Override
  public Set<int[][]> solveGrid() {
    int[][] workingGrid = grid.getGrid();
    Set<int[][]> solutions = new HashSet<>();

    if (!grid.isValid() || !ISudokuSolver.isGridStateValid(workingGrid)) {
      return solutions;
    }

    solve(workingGrid, solutions);
    return solutions;
  }

  @Override
  public boolean isValidMove(int row, int column, int value) {
    
    // Input Validation //

    if (value == -1) { // Always allow cells to be emptied
      return true;
    } else if (value < 1 || value > 9) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 inclusive.");
    }

    Tuple2<Integer, Integer> grid_size = grid.getGridSize();
    if (row < 0 || row >= grid_size.first() * 3 || column < 0 || column >= grid_size.second() * 3) {
      throw new IndexOutOfBoundsException("row or column out of bounds");
    }

    return ISudokuSolver.isPlacementValid(grid.getGrid(), row, column, value);

  }
  
  // Convenience method to contain the exhaustive search which finds all solutions to a puzzle inclusive
  // a given state.
  private static void solve(int[][] workingGrid, Set<int[][]> solutions) {

    Tuple2<Integer, Integer> emptyCell = findNextEmptyCell(workingGrid);
    if (emptyCell == null) {
      solutions.add(ISudokuGrid.copyGrid(workingGrid));
      return;
    }

    int row = emptyCell.first();
    int column = emptyCell.second();

    for (int candidate = 1; candidate <= 9; candidate++) {
      if (ISudokuSolver.isPlacementValid(workingGrid, row, column, candidate)) {
        workingGrid[row][column] = candidate;
        solve(workingGrid, solutions);
        workingGrid[row][column] = -1;
      }
    }

  }

  // Convenience method to find & return the next empty cell
  private static Tuple2<Integer, Integer> findNextEmptyCell(int[][] grid_to_check) {

    for (int row = 0; row < grid_to_check.length; row++) {
      for (int column = 0; column < grid_to_check[row].length; column++) {

        if (grid_to_check[row][column] == -1) {
          return new Tuple2<>(row, column);
        }

      }
    }
    return null;
  }

}
