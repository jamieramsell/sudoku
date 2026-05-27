package sudoku;

import java.util.List;
import java.util.ArrayList;

public class SudokuGenerator implements ISudokuGenerator{

  private final PuzzleDifficulty difficulty;
  private final PuzzleSymmetry symmetry;
  private ISudokuGrid grid;
  private ISudokuGrid working_grid;
  private final int grid_cell_size;

  /**
   * Initialises a {@code SudokuGenerator} with the given target difficulty.
   * 
   * @param difficulty The difficulty of the puzzle represents how many cells should already be
   * filled when starting the puzzle, or alternatively, how many cells the algorithm should remove.
   * @param symmetry Represents the symmetry (if any) of the initial state of the puzzle.
   */
  public SudokuGenerator(PuzzleDifficulty difficulty, PuzzleSymmetry symmetry) {
    this.difficulty = difficulty;
    this.symmetry = symmetry;
    this.grid = new SudokuGrid();
    this.grid_cell_size = grid.getGrid().getCellSize();
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
   * @param hasUniqueSolution Whether or not the generated puzzle should only have one solution.
   */
  private void generateFullGrid() {

    // Firstly generate a random cell
    int random_value = randomInteger(1, 10);
    int random_x = randomInteger(0, grid_cell_size);
    int random_y = randomInteger(0, grid_cell_size);
    grid.setValue(random_x, random_y, random_value);

    // Then compute a random solution
    ISudokuSolver solver = new SudokuSolver(grid);
    List<IGridState> solutions = new ArrayList<>();
    solutions.addAll(solver.solveGrid());
    int num_solutions = solutions.size();
    IGridState solution = solutions.get(randomInteger(0, num_solutions));

    // Finally, update grid attribute with solved values of cells
    for (int row = 0; row < grid_cell_size; row++) {
      for (int col = 0; col < grid_cell_size; col++) {
        grid.setValue(row, col, solution.getValue(row, col));
      }
    }

  }

  /**
   * Convenience method to remove a target number of cells from the filled sudoku puzzle.
   * <p>The number of cells to remove is determined by the difficulty of the
   * {@code SudokuGenerator}, which was assigned upon instantiation.
   * <p>This method acts directly upon {@code grid}, rather than returning an updated version.
   * @return whether the target number of cells to remove could be reached.
   */
  private boolean removeCells() {

    // Todo: complete implementation
    
    Tuple2<Integer, Integer> target_cells = difficulty.getCellsToRemove();

    switch (symmetry) {
      case PuzzleSymmetry.ROTATIONAL -> {break;}
      case PuzzleSymmetry.REFLECTIONAL -> {break;}
      case PuzzleSymmetry.DIHEDRAL -> {break;}
      case PuzzleSymmetry.NONE -> {break;}
    }
    
    Tuple2<Integer, Integer> cell_to_remove = null;

    throw new UnsupportedOperationException();

  }

  /**
   * Convenience function to generate a random integer between the two values provided.
   * @param lower_bound The inclusive lower bound of the number to generate.
   * @param upper_bound The upper bound of the number to generate. (non-inclusive)
   * @return A random number {@code lower_bound <= n < upper_bound}
   */
  private static int randomInteger(int lower_bound, int upper_bound) {
    int num_possibilities = upper_bound - lower_bound;
    return lower_bound + ((int) Math.random()) * (num_possibilities);
  }

}
