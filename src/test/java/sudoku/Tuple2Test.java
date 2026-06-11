package sudoku;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Tests for {@link Tuple2}. */
public class Tuple2Test {

  @Test
  public void accessors_returnConstructorValues() {
    Tuple2<Integer, String> tuple = new Tuple2<>(3, "rows");

    assertEquals(Integer.valueOf(3), tuple.first());
    assertEquals("rows", tuple.second());
  }

  @Test
  public void equals_sameValues_returnsTrue() {
    Tuple2<Integer, Integer> a = new Tuple2<>(2, 3);
    Tuple2<Integer, Integer> b = new Tuple2<>(2, 3);

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());
  }

  @Test
  public void equals_differentValues_returnsFalse() {
    Tuple2<Integer, Integer> a = new Tuple2<>(2, 3);
    Tuple2<Integer, Integer> b = new Tuple2<>(3, 2);

    assertNotEquals(a, b);
  }

  @Test
  public void toString_containsBothValues() {
    Tuple2<Integer, String> tuple = new Tuple2<>(3, "rows");

    String representation = tuple.toString();

    assertTrue(representation.contains("3"));
    assertTrue(representation.contains("rows"));
  }

  @Test
  public void nestedTuples_accessorsAndEqualityWork() {
    Tuple2<Tuple2<Integer, Integer>, String> outer_a =
        new Tuple2<>(new Tuple2<>(1, 2), "label");
    Tuple2<Tuple2<Integer, Integer>, String> outer_b =
        new Tuple2<>(new Tuple2<>(1, 2), "label");

    assertEquals(new Tuple2<>(1, 2), outer_a.first());
    assertEquals(outer_a, outer_b);
  }

  @Test
  public void supportsNullValues() {
    Tuple2<String, String> tuple = new Tuple2<>(null, "second");

    assertEquals(null, tuple.first());
    assertEquals("second", tuple.second());
    assertEquals(new Tuple2<>(null, "second"), tuple);
  }

}
