package sudoku;

/**
 * Represents a tuple of two elements. Tuples are immutable and therefore the values of the objects
 * cannot be changed after instantiation.
 */
public record Tuple2<T, U>(T first, U second) {}
