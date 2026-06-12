# Change Log
All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [1.0.0] - 2026-06-12
### Added
- Composite grid model (`ISudokuState`, `SudokuGridState`, `SudokuBox`) supporting variable grid sizes from 4x4 to 16x16.
- `SudokuGrid` public API with cached, dirty-row-invalidated string rendering.
- `SudokuSolver` recursive backtracking solver, including single-solution, all-solutions, and capped-solution search, plus solvability and unique-solution checks.
- `SudokuGenerator` for producing puzzles at five difficulty levels (`VERY_EASY`, `EASY`, `MEDIUM`, `HARD`, `RANDOM`), with optional unique-solution guarantee.
- Cell removal symmetry strategies: `NoSymmetry`, `RotationalSymmetry`, `ReflectionalSymmetry`, and `DihedralSymmetry`.
- Comprehensive JUnit 4 test suite covering all packages.
- Project documentation: README, Javadoc, issue/PR templates, and CI workflows.
