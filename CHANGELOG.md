# Change Log
All notable changes to this project will be documented in this file.
 
The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [1.0.2] - 2026-06-17
### Added
- `jitpack.yml` pinning the JitPack build to OpenJDK 21, so the library can be consumed via JitPack (JitPack's default JDK 8 cannot compile the project's Java 21 records).

## [1.0.1] - 2026-06-17
### Added
- Maven build (`pom.xml`) producing a publishable library JAR plus a sources JAR, with the JUnit test suite and Checkstyle wired into the build lifecycle.
- Maven Wrapper (`mvnw`, `mvnw.cmd`) so the project builds without a pre-installed Maven.
- GitHub Packages distribution management for publishing the artifact.
- GitHub Actions `build` workflow running `./mvnw verify` (compile, test, and Checkstyle) on pushes to `main` and on pull requests.

### Changed
- Replaced the standalone `lint` CI workflow with the `build` workflow above, so tests and style are checked together in a single pass that mirrors the local build.

### Fixed
- Checkstyle configuration: `LineLength` is now a direct child of `Checker` (it was incorrectly nested under `TreeWalker`, which fails on Checkstyle 8+).
- Renamed the `generatePuzzle` parameter `hasUniqueSolution` to `has_unique_solution` to conform to the project's snake_case convention.

## [1.0.0] - 2026-06-12
### Added
- Composite grid model (`ISudokuState`, `SudokuGridState`, `SudokuBox`) supporting variable grid sizes from 4x4 to 16x16.
- `SudokuGrid` public API with cached, dirty-row-invalidated string rendering.
- `SudokuSolver` recursive backtracking solver, including single-solution, all-solutions, and capped-solution search, plus solvability and unique-solution checks.
- `SudokuGenerator` for producing puzzles at five difficulty levels (`VERY_EASY`, `EASY`, `MEDIUM`, `HARD`, `RANDOM`), with optional unique-solution guarantee.
- Cell removal symmetry strategies: `NoSymmetry`, `RotationalSymmetry`, `ReflectionalSymmetry`, and `DihedralSymmetry`.
- Comprehensive JUnit 4 test suite covering all packages.
- Project documentation: README, Javadoc, issue/PR templates, and CI workflows.
