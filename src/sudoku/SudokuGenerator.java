package sudoku;

public class SudokuGenerator implements ISudokuGenerator{

  private final PuzzleDifficulty difficulty;
  private ISudokuGrid grid;

  /**
   * Initialises a {@code SudokuGenerator} with the given target difficulty.
   * 
   * @param difficulty The difficulty of the puzzle represents how many cells should already be
   * filled when starting the puzzle, or alternatively, how many cells the algorithm should remove.
   */
  public SudokuGenerator(PuzzleDifficulty difficulty) {
    this.difficulty = difficulty;
    grid = new SudokuGrid();
  }

  @Override
  public ISudokuGrid generatePuzzle(boolean hasUniqueSolution) {
    
    // Try to generate a random puzzle until one has been found which can have the required number
    // of target cells removed.
    do {
      generateFullGrid();
    } while (!removeCells());

    return grid;

  }

  /**
   * Convenience method to generate a random completely filled Sudoku puzzle.
   * <p>This method acts directly upon {@code grid}, rather than returning an updated version.
   */
  private void generateFullGrid() {
    throw new UnsupportedOperationException();
  }

  /**
   * Convenience method to remove a target number of cells from the filled sudoku puzzle.
   * <p>The number of cells to remove is determined by the difficulty of the
   * {@code SudokuGenerator}, which was assigned upon instantiation.
   * <p>This method acts directly upon {@code grid}, rather than returning an updated version.
   * @return whether the target number of cells to remove could be reached.
   */
  private boolean removeCells() {
    throw new UnsupportedOperationException();
  }


}
