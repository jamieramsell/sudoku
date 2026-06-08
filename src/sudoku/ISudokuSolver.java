package sudoku;

import java.util.Set;

public interface ISudokuSolver {

  /**
   * Uses backtracking to compute whether or not the grid in its current state is solvable.
   * @author Jamie
   * @return whether or not the sudoku puzzle is solvable
   */
  public boolean isSolvable();

  /**
   * Determines whether or not the sudoku puzzle has exactly one valid solution.
   * @author Jamie
   * @return true if the puzzle has one solution; false otherwise
   */
  public boolean hasUniqueSolution();

  public int countSolutions();
  
  /**
   * Computes a set of solutions for the sudoku grid in its current state.
   * @author Jamie
   * @return the (possibly empty) set of all possible solutions for the grid.
   */
  public Set<GridState> solveGrid();

  /**
   * Computes a set of solutions for the sudoku grid in its current state.
   * @author Jamie
   * @param solutions_required The maximum number of solutions that should be generated.
   * @return the (possibly empty) set of possible solutions for the grid.
   */
  public Set<GridState> solveGrid(int solutions_required);

  /**
   * Checks whether setting a given cell to a certain value would follow the rules of Sudoku.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * @author Jamie
   * @param row the row (x-coordinate) of the cell to check.
   * @param column the column (y-coordinate) of the cell to check.
   * @param value the value to test.
   * @return whether the value can be legally placed in the cell
   */
  public boolean isValidMove(int row, int column, int value);

  /**
   * General purpose method to check whether a move is legal in a given grid state.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * 
   * @author Jamie
   * @param grid_to_check The current state of the grid to check.
   * @param row The row (x-coordinate) of the cell to check.
   * @param column The column (y-coordinate) of the cell to check
   * @param value The value to be checked.
   * @return Whether {@code value} can be inserted into {@code grid_to_check} at
   * {@code (row, column)}.
   */
  static boolean isPlacementValid(GridState grid_to_check, int row, int column, int value) {

    // Can use a single for loop here as a grid is always square
    for (int index = 0; index < (grid_to_check.getCellSize()); index++) {
      if (index != column && grid_to_check.getValue(row, index) == value) {
        return false;
      }
      if (index != row && grid_to_check.getValue(index, column) == value) {
        return false;
      }
    }

    // Check if any cell in the box is a duplicate
    int top_left_row = row - row % 3;
    int top_left_col = column - column % 3;

    for (int current_row = top_left_row; current_row < top_left_row + 3; current_row++) {
      for (int current_col = top_left_col; current_col < top_left_col + 3; current_col++) {

        if (current_row == row && current_col == column) {
          continue; // Ignore the target cell's former value, which will be irrelevant once replaced
        }
        if (value == grid_to_check.getValue(current_row, current_col)) {
          return false;
        }

      }
    }

    return true;
  }

  /**
   * Checks to ensure the state of the grid provided is legal in terms of the Sudoku game rules.
   * @author Jamie
   * @param grid_to_check The grid state to check.
   * @return Whether the grid state is valid.
   */
  public static boolean isGridStateValid(GridState grid_to_check) {

    for (int row = 0; row < grid_to_check.getCellSize(); row++) {
      for (int column = 0; column < grid_to_check.getCellSize(); column++) {

        int value = grid_to_check.getValue(row, column);

        if (value == -1) { // Cells are always allowed to be empty
          continue;
        } else if (value < 1 || value > 9) { // Check for out of bounds values
          return false;
        }

        // Check that the value of the current cell is legal
        boolean valid = isPlacementValid(grid_to_check, row, column, value);

        if (!valid) {
          return false;
        }

      }
    }

    return true;
  }

}
