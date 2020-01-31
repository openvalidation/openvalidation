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

package io.openvalidation.integration.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.openvalidation.integration.generator.TestCaseLoader;
import io.openvalidation.integration.generator.model.IntegrationTest;
import io.openvalidation.integration.generator.model.IntegrationTestExecution;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class IntegrationTestsRunner {

  @ParameterizedTest(name = "{0}")
  @MethodSource()
  public void main_integrationtest(
      String name, IntegrationTestExecution execution, IntegrationTest testdefinition)
      throws Exception {

    DebugInfo dbg = new DebugInfo(execution, testdefinition);

    try {
      HUMLFramework.IOpenValidator validator =
          IntegrationTestFactory.createValidator(testdefinition.getMaskedTestName());
      Object model =
          IntegrationTestFactory.createModel(
              execution.getJsonTestData(), testdefinition.getMaskedTestName());

      HUMLFramework.OpenValidationSummary summary =
          validator.validate(model); // validator.validate(model);

      assertThat(summary, notNullValue());
      assertThat(dbg.msg("NO VALIDATION ERROR MESSAGE RECEIVED"), summary.hasErrors(), is(true));
      assertThat(
          dbg.msg("ERROR MESSAGES SHOULD NOT BE EMPTY"), summary.getErrors(), notNullValue());
      assertThat(
          dbg.msg("ERROR MESSAGES SHOULD CONTAINS AT LEAST 1 MESSAGE"),
          summary.getErrors().length,
          greaterThan(0));

      assertThat(
          dbg.msg("FIRST ERROR MESSAGE SHOULD NOT BE NULL"),
          summary.getErrors()[0],
          notNullValue());
      assertThat(
          dbg.msg("ACTUAL ERROR MESSAGE UNEQUALS THE EXPECTED ONE"),
          summary.getErrors()[0].getError(),
          is(execution.getExpectedErrorMessage()));
    } catch (Exception exp) {
      dbg.exception(exp);
      assertThat(dbg.msg("EXCEPTION WHILE EXECUTING TEST"), false);
    }
  }

  // todo 09.08.19 jgeske use intelligent fallback. this is null with most default configurations
  private static String getTestDirectory() {

    URL resource = IntegrationTest.class.getClassLoader().getResource("root.file");
    String filePath = resource.getFile();
    String path = (new File(filePath)).getParent().replaceAll("%2520", "%20");

    return path;
  }

  private static Stream<Arguments> main_integrationtest() throws Exception {
    List<IntegrationTest> testdefinitions = TestCaseLoader.loadAllTests(getTestDirectory());

    List<Arguments> alltests = new ArrayList<>();

    testdefinitions.forEach(
        d -> {
          d.getTestExecution()
              .forEach(
                  e -> {
                    alltests.add(Arguments.of(d.getTestName() + " - " + e.getTestName(), e, d));
                  });
        });

    return alltests.stream();
  }
}
