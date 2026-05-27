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
    // Note: Currently, the hasUniqueSolution parameter is not enforced. Future implementations
    // may add validation to ensure unique solutions when this parameter is true.
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
            if (isValidPlacement(row, col, value)) {
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
   * Checks if a value can be placed at the given position.
   * @param row The row index
   * @param col The column index
   * @param value The value to check
   * @return true if the placement is valid, false otherwise
   */
  private boolean isValidPlacement(int row, int col, int value) {
    // Check row
    for (int i = 0; i < grid_cell_size; i++) {
      if (i != col && grid.getValue(row, i) == value) {
        return false;
      }
    }
    
    // Check column
    for (int i = 0; i < grid_cell_size; i++) {
      if (i != row && grid.getValue(i, col) == value) {
        return false;
      }
    }
    
    // Check 3x3 box
    int box_row = row - row % 3;
    int box_col = col - col % 3;
    for (int i = box_row; i < box_row + 3; i++) {
      for (int j = box_col; j < box_col + 3; j++) {
        if ((i != row || j != col) && grid.getValue(i, j) == value) {
          return false;
        }
      }
    }
    
    return true;
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
   * @return whether the target number of cells to remove could be reached.
   */
  private boolean removeCells() {
    
    Tuple2<Integer, Integer> target_cells = difficulty.getCellsToRemove();
    int min_cells = target_cells.first();
    int max_cells = target_cells.second();
    int target_to_remove = randomInteger(min_cells, max_cells + 1);
    
    int cells_removed = 0;
    boolean[][] removed_cells = new boolean[grid_cell_size][grid_cell_size];
    int max_removal_attempts = grid_cell_size * grid_cell_size * 3; // Prevent infinite loops
    int attempts = 0;
    
    while (cells_removed < target_to_remove && attempts < max_removal_attempts) {
      attempts++;
      
      int row = randomInteger(0, grid_cell_size);
      int col = randomInteger(0, grid_cell_size);
      
      // Skip if cell is already empty
      if (grid.getValue(row, col) == -1) {
        continue;
      }
      
      // Skip if cell has already been removed
      if (removed_cells[row][col]) {
        continue;
      }
      
      // Calculate symmetric cells based on symmetry type
      List<Tuple2<Integer, Integer>> cells_to_remove = getSymmetricCells(row, col);
      
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
      
      // Remove the cells
      for (Tuple2<Integer, Integer> cell : cells_to_remove) {
        int r = cell.first();
        int c = cell.second();
        grid.setValue(r, c, -1);
        removed_cells[r][c] = true;
        cells_removed++;
      }
    }
    
    return cells_removed >= min_cells;
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
