package sudoku;

import java.util.List;
import java.util.ArrayList;

public class SudokuGenerator implements ISudokuGenerator{

  private final PuzzleDifficulty difficulty;
  private final PuzzleSymmetry symmetry;
  private ISudokuGrid grid;
  private final int grid_cell_size;
  private final List<Tuple2<Integer, Integer>> all_cells;
  private ISudokuSolver solver;

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
    this.all_cells = new ArrayList<>();
    for (int row = 0; row < grid_cell_size; row++) {
      for (int col = 0; col < grid_cell_size; col++) {
        this.all_cells.add(new Tuple2<>(row, col));
      }
    }
    this.solver = new SudokuSolver(grid);
  }

  @Override
  public ISudokuGrid generatePuzzle(boolean hasUniqueSolution) {
    
    // Try to generate a random puzzle until one has been found which can have the required number
    // of target cells removed.
    do {
      generateFullGrid();
    } while (!removeCells(hasUniqueSolution));

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
    for (int row = 0; row < grid_cell_size; row++) {
      for (int col = 0; col < grid_cell_size; col++) {
        if (grid.getValue(row, col) == -1) {
          // Try values in random order
          int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9};
          shuffleArray(values);
          
          for (int value : values) {
            // Precondition: cell must be empty during grid generation
            assert grid.getValue(row, col) == -1 : String.format("Cell at (%d,%d) must be empty", row, col);
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
   * @param hasUniqueSolution Whether the resulting puzzle must have a unique solution
   * @return whether the target number of cells to remove could be reached and constraints satisfied.
   */
  private boolean removeCells(boolean hasUniqueSolution) {
    
    // Reinitialize solver with the current grid state to ensure consistency
    this.solver = new SudokuSolver(grid);
    Tuple2<Integer, Integer> target_cells = difficulty.getCellsToRemove();
    int min_cells = target_cells.first();
    int max_cells = target_cells.second();
    int target_to_remove = randomInteger(min_cells, max_cells + 1);
    
    int cells_removed = 0;
    shuffleCells(all_cells);
    
    for (Tuple2<Integer, Integer> candidate : all_cells) {
      if (cells_removed >= target_to_remove) {
        break;
      }
      int row = candidate.first();
      int col = candidate.second();
      if (grid.getValue(row, col) == -1) {
        continue;
      }
      
      // Calculate symmetric cells based on symmetry type
      List<Tuple2<Integer, Integer>> cells_to_remove = getSymmetricCells(row, col);
      
      // Check if all symmetric cells can be removed
      boolean can_remove_all = true;
      for (Tuple2<Integer, Integer> cell : cells_to_remove) {
        int r = cell.first();
        int c = cell.second();
        if (grid.getValue(r, c) == -1) {
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
      int[] original_values = new int[cells_to_remove.size()];
      int index = 0;
      for (Tuple2<Integer, Integer> cell : cells_to_remove) {
        int r = cell.first();
        int c = cell.second();
        original_values[index++] = grid.getValue(r, c);
        grid.setValue(r, c, -1);
      }

      if (hasUniqueSolution && solver.solveGrid(2).size() != 1) {
        index = 0;
        for (Tuple2<Integer, Integer> cell : cells_to_remove) {
          int r = cell.first();
          int c = cell.second();
          grid.setValue(r, c, original_values[index++]);
        }
        continue;
      }

      cells_removed += cells_to_remove.size();
      if (cells_removed >= max_cells) {
        break;
      }
    }
    
    // Check if minimum cells were removed
    if (cells_removed < min_cells) {
      return false;
    }

    return true;
  }

  private void shuffleCells(List<Tuple2<Integer, Integer>> cells) {
    for (int i = cells.size() - 1; i > 0; i--) {
      int j = randomInteger(0, i + 1);
      Tuple2<Integer, Integer> temp = cells.get(i);
      cells.set(i, cells.get(j));
      cells.set(j, temp);
    }
  }
  
  /**
   * Gets the list of cells that should be removed based on the symmetry pattern.
   * @param row The row of the primary cell
   * @param col The column of the primary cell
   * @return A list of cells to remove
   */
  private List<Tuple2<Integer, Integer>> getSymmetricCells(int row, int col) {
    List<Tuple2<Integer, Integer>> cells = new ArrayList<>();
    cells.add(new Tuple2<>(row, col));
    
    switch (symmetry) {
      case ROTATIONAL -> {
        // 180-degree rotational symmetry
        int sym_row = grid_cell_size - 1 - row;
        int sym_col = grid_cell_size - 1 - col;
        if (sym_row != row || sym_col != col) {
          cells.add(new Tuple2<>(sym_row, sym_col));
        }
      }
      case REFLECTIONAL -> {
        // Vertical mirror symmetry (left-right)
        int sym_col = grid_cell_size - 1 - col;
        if (sym_col != col) {
          cells.add(new Tuple2<>(row, sym_col));
        }
      }
      case DIHEDRAL -> {
        // Both rotational and reflectional symmetry (4-fold symmetry)
        int sym_row = grid_cell_size - 1 - row;
        int sym_col = grid_cell_size - 1 - col;
        
        // Add reflection across vertical axis (left-right mirror)
        if (sym_col != col) {
          cells.add(new Tuple2<>(row, sym_col));
        }
        // Add reflection across horizontal axis (top-bottom mirror)
        if (sym_row != row) {
          cells.add(new Tuple2<>(sym_row, col));
        }
        // Add 180-degree rotation (point symmetry around center)
        if ((sym_row != row || sym_col != col) && !hasDuplicate(cells, sym_row, sym_col)) {
          cells.add(new Tuple2<>(sym_row, sym_col));
        }
      }
      case NONE -> {
        // No symmetry, just remove the single cell
      }
    }
    
    return cells;
  }
  
  /**
   * Checks if a cell is already in the list.
   * @param cells The list of cells
   * @param row The row to check
   * @param col The column to check
   * @return true if the cell is already in the list, false otherwise
   */
  private boolean hasDuplicate(List<Tuple2<Integer, Integer>> cells, int row, int col) {
    for (Tuple2<Integer, Integer> cell : cells) {
      if (cell.first() == row && cell.second() == col) {
        return true;
      }
    }
    return false;
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
