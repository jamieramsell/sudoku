package sudoku;

public class SudokuSolver implements ISudokuSolver {

  private final ISudokuGrid grid;

  public SudokuSolver(ISudokuGrid grid) {
    this.grid = grid;
  }

  // To do
  public boolean isSolvable() {return false;}

  // To do
  public boolean hasUniqueSolution() {return false;}

  // To do
  public int countSolutions() {return -1;}

  // To do
  public int[][] solveGrid() {return new int[][]{{-1}};}

  // To do
  public boolean isValidMove(int row, int column, int value) {return false;}

}
