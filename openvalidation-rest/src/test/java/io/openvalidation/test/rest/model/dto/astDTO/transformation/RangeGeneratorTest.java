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
  public void RangeGenerator_generate_with_not_empty_documentSection_expect_null() {
    List<String> constructorInputLines = new ArrayList<>(Collections.singleton("1234567890"));
    DocumentSection constructorInput =
        new DocumentSection(new Range(0, 0, 0, 10), constructorInputLines, null);
    RangeGenerator generator = new RangeGenerator(constructorInput);

    List<String> expectedLines = new ArrayList<>(Collections.singleton("2345678"));
    DocumentSection expected = new DocumentSection(new Range(0, 1, 0, 8), expectedLines, null);
    DocumentSection actual = generator.generate("2345678");

    assertThat(actual, is(expected));
  }
}
