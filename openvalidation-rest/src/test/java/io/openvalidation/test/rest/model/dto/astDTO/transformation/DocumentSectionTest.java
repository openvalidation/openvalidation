package io.openvalidation.test.rest.model.dto.astDTO.transformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DocumentSectionTest {
  @Test
  public void DocumentSection_constructor_only_with_start_line_expect_correct_range() {
    List<String> lineInputs = new ArrayList<>();
    String firstLine = "test line";
    lineInputs.add(firstLine);
    String lastLine = "another test line";
    lineInputs.add(lastLine);

    int startLineInput = 0;
    DocumentSection section = new DocumentSection(startLineInput, lineInputs, null);

    Range expectedRange = new Range(0, 0, 1, lastLine.length());
    Range actualRange = section.getRange();

    assertThat(actualRange, is(expectedRange));
  }

  @Test
  public void
      DocumentSection_constructor_with_one_line_above_expected_lines_expect_correct_range() {
    List<String> lineInputs = new ArrayList<>();
    String firstLine = "test line";
    lineInputs.add(firstLine);
    String lastLine = "another test line";
    lineInputs.add(lastLine);

    int startLineInput = 1;
    DocumentSection section = new DocumentSection(startLineInput, lineInputs, null);

    Range expectedRange = new Range(1, 0, 2, lastLine.length());
    Range actualRange = section.getRange();

    assertThat(actualRange, is(expectedRange));
  }

  @Test
  public void DocumentSection_trimLine_nothing_to_trim_expect_no_change() {
    List<String> lineInputs = new ArrayList<>();
    String firstLine = "test line";
    lineInputs.add(firstLine);

    Range rangeInput = new Range(0, 0, 0, firstLine.length());
    DocumentSection section = new DocumentSection(rangeInput, lineInputs, null).trimLine();

    Range expectedRange = new Range(0, 0, 0, firstLine.length());
    Range actualRange = section.getRange();

    assertThat(actualRange, is(expectedRange));
  }

  @Test
  public void DocumentSection_trimLine_with_spaces_at_start_and_end_expect_no_change() {
    List<String> lineInputs = new ArrayList<>();
    String firstLine = "  test line  ";
    lineInputs.add(firstLine);

    Range rangeInput = new Range(0, 0, 0, firstLine.length());
    DocumentSection section = new DocumentSection(rangeInput, lineInputs, null).trimLine();

    Range expectedRange = new Range(0, "  ".length(), 0, firstLine.length() - "  ".length());
    Range actualRange = section.getRange();

    assertThat(actualRange, is(expectedRange));
  }
}
