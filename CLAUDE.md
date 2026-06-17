# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

This is a Maven project (`pom.xml`, coordinates `io.github.jamieramsell:sudoku`) following the standard layout: production source in `src/main/java/`, tests in `src/test/java/` (both mirroring the `sudoku` package structure). Maven build output goes to `target/`. Use the Maven Wrapper (`./mvnw`) so no global Maven install is required; dependencies (JUnit 4.13.2 + transitive Hamcrest) are resolved from Maven Central.

**Compile, test, lint, and package (builds `target/sudoku-<version>.jar` + sources jar):**
```bash
./mvnw clean verify
```

**Run all tests** (JUnit 4 via Surefire — discovers `*Test` classes directly; the `AllTests` suite is excluded to avoid double-running):
```bash
./mvnw test
```

**Run a single test class:**
```bash
./mvnw test -Dtest=<TestClassName>
```

**Run Checkstyle only** (uses `checkstyle.xml`, the project's snake_case rules; bound to the `verify` phase):
```bash
./mvnw checkstyle:check
```

**Publish the library JAR to GitHub Packages:**
```bash
./mvnw deploy
```

**Regenerate Javadoc:**
```bash
javadoc -d docs -sourcepath src/main/java -subpackages sudoku
```

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
