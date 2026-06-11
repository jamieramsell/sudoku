package sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

/** Tests for {@link SudokuSolver} and the static helpers on {@link ISudokuSolver}. */
public class SudokuSolverTest {

  @Test
  public void isPlacementValid_validPlacement_returnsTrue() {
    SudokuGridState state = new SudokuGridState();
    assertTrue(ISudokuSolver.isPlacementValid(state, 0, 0, 5));
  }

  @Test
  public void isPlacementValid_rowConflict_returnsFalse() {
    SudokuGridState state = new SudokuGridState();
    state.setValue(0, 1, 5);

    assertFalse(ISudokuSolver.isPlacementValid(state, 0, 0, 5));
  }

  @Test
  public void isPlacementValid_columnConflict_returnsFalse() {
    SudokuGridState state = new SudokuGridState();
    state.setValue(1, 0, 5);

    assertFalse(ISudokuSolver.isPlacementValid(state, 0, 0, 5));
  }

  @Test
  public void isPlacementValid_boxConflict_returnsFalse() {
    SudokuGridState state = new SudokuGridState();
    state.setValue(1, 1, 5);

    assertFalse(ISudokuSolver.isPlacementValid(state, 0, 0, 5));
  }

  @Test
  public void isGridStateValid_emptyGrid_returnsTrue() {
    assertTrue(ISudokuSolver.isGridStateValid(new SudokuGridState()));
  }

  @Test
  public void isGridStateValid_invalidGrid_returnsFalse() {
    SudokuGridState state = new SudokuGridState();
    state.setValue(0, 0, 5);
    state.setValue(0, 1, 5);

    assertFalse(ISudokuSolver.isGridStateValid(state));
  }

  @Test
  public void isValidMove_delegatesToIsPlacementValid() {
    SudokuGrid grid = new SudokuGrid();
    SudokuSolver solver = new SudokuSolver(grid);

    assertTrue(solver.isValidMove(0, 0, 5));

    grid.setValue(0, 1, 5);
    assertFalse(solver.isValidMove(0, 0, 5));
  }

  @Test
  public void isValidMove_emptyValueAlwaysAllowed() {
    SudokuGrid grid = new SudokuGrid();
    SudokuSolver solver = new SudokuSolver(grid);

    assertTrue(solver.isValidMove(0, 0, -1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isValidMove_valueOutOfRange_throws() {
    new SudokuSolver(new SudokuGrid()).isValidMove(0, 0, 10);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void isValidMove_coordinatesOutOfRange_throws() {
    new SudokuSolver(new SudokuGrid()).isValidMove(9, 0, 5);
  }

  @Test
  public void isSolvable_emptyGrid_returnsTrue() {
    SudokuSolver solver = new SudokuSolver(new SudokuGrid());
    assertTrue(solver.isSolvable());
  }

  @Test
  public void isSolvable_invalidGrid_returnsFalse() {
    SudokuGrid grid = new SudokuGrid();
    grid.setValue(0, 0, 5);
    grid.setValue(0, 1, 5);

    assertFalse(new SudokuSolver(grid).isSolvable());
  }

  @Test
  public void solveGrid_fourByFourEmptyGrid_hasTwoHundredEightyEightSolutions() {
    SudokuSolver solver = new SudokuSolver(new SudokuGrid(4));

    Set<ISudokuGrid> solutions = solver.solveGrid();

    assertEquals(288, solutions.size());
    for (ISudokuGrid solution : solutions) {
      assertTrue(solution.isSolved());
    }
  }

  @Test
  public void countSolutions_matchesSolveGridSize() {
    SudokuSolver solver = new SudokuSolver(new SudokuGrid(4));
    assertEquals(288, solver.countSolutions());
  }

  @Test
  public void hasUniqueSolution_singleEmptyCell_returnsTrue() {
    SudokuGrid grid = new SudokuGrid(4);
    SudokuSolver solver = new SudokuSolver(grid);
    ISudokuGrid solved = solver.solveGrid(1).iterator().next();

    SudokuGrid almost_complete = (SudokuGrid) solved.clone();
    almost_complete.setValue(0, 0, -1);

    assertTrue(new SudokuSolver(almost_complete).hasUniqueSolution());
  }

  @Test
  public void hasUniqueSolution_emptyGrid_returnsFalse() {
    SudokuSolver solver = new SudokuSolver(new SudokuGrid(4));
    assertFalse(solver.hasUniqueSolution());
  }

  @Test(expected = IllegalArgumentException.class)
  public void solveGrid_zeroSolutionsRequired_throws() {
    new SudokuSolver(new SudokuGrid(4)).solveGrid(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void solveGrid_negativeSolutionsRequired_throws() {
    new SudokuSolver(new SudokuGrid(4)).solveGrid(-2);
  }

  @Test
  public void isSolvable_fullySolvedGrid_returnsTrue() {
    SudokuGrid solved = createSolvedFourByFourGrid();
    assertTrue(new SudokuSolver(solved).isSolvable());
  }

  @Test
  public void countSolutions_fullySolvedGrid_returnsOne() {
    SudokuGrid solved = createSolvedFourByFourGrid();
    assertEquals(1, new SudokuSolver(solved).countSolutions());
  }

  @Test
  public void solveGrid_fullySolvedGrid_returnsSetContainingEqualGrid() {
    SudokuGrid solved = createSolvedFourByFourGrid();
    Set<ISudokuGrid> solutions = new SudokuSolver(solved).solveGrid();

    assertEquals(1, solutions.size());
    assertEquals(solved, solutions.iterator().next());
  }

  @Test
  public void countSolutions_invalidGrid_returnsZero() {
    SudokuGrid grid = new SudokuGrid();
    grid.setValue(0, 0, 5);
    grid.setValue(0, 1, 5);

    assertEquals(0, new SudokuSolver(grid).countSolutions());
  }

  @Test
  public void isGridStateValid_fullySolvedGrid_returnsTrue() {
    SudokuGrid solved = createSolvedFourByFourGrid();
    assertTrue(ISudokuSolver.isGridStateValid(solved));
  }

  @Test
  public void isPlacementValid_usingSudokuGrid_detectsRowConflict() {
    SudokuGrid grid = new SudokuGrid();
    grid.setValue(0, 1, 5);

    assertFalse(ISudokuSolver.isPlacementValid(grid, 0, 0, 5));
    assertTrue(ISudokuSolver.isPlacementValid(grid, 0, 0, 6));
  }

  // Builds a fully-solved, valid 4x4 sudoku grid:
  //   1 2 | 3 4
  //   3 4 | 1 2
  //   -------
  //   2 1 | 4 3
  //   4 3 | 2 1
  private static SudokuGrid createSolvedFourByFourGrid() {
    int[][] values = {
        {1, 2, 3, 4},
        {3, 4, 1, 2},
        {2, 1, 4, 3},
        {4, 3, 2, 1},
    };

    SudokuGrid grid = new SudokuGrid(4);
    for (int row = 0; row < 4; row++) {
      for (int col = 0; col < 4; col++) {
        grid.setValue(row, col, values[row][col]);
      }
    }
    return grid;
  }

}
