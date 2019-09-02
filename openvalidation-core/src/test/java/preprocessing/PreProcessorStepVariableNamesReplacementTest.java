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

package preprocessing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.PreProcessorContext;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.core.preprocessing.PreProcessorStepFactory;
import io.openvalidation.core.preprocessing.steps.PreProcessorAliasResolutionStep;
import io.openvalidation.core.preprocessing.steps.PreProcessorVariableNamesStep;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PreProcessorStepVariableNamesReplacementTest {

  @ParameterizedTest
  @CsvSource({
    "test as varname, varname",
    "test as varname1\\n\\ntest as varname2, varname1:varname2",
    "test as varname space\\n\\ntest as varname2 space, varname space: varname2 space",
    "test as varname\\n \\nif as me then you, varname",
    "test as varname space\\n\\nif as me then you\\n\\ntest as varname2 space, varname space:varname2 space",
    "test as varname space\\n\\nshe must be as you\\n\\ntest as varname2 space, varname space:varname2 space",
    "test as varname space\\n\\nshe must not be as you\\n\\ntest as varname2 space, varname space:varname2 space"
  })
  public void collect_variable_names(String rule, String expectation) throws Exception {
    PreProcessorAliasResolutionStep aliasStep =
        PreProcessorStepFactory.createStep(PreProcessorAliasResolutionStep.class, getCtx());
    PreProcessorVariableNamesStep namesStep =
        PreProcessorStepFactory.createStep(PreProcessorVariableNamesStep.class, getCtx());

    String[] expectedNames =
        StringUtils.splitAndRemoveEmpty(expectation, ":").stream()
            .map(n -> n.trim())
            .collect(Collectors.toList())
            .toArray(new String[0]);

    String result = aliasStep.process(rule.replace("\\n", "\n"));
    List<String> names =
        namesStep.parseVariableNames(result).stream()
            .map(n -> n.trim())
            .collect(Collectors.toList());

    assertThat(names, notNullValue());
    assertThat(names, hasSize(expectedNames.length));
    assertThat(names, hasItems(expectedNames));
  }

  @ParameterizedTest
  @CsvSource({
    "test as varname\\n\\ntest as varname, varname",
    "test as varname\\n\\ntest as varname\\n\\ntest as varname2\\n\\ntest as varname2, varname:varname2",
    "test as varname\\n\\ntest as varname1\\n\\ntest as varname2\\n\\ntest as varname2, varname2",
  })
  public void validate_duplicated_variablenames(String rule, String expectation) throws Exception {
    PreProcessorAliasResolutionStep aliasStep =
        PreProcessorStepFactory.createStep(PreProcessorAliasResolutionStep.class, getCtx());
    PreProcessorVariableNamesStep namesStep =
        PreProcessorStepFactory.createStep(PreProcessorVariableNamesStep.class, getCtx());

    String[] expectedNames =
        StringUtils.splitAndRemoveEmpty(expectation, ":").stream()
            .map(n -> n.trim())
            .collect(Collectors.toList())
            .toArray(new String[0]);
    String expectedErrorMessage =
        "duplicate variable names found: " + StringUtils.join(expectedNames, ",");

    Throwable expectedException = null;
    Assertions.assertThrows(
        OpenValidationException.class,
        () -> {
          String result = aliasStep.process(rule.replace("\\n", "\n"));
          String notexpectedresult = namesStep.process(result);

          assertThat(expectedException.getMessage(), is(expectedErrorMessage));
          assertThat(notexpectedresult, nullValue());
        });
  }

  @ParameterizedTest
  @CsvSource({
    "test as propname, , propname -> propname",
    "test as test, test, test -> test.propname",
    "test as test\\n\\nif hallo as test, test, test -> test.propname",
    "test as test2\\n\\na as test3, test2.test3, test2 -> test2.test3.propname:test3 -> test2.test3.propname",
  })
  public void validate_same_names_as_in_scema(String rule, String propertypath, String expectation)
      throws Exception {
    PreProcessorAliasResolutionStep aliasStep =
        PreProcessorStepFactory.createStep(PreProcessorAliasResolutionStep.class, getCtx());
    PreProcessorVariableNamesStep namesStep =
        PreProcessorStepFactory.createStep(PreProcessorVariableNamesStep.class, getCtx());

    DataSchema schema = new DataSchema();
    schema.addProperty("propname", propertypath, DataPropertyType.String);

    namesStep.getContext().setSchema(schema);

    String[] expectedNames =
        StringUtils.splitAndRemoveEmpty(expectation, ":").stream()
            .map(n -> n.trim())
            .collect(Collectors.toList())
            .toArray(new String[0]);
    String expectedErrorMessage =
        "variable names schould not be named as schema attributes: "
            + StringUtils.join(expectedNames, ",");

    Throwable expectedException = null;
    OpenValidationException currentException =
        Assertions.assertThrows(
            OpenValidationException.class,
            () -> {
              String result = aliasStep.process(rule.replace("\\n", "\n"));
              namesStep.process(result);
            },
            "\n\nEXPECTED VALIDATION EXCEPTION WAS NOT THROWN\n\n");

    assertThat(currentException.getMessage(), is(expectedErrorMessage));
  }

  @ParameterizedTest
  @CsvSource({"test as as, as", "test as of, of", "test as sum of \\n\\n test as is, sum of:is"})
  public void validate_same_names_as_keyword(String rule, String expectation) throws Exception {
    PreProcessorAliasResolutionStep aliasStep =
        PreProcessorStepFactory.createStep(PreProcessorAliasResolutionStep.class, getCtx());
    PreProcessorVariableNamesStep namesStep =
        PreProcessorStepFactory.createStep(PreProcessorVariableNamesStep.class, getCtx());

    namesStep.getContext().setSchema(new DataSchema());

    String[] expectedNames =
        StringUtils.splitAndRemoveEmpty(expectation, ":").stream()
            .map(n -> n.trim())
            .collect(Collectors.toList())
            .toArray(new String[0]);
    String expectedErrorMessage =
        "variable names schould not be named as reserved KEYWORD: "
            + StringUtils.join(expectedNames, ",");

    Throwable expectedException = null;
    OpenValidationException currentException =
        Assertions.assertThrows(
            OpenValidationException.class,
            () -> {
              String result = aliasStep.process(rule.replace("\\n", "\n"));
              namesStep.process(result);
            },
            "\n\nEXPECTED VALIDATION EXCEPTION WAS NOT THROWN\n\n");

    assertThat(currentException.getMessage(), is(expectedErrorMessage));
  }

  @ParameterizedTest
  @CsvSource({
    "test as halllo, test ʬasʬas halllo",
    "test as halllo test\\n\\nif halllo test then test, test ʬasʬas halllo test ʬparagraphʬ ʬifʬif halllo test ʬthenʬthen test",
    "test as halllo as\\n\\nif halllo as then test, test ʬasʬas halllo as ʬparagraphʬ ʬifʬif halllo as ʬthenʬthen test",
  })
  public void should_unmask_variablenames_within_rule(String rule, String expectation)
      throws Exception {
    PreProcessorAliasResolutionStep aliasStep =
        PreProcessorStepFactory.createStep(PreProcessorAliasResolutionStep.class, getCtx());
    PreProcessorVariableNamesStep namesStep =
        PreProcessorStepFactory.createStep(PreProcessorVariableNamesStep.class, getCtx());

    namesStep.getContext().setSchema(new DataSchema());

    String result = aliasStep.process(rule.replace("\\n", "\n"));
    result = namesStep.process(result);

    assertThat(result, is(expectation));
  }

  private PreProcessorContext getCtx() {
    PreProcessorContext ctx = new PreProcessorContext();
    ctx.setWorkingDirectory(null);
    ctx.setLocale(new Locale("en"));

    return ctx;
  }

  /*
  @ParameterizedTest
  @ValueSource(strings = {
          "variable",
          "of",
          "sum of",
          "less\nthan",
          "less \n than",
          "as"
  })

  * */
}
