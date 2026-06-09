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
  public Set<ISudokuGrid> solveGrid() {
    return solveGrid(-1);
  }

  @Override
  public Set<ISudokuGrid> solveGrid(int solutions_required) {

    if (solutions_required == 0 || solutions_required < -1) {
      throw new IllegalArgumentException("solutions_required must be >= 1, or set to -1 to " +
      "generate all possible solutions.");
    }

    ISudokuGrid working_grid = grid.clone();
    Set<ISudokuGrid> solutions = new HashSet<>();

    if (!grid.isValid() || !ISudokuSolver.isGridStateValid(working_grid)) {
      return solutions;
    }

    solve(working_grid, solutions, solutions_required);
    return solutions;
  }

  // Convenience method to contain the exhaustive search which finds all solutions to a puzzle in
  // a given state.
  private static void solve(ISudokuGrid working_grid, Set<ISudokuGrid> solutions,
      int solutions_required) {

    if (solutions.size() == solutions_required) {
      return;
    }

    Tuple2<Integer, Integer> empty_cell = findNextEmptyCell(working_grid);
    if (empty_cell == null) {
      solutions.add(working_grid.clone());
      return;
    }

    int row = empty_cell.first();
    int column = empty_cell.second();

    for (int candidate = 1; candidate <= working_grid.getSize(); candidate++) {
      if (ISudokuSolver.isPlacementValid(working_grid, row, column, candidate)) {
        working_grid.setValue(row, column, candidate);
        solve(working_grid, solutions, solutions_required);
        working_grid.setValue(row, column, -1);
      }
    }

  }

  // Convenience method to find & return the next empty cell
  private static Tuple2<Integer, Integer> findNextEmptyCell(ISudokuGrid grid_to_check) {

    for (int row = 0; row < grid_to_check.getSize(); row++) {
      for (int column = 0; column < grid_to_check.getSize(); column++) {

        if (grid_to_check.getValue(row, column) == -1) {
          return new Tuple2<>(row, column);
        }

      }
    }
    return null;
  }

  @Override
  public boolean isValidMove(int row, int column, int value) {
    
    // Input Validation //

    if (value == -1) { // Always allow cells to be empty
      return true;
    } else if (value < 1 || value > grid.getSize()) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and "
          + grid.getSize() + "  inclusive.");
    }

    if (row < 0 || row >= grid.getSize() || column < 0 || column >= grid.getSize()) {
      throw new IndexOutOfBoundsException("row or column out of bounds");
    }

    return ISudokuSolver.isPlacementValid(grid, row, column, value);

  }

}
