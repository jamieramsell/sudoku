package sudoku.generation;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import sudoku.ISudokuGrid;
import sudoku.ISudokuSolver;
import sudoku.SudokuGrid;
import sudoku.SudokuSolver;
import sudoku.Tuple2;
import sudoku.generation.symmetry.AbstractSymmetry;

/**
 * Generates 9x9 Sudoku puzzles by producing a full valid grid and then removing cells according to
 * a target {@link PuzzleDifficulty} and {@link AbstractSymmetry} pattern.
 */
public class SudokuGenerator implements ISudokuGenerator{

  private final PuzzleDifficulty difficulty;
  private final AbstractSymmetry symmetry;
  private final int size;
  private ISudokuGrid grid;
  private ISudokuSolver solver;

  /**
   * Initialises a {@code SudokuGenerator} with the given target difficulty.
   * 
   * @param difficulty The difficulty of the puzzle represents how many cells should already be
   * filled when starting the puzzle, or alternatively, how many cells the algorithm should remove.
   * @param symmetry Represents the symmetry (if any) of the initial state of the puzzle.
   * 
   * @see PuzzleDifficulty
   * @see AbstractSymmetry
   */
  public SudokuGenerator(PuzzleDifficulty difficulty, AbstractSymmetry symmetry) {
    this.difficulty = difficulty;
    this.symmetry = symmetry;
    this.grid = new SudokuGrid();
    this.size = grid.getSize();
    this.solver = new SudokuSolver(grid);
  }

  @Override
  public ISudokuGrid generatePuzzle(boolean has_unique_solution) {
    
    /* Try to generate a random puzzle until one has been found which can have the required number
     * of target cells removed.
     * Loop is bounded by max_removal_attempts of removeCells.
     */
    do {
      generateFullGrid();
    } while (!removeCells(has_unique_solution));

    return grid;

  }

  /**
   * Convenience method to generate a random completely filled Sudoku puzzle.
   * <p>This method acts directly upon {@code grid}, rather than returning an updated version.
   */
  private void generateFullGrid() {
    // Reset the grid to empty state
    grid.resetGrid();
    
    // Fill the grid using backtracking
    fillGridBacktracking();
  }
  
  /**
   * Fills the grid using a backtracking algorithm with randomization.
   * @return true if the grid was successfully filled, false otherwise
   */
  private boolean fillGridBacktracking() {
    // Find the first empty cell
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (grid.getValue(row, col) == -1) {
          // Try values in random order
          int[] values = IntStream.rangeClosed(1, size).toArray();
          shuffleArray(values);
          
          for (int value : values) {
            if (solver.isValidMove(row, col, value)) {
              grid.setValue(row, col, value);

              if (fillGridBacktracking()) {
                return true;
              }

              // Backtrack
              grid.setValue(row, col, -1);
            }
          }
          return false;
        }
      }
    }
    return true; // Grid is full
  }
  
  /**
   * Shuffles an array in place using Fisher-Yates shuffle.
   * @param array The array to shuffle
   */
  private void shuffleArray(int[] array) {
    for (int i = array.length - 1; i > 0; i--) {
      int j = randomInteger(0, i + 1);
      int temp = array[i];
      array[i] = array[j];
      array[j] = temp;
    }
  }

  /**
   * Convenience method to remove a target number of cells from the filled sudoku puzzle.
   * <p>The number of cells to remove is determined by the difficulty of the
   * {@code SudokuGenerator}, which was assigned upon instantiation.
   * <p>This method acts directly upon {@code grid}, rather than returning an updated version.
   * @param has_unique_solution Whether the resulting puzzle must have a unique solution
   * @return whether the target number of cells to remove could be reached and constraints satisfied.
   */
  private boolean removeCells(boolean has_unique_solution) {
    
    // Reinitialize solver with the current grid state to ensure consistency
    this.solver = new SudokuSolver(grid);
    Tuple2<Integer, Integer> target_cells = difficulty.getCellsToRemove();
    int min_cells = target_cells.first();
    int max_cells = target_cells.second();
    int target_to_remove = randomInteger(min_cells, max_cells + 1);
    
    int cells_removed = 0;
    boolean[][] removed_cells = new boolean[size][size];
    // Limit removal attempts to prevent infinite loops: multiply total cells by 3
    // to account for cells already removed and symmetry constraints
    int max_removal_attempts = size * size * 3;
    int attempts = 0;
    
    while (cells_removed < target_to_remove && attempts < max_removal_attempts) {
      attempts++;
      
      int row = randomInteger(0, size);
      int col = randomInteger(0, size);
      
      // Skip if cell is already empty
      if (grid.getValue(row, col) == -1) {
        continue;
      }
      
      // Skip if cell has already been removed
      if (removed_cells[row][col]) {
        continue;
      }
      
      // Calculate symmetric cells based on symmetry type
      List<Tuple2<Integer, Integer>> cells_to_remove = symmetry.getSymmetricCells(row, col);
      
      // Check if all symmetric cells can be removed
      boolean can_remove_all = true;
      for (Tuple2<Integer, Integer> cell : cells_to_remove) {
        int r = cell.first();
        int c = cell.second();
        if (grid.getValue(r, c) == -1 || removed_cells[r][c]) {
          can_remove_all = false;
          break;
        }
      }
      
      if (!can_remove_all) {
        continue;
      }
      
      // Skip if this symmetric group would exceed maximum removals for the selected difficulty
      if (cells_removed + cells_to_remove.size() > max_cells) {
        continue;
      }

      // Remove the cells
      for (Tuple2<Integer, Integer> cell : cells_to_remove) {
        int r = cell.first();
        int c = cell.second();
        grid.setValue(r, c, -1);
        removed_cells[r][c] = true;
        cells_removed++;
      }
    }
    
    // Check if minimum cells were removed
    if (cells_removed < min_cells) {
      return false;
    }
    
    // If unique solution is required, verify it
    if (has_unique_solution) {
      // Only need to find at most 2 solutions: if exactly 1, it's unique; if 2+, it's not unique
      Set<ISudokuGrid> solutions = solver.solveGrid(2);
      if (solutions.size() != 1) {
        // Puzzle has multiple or no solutions, reject and try again
        return false;
      }
    }
    
    return true;
  }
  
  /**
   * Convenience function to generate a random integer between the two values provided.
   * @param lower_bound The inclusive lower bound of the number to generate.
   * @param upper_bound The upper bound of the number to generate. (non-inclusive)
   * @return A random number {@code lower_bound <= n < upper_bound}
   */
  private static int randomInteger(int lower_bound, int upper_bound) {
    int num_possibilities = upper_bound - lower_bound;
    return lower_bound + (int) (Math.random() * num_possibilities);
  }

}
