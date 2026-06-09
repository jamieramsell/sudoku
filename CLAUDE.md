# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

This is a Java project configured for VS Code with the Java Extension Pack. Compilation output goes to `bin/`, source is in `src/`, and dependencies are in `lib/`.

**Compile:**
```bash
javac -cp "lib/*" -d bin $(find src -name "*.java")
```

**Run all tests** (JUnit 4):
```bash
java -cp "bin:lib/*" org.junit.runner.JUnitCore <TestClassName>
```

**Run a single test:**
```bash
java -cp "bin:lib/*" org.junit.runner.JUnitCore sudoku.<TestClassName>
```

**Regenerate Javadoc:**
```bash
javadoc -d docs -sourcepath src -subpackages sudoku
```

Dependencies: `lib/junit-4.13.2.jar` and `lib/hamcrest-core-1.3.jar`.

## Architecture

The grid is modelled as a two-level composite of `ISudokuState` implementations:

- **`SudokuGridState`** — the top-level grid, holds a 2D list of `SudokuBox` instances (one per box region).
- **`SudokuBox`** — a single box/region, also implements `ISudokuState`. It is the leaf node of the composite.
- **`SudokuGrid`** — the public-facing class. Wraps a `SudokuGridState` and adds string rendering via an internal `StringCache` (lazy, dirty-row invalidation for performance).

`ISudokuState` is the shared interface for both levels, exposing `getValue`, `setValue`, `getGridDimensions`, `checkForDuplicates`, and `clone`. `ISudokuGrid` extends `ISudokuState` and adds higher-level operations (`isValid`, `isSolved`, `resetGrid`, `getSize`).

**Coordinate system:** all cell coordinates are `(row, column)` indexed from `(0, 0)` at the upper-left. Empty cells are represented as `-1`.

**Grid sizes:** valid sizes are integers `x` where `2 ≤ x ≤ 16` and `x` is even, square, or both (e.g. 4, 6, 9, 12, 16). Box dimensions are computed by `SudokuBox.calculateBoxSize(size)` — it finds the most square-like factor pair.

**Solver (`SudokuSolver`):** implements `ISudokuSolver`. Uses recursive backtracking. `solveGrid(int solutions_required)` is the core method; pass `-1` for all solutions or a positive integer to cap. `isSolvable()` and `hasUniqueSolution()` are convenience wrappers. `ISudokuSolver` also contains two static utility methods (`isPlacementValid`, `isGridStateValid`) used during backtracking.

**Generator (`SudokuGenerator`):** takes a `PuzzleDifficulty` (controls how many cells to remove) and an `AbstractSymmetry` (controls which cells are removed symmetrically). It generates a full valid grid, then removes cells while checking for unique solvability. The `sudoku.generation.symmetry` package contains `NoSymmetry`, `RotationalSymmetry`, `ReflectionalSymmetry`, and `DihedralSymmetry`, all extending `AbstractSymmetry`.

**`Tuple2<A, B>`:** a simple generic pair used throughout for dimensions (`rows, cols`).

## Code Style

- Variables and parameters use `snake_case` (e.g. `solutions_required`, `box_dimensions`) — intentionally non-standard for Java; follow this throughout.
- Constants use `SCREAMING_SNAKE_CASE`.
- Prefer package-private visibility where public access isn't required (`ISudokuState`, `SudokuBox`, and `SudokuGridState` are all package-private).
- Javadoc on all public methods. Inline comments used to explain non-obvious logic blocks.
