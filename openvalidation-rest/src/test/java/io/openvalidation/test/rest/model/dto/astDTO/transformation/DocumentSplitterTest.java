package io.openvalidation.test.rest.model.dto.astDTO.transformation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.rest.model.dto.astDTO.Range;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSection;
import io.openvalidation.rest.model.dto.astDTO.transformation.DocumentSplitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DocumentSplitterTest {

  @Test
  public void splitDocument_with_null_expect_empty_list() {
    DocumentSplitter splitter = new DocumentSplitter(null);

    List<DocumentSection> expected = new ArrayList<>();
    List<DocumentSection> actual = splitter.splitDocument();

    assertThat(actual, is(expected));
  }

  @Test
  public void splitDocument_with_one_line_expect_empty_list() {
    DocumentSplitter splitter = new DocumentSplitter("Das ist ein Test");

    List<DocumentSection> expected = new ArrayList<>();
    DocumentSection section =
        new DocumentSection(
            new Range(0, 0, 0, 16),
            new ArrayList<String>(Collections.singleton("Das ist ein Test")) , null);
    expected.add(section);

    List<DocumentSection> actual = splitter.splitDocument();

    assertThat(actual, is(expected));
  }

  @Test
  public void splitDocument_with_two_sections_expect_two_section() {
    DocumentSplitter splitter = new DocumentSplitter("Das ist ein Test\nBlabla\n\nBlabla");

    List<DocumentSection> expected = new ArrayList<>();

    ArrayList<String> firstLines = new ArrayList<>();
    firstLines.add("Das ist ein Test");
    firstLines.add("Blabla");
    DocumentSection firstSection = new DocumentSection(new Range(0, 0, 1, 6), firstLines, null);
    expected.add(firstSection);

    DocumentSection secondSection =
        new DocumentSection(
            new Range(3, 0, 3, 6), new ArrayList<>(Collections.singleton("Blabla")), null);
    expected.add(secondSection);

    List<DocumentSection> actual = splitter.splitDocument();

    assertThat(actual, is(expected));
  }
}
