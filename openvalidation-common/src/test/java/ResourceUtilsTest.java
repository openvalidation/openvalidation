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

import io.openvalidation.common.utils.ResourceUtils;
import org.junit.jupiter.api.Test;

public class ResourceUtilsTest {

  @Test
  public void get_classpath_and_load_resource_content() {
    String classPath = ResourceUtils.getPath("test.txt");
    String content = ResourceUtils.getResourceContentByClassPath(classPath);

    assertThat(content, is("i'am a resource"));
  }

  @Test
  public void get_classpath_without_resource_name() {
    String path = ResourceUtils.getPathWithoutResourceName("sub/test.txt");

    assertThat(path, is("classpath:sub"));
  }

  @Test
  public void load_resource_from_nested_classpath() {
    String content = ResourceUtils.getResourceContentByClassPath("classpath:sub/test.txt");
    assertThat(content, is("i'am a sub resource"));

    content = ResourceUtils.getResourceContentByClassPath("classpath:/sub/test.txt");
    assertThat(content, is("i'am a sub resource"));
  }

  @Test
  public void load_resource_from_package_fallback() {
    String classPath = ResourceUtils.getPath("_test_.txt");
    String content = ResourceUtils.getResourceContentByClassPath(classPath);
    assertThat(content, is("_i'am a resource_"));
  }

  @Test
  public void load_resource_by_classpath() {
    String classPath = ResourceUtils.getPath("_test_.txt");
    String content = ResourceUtils.getResourceConent(classPath);
    assertThat(content, is("_i'am a resource_"));
  }

  @Test
  public void should_exists() {

    assertThat(ResourceUtils.exists("test.txt"), is(true));
    assertThat(ResourceUtils.exists("/test.txt"), is(true));
    assertThat(ResourceUtils.exists("sub/test.txt"), is(true));

    assertThat(ResourceUtils.exists("classpath:test.txt"), is(true));
    assertThat(ResourceUtils.exists("classpath:sub/test.txt"), is(true));
  }

  @Test
  public void should_not_exists() {
    assertThat(ResourceUtils.exists("atest.txt"), is(false));
    assertThat(ResourceUtils.exists("classpath:atest.txt"), is(false));
  }
}
