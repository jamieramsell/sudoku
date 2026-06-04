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
    return solveGrid(1).size() == 1;
  }

  @Override
  public boolean hasUniqueSolution() {
    return solveGrid(2).size() == 1;
  }

  @Override
  public int countSolutions() {
    return solveGrid().size();
  }

  @Override
  public Set<IGridState> solveGrid() {
    return solveGrid(-1);
  }

  @Override
  public Set<IGridState> solveGrid(int solutions_required) {

    if (solutions_required == 0 || solutions_required < -1) {
      throw new IllegalArgumentException("solutions_required must be >= 1, or set to -1 to " +
      "generate all possible solutions.");
    }

    IGridState workingGrid = grid.getGrid();
    Set<IGridState> solutions = new HashSet<>();

    if (!grid.isValid() || !ISudokuSolver.isGridStateValid(workingGrid)) {
      return solutions;
    }

    solve(workingGrid, solutions, solutions_required);
    return solutions;
  }

  // Convenience method to contain the exhaustive search which finds all solutions to a puzzle in
  // a given state.
  private static void solve(IGridState workingGrid, Set<IGridState> solutions,
      int solutions_required) {

    if (solutions.size() == solutions_required) {
      return;
    }

    Tuple2<Integer, Integer> emptyCell = findNextEmptyCell(workingGrid);
    if (emptyCell == null) {
      solutions.add(workingGrid.clone());
      return;
    }

    int row = emptyCell.first();
    int column = emptyCell.second();
    if (row == -1 || column == -1) {
      return;
    }

    for (int candidate = 1; candidate <= 9; candidate++) {
      if (ISudokuSolver.isPlacementValid(workingGrid, row, column, candidate)) {
        workingGrid.setValue(row, column, candidate);
        solve(workingGrid, solutions, solutions_required);
        workingGrid.setValue(row, column, -1);
      }
    }

  }

  // Convenience method to find & return the next empty cell
  private static Tuple2<Integer, Integer> findNextEmptyCell(IGridState grid_to_check) {

    Tuple2<Integer, Integer> bestCell = null;
    int minCandidates = Integer.MAX_VALUE;

    for (int row = 0; row < grid_to_check.getCellSize(); row++) {
      for (int column = 0; column < grid_to_check.getCellSize(); column++) {

        if (grid_to_check.getValue(row, column) == -1) {
          int candidateCount = 0;
          for (int candidate = 1; candidate <= 9; candidate++) {
            if (ISudokuSolver.isPlacementValid(grid_to_check, row, column, candidate)) {
              candidateCount++;
            }
          }
          if (candidateCount == 0) {
            return new Tuple2<>(-1, -1);
          }
          if (candidateCount < minCandidates) {
            minCandidates = candidateCount;
            bestCell = new Tuple2<>(row, column);
            if (candidateCount == 1) {
              return bestCell;
            }
          }
        }

      }
    }
    return bestCell;
  }

  @Override
  public boolean isValidMove(int row, int column, int value) {
    
    // Input Validation //

    if (value == -1) { // Always allow cells to be emptied
      return true;
    } else if (value < 1 || value > 9) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and 9 inclusive.");
    }

    int[] grid_size = grid.getGridSize();
    if (row < 0 || row >= grid_size[0] * 3 || column < 0 || column >= grid_size[1] * 3) {
      throw new IndexOutOfBoundsException("row or column out of bounds");
    }

    return ISudokuSolver.isPlacementValid(grid.getGrid(), row, column, value);

  }

}
