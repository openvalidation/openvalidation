/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.model.ContentOptionKind;
import io.openvalidation.common.utils.FileSystemUtils;
import io.openvalidation.common.utils.OSUtils;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FileSystemUtilsTest {

  @ParameterizedTest
  @ValueSource(
      strings = {
        "c:\\temp\\halo.test",
        "/test/test.hallo",
        "test/test.hallo",
        "test\\test.hallo",
        "\\test\\test.hallo",
        "file:///~/calendar"
      })
  public void should_be_a_local_file(String file) {
    assertThat(FileSystemUtils.isLocalFile(file), is(true));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "http://temp/halo.test",
        "ftp://temp/halo.test",
        "test/test",
        "test\\test",
      })
  public void should_not_be_a_local_file(String file) {
    assertThat(FileSystemUtils.isLocalFile(file), is(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"http://temp/halo.test", "ftp://temp/halo.test"})
  public void should_be_url(String file) {
    assertThat(FileSystemUtils.isURL(file), is(true));
  }

  @ParameterizedTest
  @ValueSource(strings = {"file:///temp/halo.test", "temp/halo.test"})
  public void should_not_be_url(String file) {
    assertThat(FileSystemUtils.isURL(file), is(false));
  }

  @Test
  public void should_be_right_os() {
    boolean expected = File.separatorChar == '/';
    assertThat(OSUtils.isUnix(), is(expected));
  }

  @DisplayName("Content Kind")
  @ParameterizedTest(name = "\"{0}\" should be {1}")
  @CsvSource({
    "file:///hallo/test.hall, FilePath",
    "temp/halo.test, FilePath",
    "http://www.test.de/file.ext, URL",
    "test, Content",
  })
  public void should_not_be_right_content_kind(String content, ContentOptionKind expectedKind) {
    assertThat(FileSystemUtils.getContentKind(content), is(expectedKind));
  }

  // todo 20190301 create Tests for Content Type Detection
}
