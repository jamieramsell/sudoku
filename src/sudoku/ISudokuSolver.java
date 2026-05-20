package sudoku;

import java.util.List;

public interface ISudokuSolver {

  /**
   * Uses backtracking to compute whether or not the grid in its current state is solvable.
   * @return whether or not the sudoku puzzle is solvable
   */
  public boolean isSolvable();

  /**
   * Determines whether or not the sudoku puzzle has exactly one valid solution.
   * @return true if the puzzle has one solution; false otherwise
   */
  public boolean hasUniqueSolution();

  public int countSolutions();
  
  /**
   * Computes a set of solutions for the sudoku grid in its current state.
   * @return the (possibly empty) set of all possible solutions for the grid.
   */
  public List<int[][]> solveGrid();

  /**
   * Checks whether setting a given cell to a certain value would follow the rules of Sudoku.
   * <p>Coordinates are indexed from (0, 0), which is the upper left-most square in the grid.
   * @param row the row (x-coordinate) of the cell to check.
   * @param column the column (y-coordinate) of the cell to check.
   * @param value the value to test.
   * @return whether the value can be legally placed in the cell
   */
  public boolean isValidMove(int row, int column, int value);

}
