# sudoku

> A Java library for representing, solving, and generating Sudoku puzzles of variable grid sizes.

[![Build](https://github.com/jamieramsell/sudoku/actions/workflows/build.yml/badge.svg)](https://github.com/jamieramsell/sudoku/actions/workflows/build.yml)
[![Java](https://img.shields.io/badge/java-21+-orange.svg)](https://openjdk.org)
[![JUnit](https://img.shields.io/badge/tests-JUnit%204-brightgreen.svg)](https://junit.org/junit4/)
[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

---

## Overview

This project models Sudoku grids as a composite of states, with solving and generation built on top. It supports any grid size `x` where `2 ≤ x ≤ 16` and `x` is even, square, or both (e.g. 4, 6, 9, 12, 16), with box dimensions computed automatically as the most square-like factor pair.

The solver uses recursive backtracking and can find a single solution, check for uniqueness, or enumerate all solutions. The generator builds a full valid grid and removes cells (optionally in a symmetric pattern) while preserving a configurable difficulty and, if required, a unique solution.

---

## Features

- **Variable grid sizes** — any valid size from 4x4 up to 16x16
- **Composite grid model** — `SudokuGridState` (top-level grid) composed of `SudokuBox` regions, both implementing a shared `ISudokuState` interface
- **Backtracking solver** (`SudokuSolver`) — solve for one solution, all solutions, or up to a capped number; check solvability and solution uniqueness
- **Puzzle generation** (`SudokuGenerator`) — generate puzzles at five difficulty levels (`VERY_EASY`, `EASY`, `MEDIUM`, `HARD`, `RANDOM`), with or without a unique-solution guarantee
- **Cell removal symmetry** — `NoSymmetry`, `RotationalSymmetry`, `ReflectionalSymmetry`, and `DihedralSymmetry` strategies for how cells are removed during generation
- **String rendering** — `SudokuGrid` provides cached, dirty-row-invalidated string output of the grid

---

## Architecture

| Component | Responsibility |
|---|---|
| `ISudokuState` | Shared interface for grid/box state: `getValue`, `setValue`, `getGridDimensions`, `checkForDuplicates`, `clone` |
| `SudokuGridState` | Top-level grid which holds a 2D list of `SudokuBox` instances |
| `SudokuBox` | A single box/region; leaf node of the composite, also implements `ISudokuState` |
| `ISudokuGrid` | Extends `ISudokuState` with `isValid`, `isSolved`, `resetGrid`, `getSize` |
| `SudokuGrid` | Public-facing grid class; wraps `SudokuGridState` and adds string rendering |
| `ISudokuSolver` / `SudokuSolver` | Recursive backtracking solver, plus static validation helpers |
| `ISudokuGenerator` / `SudokuGenerator` | Generates puzzles given a `PuzzleDifficulty` and an `AbstractSymmetry` |
| `Tuple2<A, B>` | Generic pair, used throughout for grid/box dimensions |

Cell coordinates are `(row, column)`, indexed from `(0, 0)` at the upper-left. Empty cells are represented as `-1`.

---

## Project Structure

```
sudoku/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── sudoku/
│   │           ├── ISudokuGrid.java
│   │           ├── ISudokuSolver.java
│   │           ├── ISudokuState.java
│   │           ├── SudokuBox.java
│   │           ├── SudokuGrid.java
│   │           ├── SudokuGridState.java
│   │           ├── SudokuSolver.java
│   │           ├── Tuple2.java
│   │           └── generation/
│   │               ├── ISudokuGenerator.java
│   │               ├── SudokuGenerator.java
│   │               └── symmetry/
│   │                   ├── AbstractSymmetry.java
│   │                   ├── NoSymmetry.java
│   │                   ├── RotationalSymmetry.java
│   │                   ├── ReflectionalSymmetry.java
│   │                   └── DihedralSymmetry.java
│   └── test/
│       └── java/
│           └── sudoku/
│               ├── AllTests.java
│               ├── SudokuBoxTest.java
│               ├── SudokuGridStateTest.java
│               ├── SudokuGridTest.java
│               ├── SudokuSolverTest.java
│               ├── Tuple2Test.java
│               └── generation/
│                   ├── SudokuGeneratorTest.java
│                   └── symmetry/
│                       ├── DihedralSymmetryTest.java
│                       ├── NoSymmetryTest.java
│                       ├── ReflectionalSymmetryTest.java
│                       └── RotationalSymmetryTest.java
│
├── pom.xml          # Maven build (coordinates io.github.jamieramsell:sudoku)
├── mvnw             # Maven Wrapper (no global Maven install required)
├── mvnw.cmd
├── .mvn/
├── checkstyle.xml   # snake_case style rules, enforced by the build
├── docs/            # generated Javadoc
├── .github/         # CI workflow and issue/PR templates
└── README.md
```

---

## Getting Started

### Prerequisites

- JDK 21+
- No Maven install needed — use the bundled Maven Wrapper (`./mvnw`). Dependencies (JUnit 4 + Hamcrest) are resolved from Maven Central.

### Build, test, and lint

`verify` compiles the sources, runs the JUnit suite, and runs Checkstyle in one pass, producing `target/sudoku-<version>.jar` and a sources jar:

```bash
./mvnw clean verify
```

### Run all tests

```bash
./mvnw test
```

### Run a single test class

```bash
./mvnw test -Dtest=<TestClassName>
```

### Use as a dependency

The library is published to GitHub Packages. After [authenticating to GitHub Packages](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry), declare:

```xml
<dependency>
  <groupId>io.github.jamieramsell</groupId>
  <artifactId>sudoku</artifactId>
  <version>1.0.1</version>
</dependency>
```

### Regenerate Javadoc

```bash
javadoc -d docs -sourcepath src/main/java -subpackages sudoku
```

---

## Contributing

Contributions are welcome. Please open an issue before submitting a pull request so the proposed change can be discussed first.

1. Fork the repository
2. Create a feature branch (`git checkout -b feat/your-feature`)
3. Commit your changes (`git commit -m 'feat: add your feature'`)
4. Push to the branch (`git push origin feat/your-feature`)
5. Open a pull request

Commit messages should follow the [Conventional Commits](https://www.conventionalcommits.org/) specification.

---

## License

This project is licensed under the [MIT License](LICENSE).
