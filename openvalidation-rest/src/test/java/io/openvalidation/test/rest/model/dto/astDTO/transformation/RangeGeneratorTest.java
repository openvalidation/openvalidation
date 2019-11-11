package io.openvalidation.test.rest.model.dto.astDTO.transformation;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.RangeGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RangeGeneratorTest {

  @Test
  public void RangeGenerator_generate_with_string_null_expect_null() {
    RangeGenerator generator = new RangeGenerator((String) null);

    DocumentSection expected = null;
    DocumentSection actual = generator.generate((String) null);

    assertThat(actual, is(expected));
  }

  @Test
  public void RangeGenerator_generate_with_documentSection_null_expect_null() {
    RangeGenerator generator = new RangeGenerator((DocumentSection) null);

    DocumentSection expected = null;
    DocumentSection actual = generator.generate((String) null);

    assertThat(actual, is(expected));
  }

  @Test
  public void RangeGenerator_generate_with_empty_documentSection_expect_null() {
    DocumentSection input = new DocumentSection(null, null, null);
    RangeGenerator generator = new RangeGenerator(input);

    DocumentSection expected = null;
    DocumentSection actual = generator.generate((String) null);

    assertThat(actual, is(expected));
  }

  @Test
  public void
      RangeGenerator_generate_with_not_empty_documentSection_one_line_expect_correct_string() {
    List<String> constructorInputLines = new ArrayList<>(Collections.singleton("1234567890"));
    DocumentSection constructorInput =
        new DocumentSection(new Range(0, 0, 0, 10), constructorInputLines, null);
    RangeGenerator generator = new RangeGenerator(constructorInput);

    List<String> expectedLines = new ArrayList<>(Collections.singleton("2345678"));
    DocumentSection expected = new DocumentSection(new Range(0, 1, 0, 8), expectedLines, null);
    DocumentSection actual = generator.generate("2345678");

    assertThat(actual, is(expected));
  }

  @Test
  public void
      RangeGenerator_generate_with_not_empty_documentSection_two_lines_expect_correct_string() {
    List<String> constructorInputLines =
        new ArrayList<>(Collections.singleton("If the age is less than 18"));

    String secondLine = "Then error!";
    constructorInputLines.add(secondLine);

    DocumentSection constructorInput =
        new DocumentSection(new Range(0, 0, 1, secondLine.length()), constructorInputLines, null);
    RangeGenerator generator = new RangeGenerator(constructorInput);

    List<String> expectedLines = new ArrayList<>(Collections.singleton("error"));
    DocumentSection expected = new DocumentSection(new Range(1, 5, 1, 10), expectedLines, null);
    DocumentSection actual = generator.generate("error");

    assertThat(actual, is(expected));
  }

  @Test
  public void RangeGenerator_generate_with_connectedOperation_expect_first_operation() {
    List<String> constructorInputLines =
        new ArrayList<>(Collections.singleton("If the age is less than 18"));

    String secondLine = "and the name is klaus";
    constructorInputLines.add(secondLine);

    String thirdLine = "or the name is klaus";
    constructorInputLines.add(thirdLine);

    String lastLine = "Dann fehler!";
    constructorInputLines.add(lastLine);

    DocumentSection constructorInput =
        new DocumentSection(new Range(0, 0, 1, lastLine.length()), constructorInputLines, null);
    RangeGenerator generator = new RangeGenerator(constructorInput);

    List<String> expectedLines = new ArrayList<>(Collections.singleton("or the name is klaus"));
    DocumentSection expected = new DocumentSection(new Range(2, 0, 2, 20), expectedLines, null);
    DocumentSection actual = generator.generate("or the name is klaus");

    assertThat(actual, is(expected));
  }

  //  @Test
  //  public void
  // RangeGenerator_generate_with_connectedOperation_with_same_operations_expect_second_operation()
  // {
  //    List<String> constructorInputLines = new ArrayList<>(Collections.singleton("If the age is
  // less than 18"));
  //
  //    String secondLine = "and the name is klaus";
  //    constructorInputLines.add(secondLine);
  //
  //
  //    String thirdLine = "and the name is klaus";
  //    constructorInputLines.add(thirdLine);
  //
  //
  //    String lastLine = "Dann fehler!";
  //    constructorInputLines.add(lastLine);
  //
  //    DocumentSection constructorInput =
  //            new DocumentSection(new Range(0, 0, 1, lastLine.length()), constructorInputLines,
  // null);
  //    RangeGenerator generator = new RangeGenerator(constructorInput);
  //
  //    List<String> expectedLines = new ArrayList<>(Collections.singleton("or the name is klaus"));
  //    DocumentSection expected = new DocumentSection(new Range(2, 0, 2, 21), expectedLines, null);
  //    DocumentSection actual = generator.generate("or the name is klaus");
  //
  //    assertThat(actual, is(expected));
  //  }
}
