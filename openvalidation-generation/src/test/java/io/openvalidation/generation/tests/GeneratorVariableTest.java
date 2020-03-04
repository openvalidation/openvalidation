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
import io.openvalidation.common.ast.builder.ASTModelBuilder;
import io.openvalidation.common.ast.builder.ASTVariableBuilder;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorVariableTest {

  private static Stream<Arguments> variable_with_value_as_condition() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Vollj_e4_hrig = huml.createVariable(\"Vollj_e4_hrig\", function(model) { return huml.GREATER_OR_EQUALS(model.Alter, 18.0); });"),
        Arguments.of(
            "csharp",
            "var Vollj_e4_hrig = huml.CreateVariable(\"Vollj_e4_hrig\", ( model) => huml.GREATER_OR_EQUALS(model.Alter, 18.0));"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Vollj_e4_hrig = huml.CreateVariable(\"Vollj_e4_hrig\", ( model) -> huml.GREATER_OR_EQUALS(model.getAlter(), 18.0));"),
        Arguments.of(
            "python",
            "self.Vollj_e4_hrig = huml.Variable(\"Vollj_e4_hrig\", lambda model: huml.GREATER_OR_EQUALS(model.Alter, 18.0))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_value_as_condition(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder
              .createVariable("Volljährig")
              .createValueAsCondition()
              .withLeftOperandAsProperty("Alter")
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_value_as_simple_string() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Bundeskanzlerin = huml.createVariable(\"Bundeskanzlerin\", function(model) { return \"Angela Merkel\"; });"),
        Arguments.of(
            "csharp",
            "var Bundeskanzlerin = huml.CreateVariable(\"Bundeskanzlerin\", ( model) => \"Angela Merkel\");"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Bundeskanzlerin = huml.CreateVariable(\"Bundeskanzlerin\", ( model) -> \"Angela Merkel\");"),
        Arguments.of(
            "python",
            "self.Bundeskanzlerin = huml.Variable(\"Bundeskanzlerin\", lambda model: \"Angela Merkel\")"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_value_as_simple_string(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder.createVariable("Bundeskanzlerin").withValueAsString("Angela Merkel");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_value_as_complex_string() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Adresse_20_von_20_BROCKHAUS_20_AG = huml.createVariable(\"Adresse_20_von_20_BROCKHAUS_20_AG\", function(model) { return \"Pierbusch 17, 44536 Lünen\"; });"),
        Arguments.of(
            "csharp",
            "var Adresse_20_von_20_BROCKHAUS_20_AG = huml.CreateVariable(\"Adresse_20_von_20_BROCKHAUS_20_AG\", ( model) => \"Pierbusch 17, 44536 Lünen\");"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Adresse_20_von_20_BROCKHAUS_20_AG = huml.CreateVariable(\"Adresse_20_von_20_BROCKHAUS_20_AG\", ( model) -> \"Pierbusch 17, 44536 Lünen\");"),
        Arguments.of(
            "python",
            "self.Adresse_20_von_20_BROCKHAUS_20_AG = huml.Variable(\"Adresse_20_von_20_BROCKHAUS_20_AG\", lambda model: \"Pierbusch 17, 44536 Lünen\")"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_value_as_complex_string(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder
              .createVariable("Adresse von BROCKHAUS AG")
              .withValueAsString("Pierbusch 17, 44536 Lünen");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_value_as_number() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Einkommen = huml.createVariable(\"Einkommen\", function(model) { return 10000.0; });"),
        Arguments.of(
            "csharp", "var Einkommen = huml.CreateVariable(\"Einkommen\", ( model) => 10000.0);"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Einkommen = huml.CreateVariable(\"Einkommen\", ( model) -> 10000.0);"),
        Arguments.of(
            "python", "self.Einkommen = huml.Variable(\"Einkommen\", lambda model: 10000.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_value_as_number(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          // Nur Integer werden akzeptiert
          builder.createVariable("Einkommen").withValueAsNumber(10000);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_value_as_number_double_short() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Einkommen = huml.createVariable(\"Einkommen\", function(model) { return 123.89; });"),
        Arguments.of(
            "csharp", "var Einkommen = huml.CreateVariable(\"Einkommen\", ( model) => 123.89);"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Einkommen = huml.CreateVariable(\"Einkommen\", ( model) -> 123.89);"),
        Arguments.of(
            "python", "self.Einkommen = huml.Variable(\"Einkommen\", lambda model: 123.89)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_value_as_number_double_short(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          // Nur Integer werden akzeptiert
          builder.createVariable("Einkommen").withValueAsNumber(123.89);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_value_as_number_double_long() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Einkommen = huml.createVariable(\"Einkommen\", function(model) { return 1.2345678989E8; });"),
        Arguments.of(
            "csharp",
            "var Einkommen = huml.CreateVariable(\"Einkommen\", ( model) => 1.2345678989E8);"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Einkommen = huml.CreateVariable(\"Einkommen\", ( model) -> 1.2345678989E8);"),
        Arguments.of(
            "python",
            "self.Einkommen = huml.Variable(\"Einkommen\", lambda model: 1.2345678989E8)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_value_as_number_double_long(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder.createVariable("Einkommen").withValueAsNumber(123456789.89);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_arithmetic_operation() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Einkommen = huml.createVariable(\"Einkommen\", function(model) { return huml.EQUALS((2.2*10.5)); });"),
        Arguments.of(
            "csharp",
            "var Einkommen = huml.CreateVariable(\"Einkommen\", ( model) => huml.EQUALS((2.2*10.5)));"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Einkommen = huml.CreateVariable(\"Einkommen\", ( model) -> huml.EQUALS((2.2*10.5)));"),
        Arguments.of(
            "python",
            "self.Einkommen = huml.Variable(\"Einkommen\", lambda model: huml.EQUALS((2.2*10.5)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_arithmetic_operation(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder
              .createVariable("Einkommen")
              .createValueAsCondition()
              .withOperator(ASTComparisonOperator.EQUALS)
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2.2)
              .multiply(10.5);
          // .withValueAsNumber(123456789.89);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_value_as_property() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Stra_df_e = huml.createVariable(\"Stra_df_e\", function(model) { return model.Antrag.Person.Adresse.Strasse; });"),
        Arguments.of(
            "csharp",
            "var Stra_df_e = huml.CreateVariable(\"Stra_df_e\", ( model) => model.Antrag.Person.Adresse.Strasse);"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable Stra_df_e = huml.CreateVariable(\"Stra_df_e\", ( model) -> model.getAntrag().getPerson().getAdresse().getStrasse());"),
        Arguments.of(
            "python",
            "self.Stra_df_e = huml.Variable(\"Stra_df_e\", lambda model: model.Antrag.Person.Adresse.Strasse)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_value_as_property(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder
              .createVariable("Straße")
              .withValueAsProperty("Antrag", "Person", "Adresse", "Strasse");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_simple_constraint_property() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Adresse2Rule = huml.createVariable(\"Adresse2Rule\", function(model) { return model.Antrag.Personen.Where(x -> huml.EQUALS(model.Name, \"Jefferson\") }).Name; });"),
        Arguments.of(
            "csharp",
            "var Adresse2Rule = huml.CreateVariable(\"Adresse2Rule\", ( model) => model.Antrag.Personen.Where(x -> huml.EQUALS(model.Name, \"Jefferson\") }).Name);"),
        // Personen wird von Java verschluckt
        Arguments.of(
            "java",
            "HUMLFramework.Variable Adresse2Rule = huml.CreateVariable(\"Adresse2Rule\", ( model) -> model.getAntrag().getPersonen().Where(x ->  huml.EQUALS(model.getName(), \"Jefferson\") }).getName());"));
  }

  @Disabled
  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_simple_constraint_property(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder.createVariable("Adresse2Rule").createValueAsProperty().appendAccessor("Antrag");
          //                                .appendConstraint("Personen")
          //                                .withLeftOperandAsProperty("Name")
          //                                .withOperator(ASTComparisonOperator.EQUALS)
          //                                    .withRightOperandAsString("Jefferson")
          //                                    .parentProperty()
          //                                    .appendAccessor("Name");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_simple_constraints_property() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var Adresse2Rule = huml.createVariable(\"Adresse_20_Rule_20_Test_20_Long_20_Variable\", function(model) { return model.Antrag.Personen.Where(x -> huml.EQUALS(model.Name, \"Jefferson\") }).Name; });"),
        Arguments.of(
            "csharp",
            "var Adresse2Rule = huml.CreateVariable(\"Adresse_20_Rule_20_Test_20_Long_20_Variable\", ( model) => model.Antrag.Personen.Where(x -> huml.EQUALS(model.Name, \"Jefferson\") }).Name);"),
        // Personen wird von Java verschluckt
        Arguments.of(
            "java",
            "HUMLFramework.Variable Adresse_20_Rule_20_Test_20_Long_20_Variable = huml.CreateVariable(\"Adresse_20_Rule_20_Test_20_Long_20_Variable\", ( model) -> model.getAntrag().getPersonen().Where(x ->  huml.EQUALS(model.getName(), \"Jefferson\") }).getName());"));
  }

  @Disabled
  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_simple_constraints_property(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder
              .createVariable("Adresse Rule Test Long Variable")
              .createValueAsProperty()
              .appendAccessor("Antrag");
          //                        .appendConstraint("Personen")
          //                        .withLeftOperandAsProperty("Name")
          //                        .withOperator(ASTComparisonOperator.EQUALS)
          //                        .withRightOperandAsString("Jefferson")
          //                        .parentProperty()
          //                        .appendAccessor("Name");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_string_array() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var capitals = huml.createVariable(\"capitals\", function(model) { return huml.CREATE_ARRAY(\"Berlin\",\"Paris\",\"London\"); });"),
        Arguments.of(
            "csharp",
            "var capitals = huml.CreateVariable(\"capitals\", ( model) => huml.CREATE_ARRAY(\"Berlin\",\"Paris\",\"London\"));"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable capitals = huml.CreateVariable(\"capitals\", ( model) -> huml.CREATE_ARRAY(\"Berlin\",\"Paris\",\"London\"));"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_string_array(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder
              .createVariable("capitals")
              .createValueAsArray()
              .addItem("Berlin")
              .addItem("Paris")
              .addItem("London");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> variable_with_number_array() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "var numbers = huml.createVariable(\"numbers\", function(model) { return huml.CREATE_ARRAY(1.0,2.0,3.0); });"),
        Arguments.of(
            "csharp",
            "var numbers = huml.CreateVariable(\"numbers\", ( model) => huml.CREATE_ARRAY(1.0,2.0,3.0));"),
        Arguments.of(
            "java",
            "HUMLFramework.Variable numbers = huml.CreateVariable(\"numbers\", ( model) -> huml.CREATE_ARRAY(1.0,2.0,3.0));"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void variable_with_number_array(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder builder = new ASTVariableBuilder(new ASTModelBuilder());
          builder
              .createVariable("numbers")
              .createValueAsArray()
              .addItem(new ASTOperandStaticNumber(1))
              .addItem(new ASTOperandStaticNumber(2))
              .addItem(new ASTOperandStaticNumber(3));

          return builder.getModel();
        });
  }
}
