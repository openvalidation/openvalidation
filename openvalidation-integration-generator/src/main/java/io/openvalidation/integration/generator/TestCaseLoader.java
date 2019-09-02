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

package io.openvalidation.integration.generator;

import static io.openvalidation.common.utils.RegExUtils.PARAGRAPH_REGEX;

import io.openvalidation.common.utils.FileSystemUtils;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.common.utils.RegExUtils;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.integration.generator.model.IntegrationTest;
import io.openvalidation.integration.generator.model.IntegrationTestExecution;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TestCaseLoader {

  // todo jgeske definition content cannot have more than one '-' character in rules, this is an
  // issue with this regex approach, and will not lead to parsing error in regular execution with
  // openvalidation-CLI
  private static String TEST_DEFINITION_SEPARATOR = "[#]+([a-zA-Z0-9\\.üöäß_ ]+)[#]+";
  private static String TEST_DEFINITION_CONTENT =
      "([a-zA-Z.\\n_:\\{\\}\\[\\]'0-9, ()*\\/\\+]+-?[a-zA-Z.\\n_:\\{\\}\\[\\]'0-9, ()*\\/\\+]+)[-]{2,}([a-zA-Z0-9\\.üöäß_ ]+)[-]{2,}";
  private static String TEST_SCHEMA = "([\\{][a-zA-Z0-9:',\\[\\]_\\{\\}\"\\n\\.\\- ]+[\\}])";
  private static String TEST_EXECUTION_SEPARATOR = "[-]+([a-zA-Z0-9\\.üöäß_ ]+)[-]+";

  public static List<IntegrationTest> loadAllTests(String testDirectory) throws IOException {
    return FileSystemUtils.getFilesFromDirectory(testDirectory, "test", true).stream()
        .flatMap(
            p -> {
              try {
                return loadTests(p.toString()).stream();
              } catch (IOException e) {
                e.printStackTrace();
              }
              return null;
            })
        .collect(Collectors.toList());
  }

  public static List<IntegrationTest> loadTests(String path) throws IOException {
    String content = FileSystemUtils.readFile(path).trim();

    List<IntegrationTest> tests = new ArrayList<>();
    List<String> rawTests = StringUtils.splitAndRemoveEmpty(content, TEST_DEFINITION_SEPARATOR);

    AtomicInteger x = new AtomicInteger();

    RegExUtils.each(
        TEST_DEFINITION_SEPARATOR,
        content,
        (matcher) -> {
          IntegrationTest test = new IntegrationTest(matcher.group(1));
          test.setTestFile(path);

          if (!test.getTestName().contains("IGNORE")) {
            String allContent = rawTests.get(x.getAndIncrement());

            List<String> allSplit =
                StringUtils.splitAndRemoveEmpty(allContent, TEST_EXECUTION_SEPARATOR);

            String definitionContent =
                null; // RegExUtils.firstGroup(TEST_DEFINITION_CONTENT, allContent.trim());
            if (allSplit != null && allSplit.size() > 1) {
              definitionContent = allSplit.get(0);
            }

            // String definitionContent = RegExUtils.firstGroup(TEST_DEFINITION_CONTENT,
            // allContent.trim());

            if (StringUtils.isNullOrEmpty(definitionContent)) definitionContent = allContent;

            String schema = RegExUtils.firstGroup(TEST_SCHEMA, definitionContent);
            String rule = definitionContent;

            if (!StringUtils.isNullOrEmpty(schema)) {
              test.setSchema(schema.trim());
              rule = rule.replace(schema, "").trim();
            }
            test.setRule(rule.trim());

            // parse executions
            test.setTestExecution(
                parseExecutions(allContent.trim(), definitionContent.trim(), test));

            tests.add(test);
          }
        });

    return tests;
  }

  public static List<IntegrationTestExecution> parseExecutions(
      String content, String definitionContent, IntegrationTest definition) {
    List<IntegrationTestExecution> tests = new ArrayList<>();
    if (!definitionContent.equals(content)) content = content.replace(definitionContent, "");

    List<String> rawTests = StringUtils.splitAndRemoveEmpty(content, TEST_EXECUTION_SEPARATOR);
    AtomicInteger x = new AtomicInteger();

    RegExUtils.each(
        TEST_EXECUTION_SEPARATOR,
        content,
        (matcher) -> {
          String allContent = rawTests.get(x.getAndIncrement());
          String executionName = matcher.group(1);

          // split multiple {jsondata} within single execution
          List<String> paragraphs = StringUtils.splitAndRemoveEmpty(allContent, PARAGRAPH_REGEX);
          if (paragraphs != null && paragraphs.size() > 0) {

            List<IntegrationTestExecution> multitests = new ArrayList<>();

            for (String line : paragraphs) {

              String jsonData = RegExUtils.firstGroup(TEST_SCHEMA, line);
              if (!StringUtils.isNullOrEmpty(jsonData)) {
                String indx = (paragraphs.size() > 1) ? "" + paragraphs.indexOf(line) : "";
                IntegrationTestExecution test = new IntegrationTestExecution(executionName + indx);
                test.setJsonTestData(jsonData.trim());
                multitests.add(test);
              } else multitests.get(multitests.size() - 1).setExpectedErrorMessage(line.trim());
            }

            // set expected message fallback

            IntegrationTestExecution itswithmessage =
                LINQ.findFirst(
                    multitests, f -> !StringUtils.isNullOrEmpty(f.getExpectedErrorMessage()));
            if (itswithmessage != null) {
              multitests.forEach(
                  m -> m.setExpectedErrorMessage(itswithmessage.getExpectedErrorMessage()));
            } else {
              multitests.forEach(
                  t -> {
                    if (StringUtils.isNullOrEmpty(t.getExpectedErrorMessage()))
                      t.setExpectedErrorMessage(definition.getUniqueErrorMessage());
                  });
            }

            tests.addAll(multitests);
          }

          /*
          String jsonData = RegExUtils.firstGroup(TEST_SCHEMA, allContent);
          String expectedMessage = allContent;

          if (!StringUtils.isNullOrEmpty(jsonData) )
              expectedMessage = expectedMessage.replace(jsonData, "");

          test.setJsonTestData(jsonData.trim());
          test.setExpectedErrorMessage(expectedMessage.trim());

          tests.add(test);
          */

        });

    return tests;
  }
}
