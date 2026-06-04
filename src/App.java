import sudoku.*;
import sudoku.ISudokuGenerator.PuzzleDifficulty;
import sudoku.ISudokuGenerator.PuzzleSymmetry;

public class App {
  public static void main(String[] args) {
    
    ISudokuGenerator generator = new SudokuGenerator(PuzzleDifficulty.MEDIUM, PuzzleSymmetry.NONE);
    ISudokuGrid grid = generator.generatePuzzle(true); 

    System.out.println(grid);

    ISudokuSolver solver = new SudokuSolver(grid);
    System.out.println(solver.isSolvable());

  }
}
