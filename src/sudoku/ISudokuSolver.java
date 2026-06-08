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
  public Set<ISudokuGrid> solveGrid();

  /**
   * Computes a set of solutions for the sudoku grid in its current state.
   * @author Jamie
   * @param solutions_required The maximum number of solutions that should be generated.
   * @return the (possibly empty) set of possible solutions for the grid.
   */
  public Set<ISudokuGrid> solveGrid(int solutions_required);

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
   * Check whether a move is legal in a given grid state.
   * <p> Coordinates are indexed from (0, 0), which is the upper left-most cell in the grid.
   * <p> Note that this function does not check whether the current grid state itself is valid,
   * only whether or not the placement of a specific cell is. 
   * @author Jamie
   * @param grid_to_check The current state of the grid to check.
   * @param row The row (x-coordinate) of the cell to check.
   * @param column The column (y-coordinate) of the cell to check
   * @param value The value to be checked.
   * @return Whether {@code value} can be inserted into {@code grid_to_check} at
   * {@code (row, column)}.
   */
  public static boolean isPlacementValid(ISudokuState grid_to_check, int row, int column, int value) {

    Tuple2<Integer, Integer> grid_dimensions = grid_to_check.getGridDimensions();
    final int rows = grid_dimensions.first();
    final int cols = grid_dimensions.second();

    /* First check all cells in the row, then all cells in the column.
     * Ignore the target cell itself.
     */
    for (int current_row = 0; current_row < rows; current_row++) {
      if (current_row == row) {
        continue;
      }
      if (grid_to_check.getValue(current_row, column) == value) {
        return false;
      }
    }

    for (int current_col = 0; current_col < cols; current_col++) {
      if (current_col == column) {
        continue;
      }
      if (grid_to_check.getValue(row, current_col) == value) {
        return false;
      }
    }

    // Finally check if the cell is a duplicate of another value within in its box
    return !(grid_to_check.checkForDuplicates(row, column));
  }

  /**
   * Checks to ensure the state of the grid provided is legal in terms of the Sudoku game rules.
   * @author Jamie
   * @param grid_to_check The grid state to check.
   * @return Whether the grid state is valid.
   */
  public static boolean isGridStateValid(ISudokuState grid_to_check) {

    Tuple2<Integer, Integer> grid_dimensions = grid_to_check.getGridDimensions();
    int rows = grid_dimensions.first();
    int cols = grid_dimensions.second();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        int value = grid_to_check.getValue(row, col);

        if (value == -1) { // Cells are always allowed to be empty
          continue;
        } else if (value < 1 || value > grid_to_check.getSize()) { // Check for out of bounds values
          return false;
        }

        // Check that the value of the current cell is legal
        boolean valid = isPlacementValid(grid_to_check, row, col, value);
        if (!valid) {
          return false;
        }
      }
    }

    return true;
  }

}
