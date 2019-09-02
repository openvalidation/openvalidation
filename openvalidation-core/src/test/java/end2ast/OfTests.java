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

package end2ast;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class OfTests {

  @Test
  public void single_of() throws Exception {
    String rule = "Berlin ALS Hauptstadt\n\nder Ort muss Hauptstadt sein";

    End2AstRunner.run(
        rule,
        "{Name:'Satoshi',Alter:25,Ort:'Dortmund'}",
        "de",
        r -> {
          r.sizeOfElements(2)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasPath("Ort")
              .hasType(DataPropertyType.String)
              .parentCondition()
              .rightVariable()
              .hasName("Hauptstadt")
              .parentModel()
              .variables()
              .hasSizeOf(1)
              .first()
              .hasString("Berlin")
              .hasName("Hauptstadt");
        });
  }

  @Test
  public void multiple_of() throws Exception {
    String rule = "der Ort des Landes muss Dortmund sein";

    End2AstRunner.run(
        rule,
        "{Ort:''}",
        "de",
        r -> {
          r.sizeOfElements(1)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasPath("Ort")
              .hasType(DataPropertyType.String)
              .parentCondition()
              .rightString("Dortmund");
        });
  }

  @Test
  public void multiple_of_in_if_then() throws Exception {
    String rule = "wenn der Name des Benutzers gleich Validaria ist dann tues";

    End2AstRunner.run(
        rule,
        "{Name:''}",
        "de",
        r -> {
          r.sizeOfElements(1)
              .rules()
              .hasSizeOf(1)
              .first()
              .hasError("tues")
              .condition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty()
              .hasPath("Name")
              .hasType(DataPropertyType.String)
              .parentCondition()
              .rightString("Validaria");
        });
  }

  //
  @Test
  public void as_alias_() throws Exception {
    String rule = "die angegebene Berufserfahrung DARF nicht GRÖßER  \n Als 20 sein";

    End2AstRunner.run(
        rule,
        "{Berufserfahrung:0}",
        "de",
        r -> {
          r.sizeOfElements(1)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.GREATER_THAN)
              .leftProperty()
              .hasPath("Berufserfahrung")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightNumber(20.0);
        });
  }

  @Test
  void simple_of_accessor_with_one_property() throws Exception {
    String rule = "die größe der person muss GRÖßER Als 100 sein";

    End2AstRunner.run(
        rule,
        "{Person: {Alter: 25, Größe: 180}}",
        "de",
        r -> {
          r.sizeOfElements(1)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .leftProperty()
              .hasPath("Person.Größe")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightNumber(100.0);
        });
  }

  @Test
  void simple_of_accessor_with_two_properties() throws Exception {
    String rule = "die größe der person muss GRÖßER Als das Alter der Person sein";

    End2AstRunner.run(
        rule,
        "{Person: {Alter: 25, Größe: 180}}",
        "de",
        r -> {
          r.sizeOfElements(1)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .leftProperty()
              .hasPath("Person.Größe")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightProperty()
              .hasPath("Person.Alter")
              .hasType(DataPropertyType.Decimal);
        });
  }

  @Test
  void simple_of_accessor_with_one_deep_property() throws Exception {
    String rule = "die breite von person.maße muss GRÖßER Als 0 sein";

    End2AstRunner.run(
        rule,
        "{Person: {Maße: {Breite: 55, Größe: 180}}}",
        "de",
        r -> {
          r.sizeOfElements(1)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .leftProperty()
              .hasPath("Person.Maße.Breite")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightNumber(0.0);
        });
  }

  @Test
  void simple_of_accessor_with_multiple_deep_properties() throws Exception {
    String rule = "0 als null\n\n1 als eins\n\ndie alter von person.infos muss GRÖßER Als 0 sein";

    End2AstRunner.run(
        rule,
        "{Person: {Infos: {Name: Peter, Alter: 20}, Maße: {Breite: 55, Größe: 180}}}",
        "de",
        r -> {
          r.sizeOfElements(3)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .leftProperty()
              .hasPath("Person.Infos.Alter")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightNumber(0.0);
        });
  }

  @ParameterizedTest
  @CsvSource({
    "Height of Metrics BIGGER THAN 180 AS over-average",
    "The Height of Metrics BIGGER THAN 180 AS over-average",
    "The Height measurement of Metrics BIGGER THAN 180 AS over-average",
    "Height of the Metrics BIGGER THAN 180 AS over-average",
    "The Height of the Metrics BIGGER THAN 180 AS over-average",
    "The Height measurement of the Metrics BIGGER THAN 180 AS over-average",
    "Height of the Metrics given BIGGER THAN 180 AS over-average",
    "The Height of the Metrics given BIGGER THAN 180 AS over-average",
    "The Height measurement of the Metrics given BIGGER THAN 180 AS over-average",
    "Height of the Metrics given is BIGGER THAN 180 AS over-average",
    "The Height of the Metrics given is BIGGER THAN 180 AS over-average",
    "The Height measurement of the Metrics given is BIGGER THAN 180 AS over-average"
  })
  void simple_of_accessor_on_unique_property_in_comparison(String variable) throws Exception {
    End2AstRunner.run(
        variable,
        "{Person: {Info: {Name: Peter, Age: 20}, Metrics: {Breadth: 55, Height: 180}}}",
        r -> {
          r.sizeOfElements(1)
              .variables()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.GREATER_THAN)
              .leftProperty()
              .hasPath("Person.Metrics.Height")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightNumber(180.0);
        });
  }

  @ParameterizedTest
  @CsvSource({
    "adult of person is true as var1",
    "the property adult of person is true as var1",
    "the property adult here of person is true as var1",
    "adult of person is indeed true as var1",
    "the property adult of person is indeed true as var1",
    "the property adult here of person is indeed true as var1",
    "adult of person is indeed true as fact as var1",
    "the property adult of person is indeed true as fact as var1",
    "the property adult here of person is indeed true as fact as var1",
  })
  void simple_of_accessor_on_unique_property_with_bool_comparison(String variable)
      throws Exception {
    End2AstRunner.run(
        variable,
        "{Protocol: {Person: {Name: Peter, Age: 20, Adult: true}}}",
        r -> {
          r.sizeOfElements(1)
              .variables()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty()
              .hasPath("Protocol.Person.Adult")
              .hasType(DataPropertyType.Boolean)
              .parentCondition()
              .rightBoolean(true);
        });
  }

  @ParameterizedTest
  @CsvSource({
    "If adult of person then error",
    "If the property adult of person then error",
    "If the property adult here of person then error"
  })
  void simple_of_accessor_on_unique_property_with_bool_comparison_implicit(String variable)
      throws Exception {
    End2AstRunner.run(
        variable,
        "{Protocol: {Person: {Name: Peter, Age: 20, Adult: true}}}",
        r -> {
          r.sizeOfElements(1)
              .rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty()
              .hasPath("Protocol.Person.Adult")
              .hasType(DataPropertyType.Boolean)
              .parentCondition()
              .rightBoolean(true);
        });
  }

  @ParameterizedTest
  @CsvSource({
    "name of random person AS var1",
    "the property name of random person AS var1",
    "the property name here of random person AS var1"
  })
  void simple_of_accessor_on_unique_property_with_underscore_by_using_space_entity(String variable)
      throws Exception {
    End2AstRunner.run(
        variable,
        "{Protocol: {Random_Person: {Name: Peter, Age: 20, Adult: true}}}",
        r -> {
          r.sizeOfElements(1)
              .variables()
              .hasSizeOf(1)
              .first()
              .operandProperty()
              .hasPath("Protocol.Random_Person.Name")
              .hasType(DataPropertyType.String);
        });
  }

  @ParameterizedTest
  @CsvSource({
    "first name of person AS var1",
    "the property first name of person AS var1",
    "the property first name here of person AS var1",
    "the first name of the person in question AS var1"
  })
  void simple_of_accessor_on_unique_property_with_underscore_by_using_space_property(
      String variable) throws Exception {
    End2AstRunner.run(
        variable,
        "{Protocol: {Person: {First_Name: Peter, Age: 20, Adult: true}}}",
        r -> {
          r.sizeOfElements(1)
              .variables()
              .hasSizeOf(1)
              .first()
              .operandProperty()
              .hasPath("Protocol.Person.First_Name")
              .hasType(DataPropertyType.String);
        });
  }
}
