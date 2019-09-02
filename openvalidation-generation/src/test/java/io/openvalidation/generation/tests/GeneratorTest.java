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

package io.openvalidation.generation.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.builder.ASTBuilder;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.generation.CodeGenerator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GeneratorTest {

  @Test
  public void test_method() {
    assertThat(true, is(true));
  }

  @Test
  @Disabled
  public void write_complete_javascriptcode() throws Exception {
    String result = CodeGenerator.generate("javascript", "main", Dummies.model(), false);
    System.out.println(result);
  }

  @Test
  @Disabled
  public void write_complete_csharpcode() throws Exception {
    ASTModel model = Dummies.model();
    model.addParam("namespace_of_model", "bag.ov.namespace.model");
    model.addParam("namespace_of_generated_class", "bag.ov.namespace.generated");

    String result = CodeGenerator.generate("csharp", "main", model, false);
    System.out.println(result);
  }

  @Test
  @Disabled
  public void write_complete_csharpcode_for_demo() throws Exception {
    ASTBuilder builder = new ASTBuilder();
    builder
        .createModel()
        .createVariable("Ist18")
        .createValueAsCondition()
        .withLeftOperandAsProperty("Alter")
        .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
        .withRightOperandAsNumber(18)
        .parentVariable()
        .parent()
        .createRule(
            "Gehalt",
            "Sie benötigen mindestens 10 Jahre Berufserfahrung und einen sehr guten Grund, um ein Gehalt von über 100.000€ zu verlangen!")
        .createConditionGroup()
        .createCondition()
        .withLeftOperandAsProperty("Jahresbruttogehalt")
        .withOperator(ASTComparisonOperator.GREATER_THAN)
        .withRightOperandAsNumber(100000)
        .parentGroup()
        .appendCondition(ASTConditionConnector.AND)
        .withLeftOperandAsProperty("Berufserfahrung")
        .withOperator(ASTComparisonOperator.LESS_THAN)
        .withRightOperandAsNumber(10)
        .parentGroup()
        .parent()
        .parent()
        .createRule(
            "Berufserfahrung",
            "Die Berufserfahrung entspricht nicht Ihrem Alter. Die Berufserfahrung begint mit dem 18'ten. Lebensjahr an.")
        .createConditionGroup()
        .createCondition()
        .withLeftOperandAsVariable("Ist18")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsBoolean(true)
        .parentGroup()
        .appendCondition(ASTConditionConnector.AND)
        .createLeftOperandAsArithmeticalOperation()
        .withProperty("Alter")
        .minus(18)
        .parent()
        .withOperator(ASTComparisonOperator.LESS_THAN)
        .withRightOperandAsProperty("Berufserfahrung")
        .parentGroup()
        .parent()
        .parent()
        .createRule(
            "IstNicht18", "Um sich bewerben zu können, müssen Sie mindestes 18 Jahre Alt sein!")
        .createCondition()
        .withLeftOperandAsProperty("Alter")
        .withOperator(ASTComparisonOperator.LESS_THAN)
        .withRightOperandAsNumber(18);

    ASTModel model = builder.getModel();
    model.addParam("available_model_type", "Open_Validation_Demo___Windows.BewerberModel");
    // model.addParam("generated_class_name", "OpenValidationRules2");
    model.addParam("generated_class_namespace", "Open_Validation_Demo___Windows.Generated");

    String result_cs = CodeGenerator.generate("csharp", "main", model, false);
    String result_js = CodeGenerator.generate("javascript", "main", model, false);
    String result_java = CodeGenerator.generate("java", "main", model, false);
    System.out.println(result_cs);
    System.out.println("\n\n\n ######################### \n\n\n");
    System.out.println(result_js);
    System.out.println("\n\n\n ######################### \n\n\n");
    System.out.println(result_java);
  }

  @Test
  @Disabled
  public void generated_namespace_test_csharp() throws Exception {
    ASTModel model = Dummies.model();
    model.addParam("generated_class_namespace", "bag.ov.namespace.generated");
    model.addParam("model_type", "Person");
    model.addParam("model_type_namespace", "bag.ov.namespace.model");

    String result = CodeGenerator.generate("csharp", "main", model, false);

    assertThat(true, Matchers.is(result.contains("namespace bag.ov.namespace.generated")));
    assertThat(true, Matchers.is(result.contains("using bag.ov.namespace.model;")));
    assertThat(true, Matchers.is(result.contains("(Person model) =>")));
  }

  @Test
  @Disabled
  public void generated_namespace_test_java() throws Exception {
    ASTModel model = Dummies.model();
    model.addParam("generated_class_namespace", "bag.ov.namespace.generated");
    model.addParam("model_type", "Person");
    model.addParam("model_type_namespace", "bag.ov.namespace.model");

    String result = CodeGenerator.generate("java", "main", model, false);

    assertThat(true, Matchers.is(result.contains("package bag.ov.namespace.generated;")));
    assertThat(true, Matchers.is(result.contains("import bag.ov.namespace.model;")));
    assertThat(true, Matchers.is(result.contains("(Person model) ->")));
  }
}
