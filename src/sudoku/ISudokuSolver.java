package sudoku;

public interface ISudokuSolver {

  /**
   * Solves a 9x9 sudoku puzzle.
   *
   * @param puzzle A 9x9 grid containing values 0-9, where 0 means empty.
   * @return A new solved 9x9 grid.
   * @throws IllegalArgumentException if the puzzle shape/content is invalid or no solution exists.
   */
  int[][] solve(int[][] puzzle);
}
