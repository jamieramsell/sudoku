package sudoku;

public interface ISudokuGenerator {

  /**
   * Represents the difficulty level of the generated Sudoku puzzle.
   * <p>Passed as a parameter to the method {@link generatePuzzle}.
   */
  public static enum PuzzleDifficulty {

    /**
     * Up to half of all cells are removed, yielding 70-45 givens.
     */
    VERY_EASY,

    /**
     * Around half of all cells are removed, yielding 35-45 givens.
     */
    EASY,

    /**
     * Around 50-60 cells are to be removed, yielding 20-30 givens.
     */
    MEDIUM,

    /**
     * The maximum number of cells will be removed from the puzzle.
     * <p>This typically constitutes 60-64 cells being removed, resulting in only 17-21 givens.
     */
    HARD,

    /**
     * The number of cells to be removed is completely random.
     * <p>This option will result in anywhere between 10 and 64 cells being removed from the grid.
     */
    RANDOM

  }

  /**
   * Generates the initial grid for a random Sudoku puzzle.
   * @param hasUniqueSolution Whether or not the generated puzzle should only have one solution.
   * Note that the removal of the requirement of having a unique solution can make solving a puzzle
   * much less challenging.
   * @return an {@link ISudokuGrid} representing the initial state of the puzzle.
   * 
   * @see {@link} PuzzleDifficulty
   */
  public ISudokuGrid generatePuzzle(boolean hasUniqueSolution);

}
