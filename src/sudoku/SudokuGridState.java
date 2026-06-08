package sudoku;

import java.util.List;
import java.util.ArrayList;

public class SudokuGridState implements ISudokuState {

  private List<List<ISudokuState>> grid;
  private final int size;
  private final Tuple2<Integer, Integer> grid_dimensions;
  private final Tuple2<Integer, Integer> box_dimensions;
  private final int rows;
  private final int columns;

  /**
   * Initialises an empty 9x9 sudoku grid.
   * <p>Empty cells are represented by {@code -1}.
   */
  public SudokuGridState() {
    this(ISudokuGrid.DEFAULT_SIZE);
  }

  /**
   * Initialises an empty sudoku grid with the given size.
   * <p> A size of 9 means that each square in the sudoku grid contains the numbers 1-9.
   * <p> Sizes supported are all values {@code x}, where {@code 2 <= x <= 16}, and {@code x} is
   * either even, square, or both.
   * @param size The size of the sudoku grid to generate
   */
  public SudokuGridState(int size) {
    ISudokuGrid.validateGridSize(size);

    // Calculate the number of boxes required for the given size of sudoku puzzle
    Tuple2<Integer, Integer> box_dimensions = SudokuBox.calculateBoxSize(size);
    int rows = size / box_dimensions.first();
    int columns = size / box_dimensions.second();

    // Calculate the number of cells in the grid
    int rows_of_cells = box_dimensions.first() * rows;
    int cols_of_cells = box_dimensions.second() * columns;
    Tuple2<Integer, Integer> grid_dimensions = new Tuple2<>(rows_of_cells, cols_of_cells);

    // Initialise the grid of sudoku boxes
    List<List<ISudokuState>> new_grid = new ArrayList<>();
    for (int row = 0; row < rows; row++) {
      new_grid.add(row, new ArrayList<>());
      for (int column = 0; column < columns; column++) {
        new_grid.get(row).add(column, new SudokuBox(size));
      }
    }

    // Initialise attributes
    this.grid = new_grid;
    this.size = size;
    this.grid_dimensions = grid_dimensions;
    this.box_dimensions = box_dimensions;
    this.rows = rows;
    this.columns = columns;
  }

  @Override
  public int getValue(int row, int column) {
    validateCoordinates(row, column);

    // Find the box in which the given cell is stored
    int box_row = row / box_dimensions.first();
    int box_col = column / box_dimensions.second();
    ISudokuState box_containing_cell = grid.get(box_row).get(box_col);

    // Find coordinates of the cell within that box
    int cell_row = row % box_dimensions.first();
    int cell_col = column % box_dimensions.second();

    // Find cell value & return
    int cell_value = box_containing_cell.getValue(cell_row, cell_col);
    return cell_value;
  }

  @Override
  public void setValue(int row, int column, int value) {
    // Validation
    validateCoordinates(row, column);
    if ((value < 1 || value > size) && value != -1) {
      throw new IllegalArgumentException("value must be either -1, or between 1 and " + size +
          " inclusive.");
    } 

    // Find the box in which the given cell is stored
    int box_row = row / box_dimensions.first();
    int box_col = column / box_dimensions.second();
    ISudokuState box_containing_cell = grid.get(box_row).get(box_col);

    // Find coordinates of the cell within that box & assign value
    int cell_row = row % box_dimensions.first();
    int cell_col = column % box_dimensions.second();
    box_containing_cell.setValue(cell_row, cell_col, value);
  }

  /**
   * Convenience method to validate that the cell at the given coordinates exists within the grid.
   * @param row The row (or y-coordinate) of the cell to check.
   * @param col The column (or x-coordinate) of the cell to check.
   * @throws IndexOutOfBoundsException if the given cell is out of bounds of the grid.
   */
  private void validateCoordinates(int row, int col) {
    if (row < 0 || col < 0 || row > size || col > size) {
      throw new IndexOutOfBoundsException("Target cell does not exist within the grid.");
    }
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public Tuple2<Integer, Integer> getGridDimensions() {
    return grid_dimensions;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof SudokuGridState)) {
      return false;
    }

    SudokuGridState casted_other = (SudokuGridState) other;

    // Check whether both grids have the same dimensions
    if (!(grid_dimensions.equals(casted_other.getGridDimensions()))) {
      return false;
    }

    // Check whether each cell value in both boxes are equal
    for (int row = 0; row < grid_dimensions.first(); row++) {
      for (int col = 0; col < grid_dimensions.second(); col++) {
        if (getValue(row, col) != casted_other.getValue(row, col)) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hash = 17;

    for (int row = 0; row < grid_dimensions.first(); row++) {
      for (int col = 0; col < grid_dimensions.second(); col++) {
        int value = getValue(row, col);
        if (value == -1) {
          value = 0;
        }
        hash = 31 * hash + value;
      }
    }

    return hash;
  }

  @Override
  public SudokuGridState clone() {
    SudokuGridState clone = new SudokuGridState(size);

    for (int row = 0; row < rows; row++) {
      List<ISudokuState> row_of_clone = clone.grid.get(row);
      List<ISudokuState> row_of_this = grid.get(row);
      
      // Reassign each cell of the current grid
      for (int col = 0; col < columns; col++) {
        ISudokuState cell_of_this = row_of_this.get(col);
        row_of_clone.set(col, cell_of_this.clone());
      }
    }

    return clone;
  }

}
