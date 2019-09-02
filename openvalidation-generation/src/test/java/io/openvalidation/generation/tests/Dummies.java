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

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.builder.ASTBuilder;
import io.openvalidation.common.ast.builder.ASTRuleBuilder;
import io.openvalidation.common.ast.builder.ASTVariableBuilder;
import io.openvalidation.common.ast.condition.ASTConditionConnector;

public class Dummies {

  public static ASTModel model() throws Exception {
    ASTBuilder builder = new ASTBuilder();
    builder.createModel().createComment("DAS IST MEIN KOMMENTAR", "MULTILINE");

    ASTModel model = builder.getModel();

    model.add(simple());
    model.add(simpleConditionGroup());
    model.add(complexConditionGroup());
    model.add(simpleConditionWithOperatorFunction());

    model.add(staticVariable());
    model.add(propertyVariable());
    model.add(conditionGroupWithVariableOperand());

    model.add(simpleArithmeticalOperation());
    model.add(arithmeticalOperationWithPropertyAndGroup());

    // model.add(simpleConstraintProperty());

    return model;
  }

  public static ASTRule simple() throws Exception {
    ASTBuilder builder = new ASTBuilder();
    builder
        .createModel()
        .createRule("MyRuleName", "Hallo dies ist eine ErrorMessage!")
        .createCondition()
        .withLeftOperandAsProperty("Versicherungsnehmer", "Alter")
        .withOperator(ASTComparisonOperator.LESS_THAN)
        .withRightOperandAsNumber(18);

    return builder.getFirstRule();
  }

  public static ASTRule simpleConditionGroup() throws Exception {
    ASTRuleBuilder builder = new ASTRuleBuilder();
    builder
        .createRule(
            "AsConditionGroup",
            "Hallo dies ist eine ErrorMessage von einer Rule mit ConditionGroup!")
        .createConditionGroup()
        .createCondition()
        .withLeftOperandAsProperty("Versicherungsnehmer", "Alter")
        .withOperator(ASTComparisonOperator.LESS_THAN)
        .withRightOperandAsNumber(18)
        .parentGroup()
        .appendCondition(ASTConditionConnector.AND)
        .withLeftOperandAsString("Jimmy")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsProperty("Versicherungsnehmer", "Name");

    return builder.getModel();
  }

  public static ASTRule complexConditionGroup() throws Exception {
    ASTRuleBuilder builder = new ASTRuleBuilder();
    builder
        .createRule(
            "AsConditionGroupComplex",
            "Hallo dies ist eine ErrorMessage von einer Rule mit einer Komplexen verschachtelten Conditions!")
        .createConditionGroup()
        .createCondition()
        .withLeftOperandAsProperty("Versicherungsnehmer", "Alter")
        .withOperator(ASTComparisonOperator.LESS_THAN)
        .withRightOperandAsNumber(18)
        .parentGroup()
        .appendConditionGroup(ASTConditionConnector.AND)
        .createCondition()
        .withLeftOperandAsString("Jimmy")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsProperty("Versicherungsnehmer", "Name")
        .parentGroup()
        .appendCondition(ASTConditionConnector.OR)
        .withLeftOperandAsProperty("Wohnort")
        .withOperator(ASTComparisonOperator.NOT_EQUALS)
        .withRightOperandAsString("Dortmund");

    return builder.getModel();
  }

  public static ASTRule simpleConditionWithOperatorFunction() throws Exception {
    ASTRuleBuilder builder = new ASTRuleBuilder();
    builder
        .createRule("MyRuleConditionOperator", "Hallo dies ist eine ErrorMessage!")
        .createCondition()
        .withLeftOperandAsProperty("Versicherungsnehmer", "Alter")
        .withOperator(ASTComparisonOperator.ONE_OF)
        .withRightOperandAsArray(1, 2, 3);

    //               .createConditionFunction(ASTComparisonOperator.ONE_OF.name())
    //                    .withOperandAsProperty("Versicherungsnehmer", "Alter")
    //                    .withParametersAsNumberArray(1,2,3);

    return builder.getModel();
  }

  public static ASTVariable staticVariable() throws Exception {
    ASTVariableBuilder builder = new ASTVariableBuilder();
    builder.createVariable("Adresse").withValueAsString("Pierbusch 17, 44536 Lünen");

    return builder.getModel();
  }

  public static ASTVariable propertyVariable() throws Exception {
    ASTVariableBuilder builder = new ASTVariableBuilder();
    builder.createVariable("Adresse2").withValueAsProperty("Antrag", "Person", "Adresse");

    return builder.getModel();
  }

  public static ASTRule conditionGroupWithVariableOperand() throws Exception {
    ASTRuleBuilder builder = new ASTRuleBuilder();
    builder
        .createRule(
            "AsConditionGroupWithOperandVariable",
            "Hallo dies ist eine ErrorMessage von einer Rule mit Variable Operand!")
        .createConditionGroup()
        .createCondition()
        .withLeftOperandAsProperty("Versicherungsnehmer", "Alter")
        .withOperator(ASTComparisonOperator.LESS_THAN)
        .withRightOperandAsNumber(18)
        .parentGroup()
        .appendCondition(ASTConditionConnector.AND)
        .withLeftOperandAsVariable("Adresse2")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsString("Pierbusch 17, Lünen");

    return builder.getModel();
  }

  public static ASTRule simpleArithmeticalOperation() throws Exception {
    ASTRuleBuilder builder = new ASTRuleBuilder();
    builder
        .create()
        .withName("SimpleArithmeticalRule")
        .createCondition()
        .createLeftOperandAsArithmeticalOperation()
        .withNumber(2)
        .plus(10)
        .minus(5)
        .divide(2)
        .parent()
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsNumber(18);

    return builder.getModel();
  }

  public static ASTRule arithmeticalOperationWithPropertyAndGroup() throws Exception {
    ASTRuleBuilder builder = new ASTRuleBuilder();
    builder
        .create()
        .withName("ComplexArithmeticalRule")
        .createCondition()
        .createLeftOperandAsArithmeticalOperation()
        .withNumber(2)
        .multiplySuboperation()
        .withProperty("Benutzer", "Alter")
        .minus(18)
        .parentOperation()
        .parent()
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsNumber(18);

    return builder.getModel();
  }

  //    public static ASTVariable simpleConstraintProperty() throws Exception {
  //
  //        ASTVariableBuilder builder = new ASTVariableBuilder();
  //        builder.createVariable("Adresse2Rule")
  //                    .createValueAsProperty()
  //                        .appendAccessor("Antrag")
  //                        .appendConstraint("Personen")
  //                            .withLeftOperandAsProperty("Name")
  //                            .withOperator(ASTComparisonOperator.EQUALS)
  //                            .withRightOperandAsString("Jefferson")
  //                        .parentProperty()
  //                            .appendAccessor("Name");
  //        //Antrag.Person _with_ name _equals_ Jefferson.Name _as_ Adresse2Rule
  //        return builder.getModel();
  //    }
}
