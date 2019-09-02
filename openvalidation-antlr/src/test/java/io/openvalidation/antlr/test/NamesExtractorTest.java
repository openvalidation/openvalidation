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

package io.openvalidation.antlr.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.openvalidation.antlr.test.util.NamesExtractorParser;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataVariableReference;
import io.openvalidation.common.utils.GrammarBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

class NamesExtractorTest {
  @Test
  void should_find_name_in_VariableContext() {
    String input = GrammarBuilder.create().with("Bruce Wayne").AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.size(), equalTo(1));
    assertThat(variableReferences.get(0).getName(), is("Batman"));
  }

  @Test
  void should_find_specific_name_in_VariableContext() {
    String input = GrammarBuilder.create().with("Bruce Wayne").AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.get(0).getName(), equalTo("Batman"));
  }

  @Test
  void should_find_DataVariableReference_with_type_unknown_in_VariableContext() {
    String input = GrammarBuilder.create().with("Bruce Wayne").AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.get(0).getType(), equalTo(DataPropertyType.Unknown));
  }

  @Test
  void should_find_DataVariableReference_with_type_decimal_in_VariableContext() {
    String input = GrammarBuilder.create().with("Bruce").ADD("Wayne").AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.get(0).getType(), equalTo(DataPropertyType.Decimal));
    assertThat(variableReferences.get(0).getName(), is("Batman"));
  }

  @Test
  void
      should_find_DataVariableReference_with_type_boolean_in_VariableContext_with_single_condition() {
    String input = GrammarBuilder.create().EQ("Bruce", "Wayne").AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.get(0).getType(), equalTo(DataPropertyType.Boolean));
    assertThat(variableReferences.get(0).getName(), is("Batman"));
  }

  @Test
  void
      should_find_DataVariableReference_with_type_boolean_in_VariableContext_with_conditiongroup() {
    String input =
        GrammarBuilder.create().EQ("Bruce", "Wayne").AND().EQ("Bat", "Man").AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.get(0).getType(), equalTo(DataPropertyType.Boolean));
    assertThat(variableReferences.get(0).getName(), is("Batman"));
  }

  @Test
  void should_find_DataVariableReference_with_type_unknown_in_VariableContext_without_expression() {
    String input = GrammarBuilder.create().AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.get(0).getType(), equalTo(DataPropertyType.Unknown));
    assertThat(variableReferences.get(0).getName(), is("Batman"));
  }

  @Test
  void
      should_find_DataVariableReference_with_type_unknown_in_VariableContext_with_empty_expression() {
    String input = GrammarBuilder.create().with("  ").AS("Batman").getText();

    List<DataVariableReference> variableReferences = NamesExtractorParser.parse(input);

    assertThat(variableReferences.get(0).getType(), equalTo(DataPropertyType.Unknown));
    assertThat(variableReferences.get(0).getName(), is("Batman"));
  }
}
