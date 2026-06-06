package sudoku.generation;

import sudoku.Tuple2;
import sudoku.ISudokuGrid;

public interface ISudokuGenerator {

  /**
   * Represents the difficulty level of the generated Sudoku puzzle.
   */
  public static enum PuzzleDifficulty {

    /**
     * Up to half of all cells are removed, yielding 70-40 givens.
     */
    VERY_EASY(new Tuple2<Integer, Integer>(10, 40)),

    /**
     * Around half of all cells are removed, yielding 30-40 givens.
     */
    EASY(new Tuple2<Integer, Integer>(40, 50)),

    /**
     * Around 50-60 cells are to be removed, yielding 20-30 givens.
     */
    MEDIUM(new Tuple2<Integer, Integer>(50, 60)),

    /**
     * The maximum number of cells will be removed from the puzzle.
     * <p>This typically constitutes 60-64 cells being removed, resulting in only 17-21 givens.
     */
    HARD(new Tuple2<Integer, Integer>(60, 64)),

    /**
     * The number of cells to be removed is completely random.
     * <p>This option will result in anywhere between 10 and 64 cells being removed from the grid.
     */
    RANDOM(new Tuple2<Integer, Integer>(10, 64));

    private Tuple2<Integer, Integer> cells_to_remove;

    private PuzzleDifficulty(Tuple2<Integer, Integer> cells_to_remove) {
      this.cells_to_remove = cells_to_remove;
    }

    Tuple2<Integer, Integer> getCellsToRemove() {
      return cells_to_remove;
    }

  }

  /**
   * Generates the initial grid for a random Sudoku puzzle.
   * @param hasUniqueSolution Whether or not the generated puzzle should only have one solution.
   * Note that the removal of the requirement of having a unique solution can make solving a puzzle
   * much less challenging.
   * @return an {@link ISudokuGrid} representing the initial state of the puzzle.
   */
  public ISudokuGrid generatePuzzle(boolean hasUniqueSolution);

}
