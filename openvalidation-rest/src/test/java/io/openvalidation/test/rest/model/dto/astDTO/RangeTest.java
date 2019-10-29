package io.openvalidation.test.rest.model.dto.astDTO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.rest.model.dto.astDTO.Position;
import io.openvalidation.rest.model.dto.astDTO.Range;
import org.junit.jupiter.api.Test;

public class RangeTest {

  @Test
  public void Range_includesPoition_with_included_position_expected_true() {
    Range range = new Range(0, 0, 0, 10);

    Position input = new Position(0, 2);

    boolean expected = true;
    boolean actual = range.includesPosition(input);

    assertThat(actual, is(expected));
  }

  @Test
  public void Range_includesPoition_with_position_at_wrong_line_expected_true() {
    Range range = new Range(0, 0, 0, 10);

    Position input = new Position(1, 2);

    boolean expected = false;
    boolean actual = range.includesPosition(input);

    assertThat(actual, is(expected));
  }

  @Test
  public void Range_includesPoition_with_position_at_right_column_bound_expected_true() {
    Range range = new Range(0, 0, 0, 10);

    Position input = new Position(0, 10);

    boolean expected = true;
    boolean actual = range.includesPosition(input);

    assertThat(actual, is(expected));
  }

  @Test
  public void Range_includesPoition_with_position_at_left_column_bound_expected_true() {
    Range range = new Range(0, 0, 0, 10);

    Position input = new Position(0, 0);

    boolean expected = true;
    boolean actual = range.includesPosition(input);

    assertThat(actual, is(expected));
  }

  @Test
  public void Range_includesPoition_with_position_at_higher_column_bound_expected_true() {
    Range range = new Range(0, 0, 0, 10);

    Position input = new Position(0, 11);

    boolean expected = false;
    boolean actual = range.includesPosition(input);

    assertThat(actual, is(expected));
  }
}
