package sudoku.generation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import sudoku.ISudokuGrid;
import sudoku.SudokuSolver;
import sudoku.generation.ISudokuGenerator.PuzzleDifficulty;
import sudoku.generation.symmetry.NoSymmetry;
import sudoku.generation.symmetry.ReflectionalSymmetry;
import sudoku.generation.symmetry.RotationalSymmetry;

/** Tests for {@link SudokuGenerator}. */
public class SudokuGeneratorTest {

  @Test
  public void generatePuzzle_withUniqueSolution_isValidAndUniquelySolvable() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.EASY, new NoSymmetry(ISudokuGrid.DEFAULT_SIZE));

    ISudokuGrid puzzle = generator.generatePuzzle(true);

    assertTrue(puzzle.isValid());
    assertTrue("expected the generated puzzle to have empty cells", countEmptyCells(puzzle) > 0);
    assertTrue(new SudokuSolver(puzzle).hasUniqueSolution());
  }

  @Test
  public void generatePuzzle_withoutUniqueSolutionRequirement_isSolvable() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.MEDIUM, new RotationalSymmetry(ISudokuGrid.DEFAULT_SIZE));

    ISudokuGrid puzzle = generator.generatePuzzle(false);

    assertTrue(puzzle.isValid());
    assertTrue(new SudokuSolver(puzzle).isSolvable());
  }

  @Test
  public void generatePuzzle_removesCellsWithinDifficultyRange() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.EASY, new NoSymmetry(ISudokuGrid.DEFAULT_SIZE));

    ISudokuGrid puzzle = generator.generatePuzzle(true);
    int empty_cells = countEmptyCells(puzzle);

    // PuzzleDifficulty.EASY targets between 40 and 50 cells removed.
    assertTrue("expected at least 40 empty cells, got " + empty_cells, empty_cells >= 40);
    assertTrue("expected at most 50 empty cells, got " + empty_cells, empty_cells <= 50);
  }

  @Test
  public void generatePuzzle_veryEasyDifficulty_isWithinRange() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.VERY_EASY, new NoSymmetry(ISudokuGrid.DEFAULT_SIZE));

    ISudokuGrid puzzle = generator.generatePuzzle(true);
    int empty_cells = countEmptyCells(puzzle);

    // PuzzleDifficulty.VERY_EASY targets between 10 and 40 cells removed.
    assertTrue("expected at least 10 empty cells, got " + empty_cells, empty_cells >= 10);
    assertTrue("expected at most 40 empty cells, got " + empty_cells, empty_cells <= 40);
    assertTrue(puzzle.isValid());
    assertTrue(new SudokuSolver(puzzle).hasUniqueSolution());
  }

  @Test
  public void generatePuzzle_hardDifficulty_isWithinRange() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.HARD, new NoSymmetry(ISudokuGrid.DEFAULT_SIZE));

    // Without requiring a unique solution this remains fast even for the maximum removal range.
    ISudokuGrid puzzle = generator.generatePuzzle(false);
    int empty_cells = countEmptyCells(puzzle);

    // PuzzleDifficulty.HARD targets between 60 and 64 cells removed.
    assertTrue("expected at least 60 empty cells, got " + empty_cells, empty_cells >= 60);
    assertTrue("expected at most 64 empty cells, got " + empty_cells, empty_cells <= 64);
    assertTrue(puzzle.isValid());
    assertTrue(new SudokuSolver(puzzle).isSolvable());
  }

  @Test
  public void generatePuzzle_randomDifficulty_isWithinOverallRange() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.RANDOM, new NoSymmetry(ISudokuGrid.DEFAULT_SIZE));

    ISudokuGrid puzzle = generator.generatePuzzle(false);
    int empty_cells = countEmptyCells(puzzle);

    // PuzzleDifficulty.RANDOM targets between 10 and 64 cells removed.
    assertTrue("expected at least 10 empty cells, got " + empty_cells, empty_cells >= 10);
    assertTrue("expected at most 64 empty cells, got " + empty_cells, empty_cells <= 64);
    assertTrue(puzzle.isValid());
  }

  @Test
  public void generatePuzzle_reflectionalSymmetry_emptyCellsAreMirrored() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.MEDIUM, new ReflectionalSymmetry(ISudokuGrid.DEFAULT_SIZE));

    ISudokuGrid puzzle = generator.generatePuzzle(false);
    int size = puzzle.getSize();

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (puzzle.getValue(row, col) == -1) {
          // Every empty cell's vertical mirror must also be empty.
          assertEquals(-1, puzzle.getValue(row, size - 1 - col));
        }
      }
    }
  }

  @Test
  public void generatePuzzle_rotationalSymmetry_emptyCellsHaveRotationalCounterpart() {
    SudokuGenerator generator =
        new SudokuGenerator(PuzzleDifficulty.MEDIUM, new RotationalSymmetry(ISudokuGrid.DEFAULT_SIZE));

    ISudokuGrid puzzle = generator.generatePuzzle(false);
    int size = puzzle.getSize();

    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        if (puzzle.getValue(row, col) == -1) {
          // Every empty cell's 180-degree rotational counterpart must also be empty.
          assertEquals(-1, puzzle.getValue(size - 1 - row, size - 1 - col));
        }
      }
    }
  }

  private static int countEmptyCells(ISudokuGrid grid) {
    int count = 0;
    for (int row = 0; row < grid.getSize(); row++) {
      for (int col = 0; col < grid.getSize(); col++) {
        if (grid.getValue(row, col) == -1) {
          count++;
        }
      }
    }
    return count;
  }

}
