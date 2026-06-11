package sudoku;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import sudoku.generation.SudokuGeneratorTest;
import sudoku.generation.symmetry.DihedralSymmetryTest;
import sudoku.generation.symmetry.NoSymmetryTest;
import sudoku.generation.symmetry.ReflectionalSymmetryTest;
import sudoku.generation.symmetry.RotationalSymmetryTest;

/**
 * Aggregates every unit test in the project so they can be run in a single pass via
 * {@code java -cp "bin:lib/*" org.junit.runner.JUnitCore sudoku.AllTests}.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    Tuple2Test.class,
    SudokuBoxTest.class,
    SudokuGridStateTest.class,
    SudokuGridTest.class,
    SudokuSolverTest.class,
    SudokuGeneratorTest.class,
    NoSymmetryTest.class,
    RotationalSymmetryTest.class,
    ReflectionalSymmetryTest.class,
    DihedralSymmetryTest.class,
})
public class AllTests {}
